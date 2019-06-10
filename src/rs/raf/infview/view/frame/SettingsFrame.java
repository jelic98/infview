package rs.raf.infview.view.frame;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.command.ApplySettingsAction;
import rs.raf.infview.observer.command.DiscardSettingsAction;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.view.adapter.action.ActionItem;
import rs.raf.infview.view.adapter.action.ButtonFactory;
import rs.raf.infview.view.component.Label;
import rs.raf.infview.view.component.Panel;
import rs.raf.infview.view.component.TextField;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SettingsFrame extends FrameTemplate {

    public SettingsFrame() {
        super(Res.STRINGS.FRAME_SETTINGS);
    }

	@Override
	protected void configure() {
        frame.addObserver(new ChangeObserverAdapter() {
            @Override
            public void onChange(ChangeType type) {
                if(type == ChangeType.CLOSE) {
                    close();
                }
            }
        });
	}
	
	@Override
	protected void populate() {
	    Set<String> keys = App.RUNTIME.getKeys();
        Map<String, String> draft = new HashMap<>(keys.size());

        Panel layout = App.UI.getPanel();
        layout.setPadding(20, 20, 20, 20);
        layout.setLayout(Panel.Layout.NORMAL);

		for(String key : keys) {
            Panel row = App.UI.getPanel();
            row.setLayout(Panel.Layout.SHRINK);

		    String value = App.RUNTIME.get(key);

		    draft.put(key, value);

            Label name = App.UI.getLabel();
            name.setText(key.replace('_', ' '));
		    row.addComponent(name);

            TextField input = App.UI.getTextField();
            input.setText(value);
            input.setColumns(10);
            input.addObserver(new ChangeObserverAdapter() {
                @Override
                public void onChange() {
                    draft.put(key, input.getText());
                }
            });
            row.addComponent(input);

			layout.addComponent(row);
		}

		ButtonFactory factory = new ButtonFactory();

        Panel row = App.UI.getPanel();
        row.setLayout(Panel.Layout.SHRINK);
        row.addComponent(factory.getButton(new ActionItem(Res.STRINGS.OPTION_CANCEL, new DiscardSettingsAction(this))));
        row.addComponent(factory.getButton(new ActionItem(Res.STRINGS.OPTION_OK, new ApplySettingsAction(keys, draft, this))));
        layout.addComponent(row);

        frame.addComponent(layout);
        frame.pack();
	}
}