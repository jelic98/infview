package rs.raf.infview.view.adapter.tab;

import rs.raf.infview.core.App;
import rs.raf.infview.model.Entity;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.view.component.*;

public class EntityTab extends TabComponent {

    private Table table;

    public EntityTab(Entity entity) {
        this(entity, null);
	}
	
    public EntityTab(Entity entity, ChangeObserver<Integer> observer) {
        table = App.UI.getTable();
        table.setModel(entity);
        table.addObserver(observer);

        ScrollPanel panel = App.UI.getScrollPanel();
        panel.addComponent(table);

        component = panel;
    }

    @Override
    public void refresh() {
        table.refresh();
    }
}