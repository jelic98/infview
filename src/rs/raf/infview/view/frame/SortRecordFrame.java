package rs.raf.infview.view.frame;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.command.Command;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.AbstractResource.SortType;
import rs.raf.infview.view.adapter.action.ActionItem;
import rs.raf.infview.view.adapter.action.ButtonFactory;
import rs.raf.infview.view.component.Button;
import rs.raf.infview.view.component.CheckBox;
import rs.raf.infview.view.component.ComboBox;
import rs.raf.infview.view.component.Label;
import rs.raf.infview.view.component.Panel;
import rs.raf.infview.view.component.Panel.Layout;

public class SortRecordFrame extends RecordFrame{

	public static Map<String, AbstractResource.SortType> sortInfo = new LinkedHashMap<>();
	private boolean apply = false;
	
	public SortRecordFrame(Entity entity, Record record, ChangeObserver<Record> callback) {
		super(entity, record, callback);
	}
	
	public Map<String, AbstractResource.SortType> getSortInfo(){
		return sortInfo;
	}

	@Override
	protected void populate() {
		sortInfo.clear();
		Panel layout = App.UI.getPanel();
		layout.setPadding(20, 20, 20, 20);
		layout.setLayout(Panel.Layout.NORMAL);

		Set<Attribute> attributes = entity.getAttributes();

		for(Attribute a : attributes) {
			final String key = a.getName();
			String hint = key;

			if(a.isPrimary()) {
				hint = "<html><p style=\"color:blue;\">" + hint + "</p></html>";
			}

			Label label = App.UI.getLabel();
			label.setText(hint);

			String value = record.get(a.getName());

			if(value == null) {
				value = a.getDefaultValue();
				record.set(key, value);
			}

			Panel input = App.UI.getPanel();
			input.setLayout(Layout.SHRINK);
			CheckBox checkBox = App.UI.getCheckBox();
			ComboBox<AbstractResource.SortType> comboBox = App.UI.getComboBox();
			input.addComponent(checkBox);
			
			Panel row = App.UI.getPanel();
			row.setLayout(Panel.Layout.SHRINK);
			row.addComponent(label);
			row.addComponent(input);

			layout.addComponent(row);
			comboBox.addObserver(new ChangeObserverAdapter() {
				@Override
				public void onChange() {
					sortInfo.put(a.getName(), (SortType) comboBox.getSelectedItem());
				}
			});
			checkBox.addObserver(new ChangeObserverAdapter() {
				@Override
				public void onChange() {
					if(checkBox.isChecked()) {
						comboBox.addItem(SortType.ASCENDING);
						comboBox.addItem(SortType.DESCENDING);
						input.addComponent(comboBox);
					}else {
						sortInfo.remove(a.getName());
						comboBox.clear();
						input.clear();
						input.addComponent(checkBox);
					}
					frame.refresh();
				}
			});
		}

		Button btnApply = new ButtonFactory().getButton(new ActionItem(Res.STRINGS.MENU_APPLY, new Command() {
			@Override
			public void execute() {
				apply = true;
				callback.onChange(ChangeType.APPLY, record);

				close();
			}
		}));

		Panel row = App.UI.getPanel();
		row.setLayout(Panel.Layout.SHRINK);
		row.addComponent(btnApply);
		layout.addComponent(row);

		frame.addComponent(layout);
		frame.pack();
    }

	public boolean isApply() {
		return apply;
	}
}
