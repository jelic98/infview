package rs.raf.infview.view.frame;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.*;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.command.Command;
import rs.raf.infview.view.adapter.action.ActionItem;
import rs.raf.infview.view.adapter.action.ButtonFactory;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.component.*;

import java.util.Map;
import java.util.Set;

public class RecordFrame extends FrameTemplate {

    protected Entity entity;
    protected Record record;
	protected ChangeObserver<Record> callback;

    public RecordFrame(Entity entity, Record record, ChangeObserver<Record> callback) {
        super(Res.STRINGS.FRAME_RECORD, true);

        record.setParent(entity);

        this.entity = entity;
		this.record = record;
        this.callback = callback;
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
		Panel layout = App.UI.getPanel();
		layout.setPadding(20, 20, 20, 20);
		layout.setLayout(Panel.Layout.NORMAL);

		Set<Attribute> attributes = entity.getAttributes();

		for(Attribute a : attributes) {
			final String key = a.getName();
			String hint = key;

			if(a.isPrimary()) {
				hint = "<html><p style=\"color:red;\">" + hint + "</p></html>";
			}

			Label label = App.UI.getLabel();
			label.setText(hint);

			String value = record.get(a.getName());

			if(value == null) {
				value = a.getDefaultValue();
				record.set(key, value);
			}

			TextField input = App.UI.getTextField();
			input.setText(value);
			input.addObserver(new ChangeObserverAdapter() {
				@Override
				public void onChange() {
					record.set(key, input.getText());
				}
			});

			Panel row = App.UI.getPanel();
			row.setLayout(Panel.Layout.SHRINK);
			row.addComponent(label);
			row.addComponent(input);

			layout.addComponent(row);
		}

		Button btnApply = new ButtonFactory().getButton(new ActionItem(Res.STRINGS.MENU_APPLY, new Command() {
			@Override
			public void execute() {
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
}