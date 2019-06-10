package rs.raf.infview.view.adapter;

import rs.raf.infview.core.App;
import rs.raf.infview.model.Resource;
import rs.raf.infview.model.schema.*;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.util.Reflection;
import rs.raf.infview.util.log.Log;
import rs.raf.infview.view.component.*;
import java.lang.reflect.Field;
import java.util.*;

public class SchemaPanelFactory {

    private static final String REGEX_CAMEL_CASE = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
    private static final Reflection reflection = new Reflection();

    private Resource resource;
    private ChangeObserver observer;

    public SchemaPanelFactory(Resource resource) {
        this.resource = resource;
    }

    public SchemaPanelFactory(Resource resource, ChangeObserver observer) {
        this.resource = resource;
        this.observer = observer;
    }

    public Panel getPanel(Object model) {
        Panel panel = App.UI.getPanel();
        panel.setLayout(Panel.Layout.NORMAL);

        populatePanel(model, panel);

        return panel;
    }

    private void populatePanel(Object model, Panel panel) {
        Map<String, Object> fields = reflection.getSchema(model);

        for(String name : fields.keySet()) {
            Object value = fields.get(name);

            Field field = reflection.getField(model.getClass(), name);

            Label label = App.UI.getLabel();
            label.setText(formatLabel(name));

            TextField textField = App.UI.getTextField();
            textField.setText(value == null ? "" : value.toString());
            textField.addObserver(new ChangeObserverAdapter() {
                @Override
                public void onChange() {
                    reflection.invokeSetter(field, model, textField.getText());
                }
            });

            Component input = textField;

            Class type = field.getType();

            if(type != null) {
                if(type.isAnnotationPresent(SchemaCollection.class)) {
                    continue;
                }else if(type.isAnnotationPresent(SchemaOptions.class)) {
                    populatePanel(value, panel);
                    continue;
                }else if(type.isAnnotationPresent(SchemaObject.class)) {
                    Set<Object> objects = new HashSet<>();

                    extractCollections(resource, objects);

                    ComboBox<Object> comboBox = App.UI.getComboBox();
                    comboBox.setItems(objects.toArray());
                    comboBox.setSelectedItem(value);
                    comboBox.addObserver(new ChangeObserverAdapter() {
                        @Override
                        public void onChange() {
                            reflection.invokeSetter(field, model, comboBox.getSelectedItem());
                        }
                    });

                    input = comboBox;
                }else if(type.isAnnotationPresent(SchemaType.class)) {
                    ComboBox<Object> comboBox = App.UI.getComboBox();
                    comboBox.setItems(type.getEnumConstants());
                    comboBox.setSelectedItem(value);
                    comboBox.addObserver(new ChangeObserverAdapter() {
                        @Override
                        public void onChange() {
                            reflection.invokeSetter(field, model, comboBox.getSelectedItem());

                            if(observer != null) {
                                observer.onChange(ChangeType.UPDATE);
                            }
                        }
                    });

                    input = comboBox;
                }else if(type.equals(boolean.class) || type.equals(Boolean.class)) {
                    CheckBox checkBox = App.UI.getCheckBox();
                    checkBox.setChecked(Boolean.parseBoolean(String.valueOf(value)));
                    checkBox.addObserver(new ChangeObserverAdapter() {
                        @Override
                        public void onChange() {
                            reflection.set(field, model, checkBox.isChecked());
                        }
                    });

                    input = checkBox;
                }
            }

            Panel row = App.UI.getPanel();
            row.setLayout(Panel.Layout.SHRINK);
            row.addComponent(label);
            row.addComponent(input);

            panel.addComponent(row);
        }
    }

    private void extractCollections(Object parent, Set<Object> children) {
        Field[] collections = reflection.getAnnotatedFields(parent.getClass(), SchemaCollection.class, true);

        for(Field f : collections) {
            try {
                children.addAll((Collection<?>) reflection.get(f, parent));
            }catch(ClassCastException e) {
                Log.e(e.getMessage());
            }
        }
    }

    private String formatLabel(String s) {
        s = s.substring(0, 1).toUpperCase() + s.substring(1);

        StringBuilder builder = new StringBuilder();

        for(String w : s.split(REGEX_CAMEL_CASE)) {
            builder.append(w);
            builder.append(" ");
        }

        return builder.toString();
    }
}