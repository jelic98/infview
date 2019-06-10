package rs.raf.infview.view.frame;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.command.Command;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.FileOperator;
import rs.raf.infview.view.adapter.action.ActionItem;
import rs.raf.infview.view.adapter.action.ButtonFactory;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.component.*;
import java.util.Iterator;

public class TrashFrame extends FrameTemplate {

	private Entity entity;
	private ChangeObserver<Entity> callback;

    public TrashFrame(Entity entity, ChangeObserver<Entity> callback) {
        super(Res.STRINGS.FRAME_TRASH, true);

        this.entity = entity;
        this.callback = callback;
    }

	@Override
	protected void configure() {
        setSizeRatio(0.3f);

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
		Entity clone = (Entity) entity.getClone();
		clone.setRecords(entity.getTrash());

		Table table = App.UI.getTable();
		table.setModel(clone);

		ScrollPanel scrollTable = App.UI.getScrollPanel();
		scrollTable.addComponent(table);

		ButtonFactory factory = new ButtonFactory();
		Button btnRestore = factory.getButton(new ActionItem(Res.STRINGS.OPTION_RESTORE, Res.ICONS.RESTORE, new Command() {
			@Override
			public void execute() {
				entity.addRecord(getSelectedRecord(clone, table));

				callback.onChange(ChangeType.REFRESH, entity);

				populate();
			}
		}));
		Button btnDelete = factory.getButton(new ActionItem(Res.STRINGS.MENU_DELETE, Res.ICONS.DELETE, new Command() {
			@Override
			public void execute() {
				Record selected = getSelectedRecord(clone, table);

				AbstractResource file = Context.instance().fileMatcher.get(entity);

				if(file != null) {
					FileOperator operator = new FileOperator(file);
					operator.delete(selected);
				}

				entity.removeRecord(selected, true);

				callback.onChange(ChangeType.REFRESH, entity);

				populate();
			}
		}));

		Panel row = App.UI.getPanel();
		row.setLayout(Panel.Layout.SHRINK);
		row.addComponent(btnRestore);
		row.addComponent(btnDelete);

		Panel layout = App.UI.getPanel();
		layout.setPadding(20, 20, 20, 20);
		layout.setLayout(Panel.Layout.NORMAL);
		layout.addComponent(scrollTable);
		layout.addComponent(row);

		frame.clear();
        frame.addComponent(layout);
		frame.pack();
	}

	private Record getSelectedRecord(Entity entity, Table table) {
		int selectedIndex = table.getSelectedRow();

		if(selectedIndex == -1 || entity.getRecords().isEmpty()) {
			DialogAdapter.error(Res.STRINGS.ERROR_RECORD_NOT_SELECTED);
			return null;
		}

		Iterator<Record> i = entity.getRecords().iterator();

		if(entity.getRecords().size() > 1) {
			while(i.hasNext() && selectedIndex-- > 0) {
				i.next();
			}
		}

		return i.next();
	}
}