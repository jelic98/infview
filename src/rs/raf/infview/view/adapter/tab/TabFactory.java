package rs.raf.infview.view.adapter.tab;

import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.Relation;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.ObservableSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TabFactory {

    private ObservableSet<TabItem> tabsBottom;
    private ChangeObserver<Entity> callback;

    public TabFactory(ObservableSet<TabItem> tabsBottom, ChangeObserver<Entity> callback) {
        this.tabsBottom = tabsBottom;
        this.callback = callback;
    }

    public TabComponent getTab(Entity entity, boolean child) {
        TabComponent tab;

        if(child) {
            tab = new EntityTab(entity);
        }else {
            tab = new EntityTab(entity, new ChangeObserverAdapter<Integer>() {
                @Override
                public void onChange(ChangeType type, Integer bundle) {
                    if(entity.getRecords().isEmpty()) {
                        return;
                    }

                    Iterator<Record> i = entity.getRecords().iterator();

                    if(entity.getRecords().size() > 1) {
                        while(i.hasNext() && bundle-- > 0) {
                            i.next();
                        }
                    }

                    Record selected = i.next();

                    Context.instance().record = selected;

                    for(TabItem tabItem : tabsBottom) {
                        Entity entity = (Entity) tabItem.getModel();

                        Set<Record> filtered = new TreeSet<>();

                        for(Record record : entity.getRecords()) {
                            for(Attribute a : entity.getAttributes()) {
                                if(a.isRelation()) {
                                    Relation r = a.getRelation();

                                    if(r.getParent().equals(selected.getParent())) {
                                        filtered.add(record);
                                    }
                                }
                            }
                        }

                        entity.setFiltered(filtered);
                    }

                    callback.onChange(ChangeType.REFRESH);
                }
            });
        }

        return tab;
    }
}