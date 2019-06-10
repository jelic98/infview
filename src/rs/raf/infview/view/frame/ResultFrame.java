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
import java.util.Set;

public class ResultFrame extends FrameTemplate {

	private Entity entity;
	private Set<Record> records;

    public ResultFrame(Entity entity, Set<Record> records) {
        super(Res.STRINGS.FRAME_RESULT, true);

        this.entity = entity;
        this.records = records;
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
		clone.setRecords(records);

		Table table = App.UI.getTable();
		table.setModel(clone);

		ScrollPanel scrollTable = App.UI.getScrollPanel();
		scrollTable.addComponent(table);

		Panel layout = App.UI.getPanel();
		layout.setPadding(20, 20, 20, 20);
		layout.setLayout(Panel.Layout.NORMAL);
		layout.addComponent(scrollTable);

		frame.clear();
        frame.addComponent(layout);
		frame.pack();
	}
}