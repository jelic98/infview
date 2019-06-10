package rs.raf.infview.view.frame;

import java.util.HashMap;
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

public class QueryRecordFrame extends RecordFrame{
	public static Map<Integer, Attribute> avgInfo;
	private boolean apply = false;
	
	public QueryRecordFrame(Entity entity, Record record, ChangeObserver<Record> callback) {
		super(entity, record, callback);
		avgInfo = new HashMap<>();
	}
	
	public Map<Integer, Attribute> getAvgInfo(){
		return avgInfo;
	}

	@Override
	protected void populate() {
		avgInfo.clear();
		Panel layout = App.UI.getPanel();
		layout.setPadding(20, 20, 20, 20);
		layout.setLayout(Panel.Layout.NORMAL);

		Set<Attribute> attributes = entity.getAttributes();
		ComboBox<Attribute> avgCb = App.UI.getComboBox();
		avgCb.addObserver(new ChangeObserverAdapter() {
			@Override
			public void onChange() {
				avgInfo.put(0, (Attribute) avgCb.getSelectedItem());
			}
		});
		Label avgLbl = App.UI.getLabel();
		avgLbl.setText("Calculate Column\t");
		Label groupLBL = App.UI.getLabel();
		groupLBL.setText("Group By");
		Label attrLbl = App.UI.getLabel();
		attrLbl.setText("Attribute");
		
		Panel input = App.UI.getPanel();
		input.setLayout(Layout.SHRINK);
		
		Panel row = App.UI.getPanel();
		row.setLayout(Panel.Layout.SHRINK);
		row.addComponent(avgLbl);

		layout.addComponent(row);
		
		input = App.UI.getPanel();
		input.setLayout(Layout.SHRINK);
		
		row = App.UI.getPanel();
		row.setLayout(Panel.Layout.SHRINK);
		row.addComponent(avgCb);
		layout.addComponent(row);
		
		input = App.UI.getPanel();
		input.setLayout(Layout.SHRINK);
		
		row = App.UI.getPanel();
		row.setLayout(Panel.Layout.SHRINK);
		
		row.addComponent(attrLbl);
		input.addComponent(groupLBL);
		row.addComponent(input);
		layout.addComponent(row);
		Integer i = 1;
		for(Attribute a : attributes) {
			avgCb.addItem(a);
			
			final String key = a.getName();
			String hint = key;

			Label label = App.UI.getLabel();
			label.setText(hint);

			String value = record.get(a.getName());

			if(value == null) {
				value = a.getDefaultValue();
				record.set(key, value);
			}

			input = App.UI.getPanel();
			input.setLayout(Layout.SHRINK);
			CheckBox checkBox = App.UI.getCheckBox();
			checkBox.addObserver(new ChangeObserverAdapter() {
				@Override
				public void onChange() {
					if(checkBox.isChecked()) {
						avgInfo.put(i.sum(i, 1), a);
					}else {
						avgInfo.remove(a);
					}
				}
			});
			input.addComponent(checkBox);
			
			row = App.UI.getPanel();
			row.setLayout(Panel.Layout.SHRINK);
			row.addComponent(label);
			row.addComponent(input);

			layout.addComponent(row);
		}

		Button btnApply = new ButtonFactory().getButton(new ActionItem(Res.STRINGS.MENU_APPLY, new Command() {
			@Override
			public void execute() {
				apply = true;
				callback.onChange(ChangeType.APPLY, record);

				close();
			}
		}));

		row = App.UI.getPanel();
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
