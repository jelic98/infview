package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.Record;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.state.Context;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Entity;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.util.ObservableSet;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.adapter.tab.EntityTab;
import rs.raf.infview.view.adapter.tab.TabComponent;
import rs.raf.infview.view.adapter.tab.TabItem;
import rs.raf.infview.view.component.Node;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class DetailsAction extends Command {

    private ObservableSet<TabItem> tabsTop, tabsBottom;
    private ChangeObserver<Entity> callback;

    public DetailsAction(ObservableSet<TabItem> tabsTop, ObservableSet<TabItem> tabsBottom, ChangeObserver<Entity> callback) {
        this.tabsTop = tabsTop;
        this.tabsBottom = tabsBottom;
        this.callback = callback;
    }

    @Override
    public void execute() {
        Node selectedNode = Context.instance().tree.getSelectedNode();

        if(selectedNode != null) {
            selectedNode = selectedNode.getDelegateModel();
        }

        if(selectedNode == null) {
            return;
        }

        if(!(selectedNode instanceof Entity)) {
            DialogAdapter.error(Res.STRINGS.ERROR_ENTITY_NOT_SELECTED);
            return;
        }

        Entity entity = (Entity) selectedNode;

        tabsTop.add(new TabItem(entity, getTab(entity, false)));

        tabsBottom.clear();

        for(Attribute a : entity.getAttributes()) {
            if(a.isRelation()) {
                Entity child = a.getRelation().getEntity();

                if(child != null) {
                    tabsBottom.add(new TabItem(child, getTab(child, true)));
                }
            }
        }
    }

    private TabComponent getTab(Entity entity, boolean child) {
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

                    for(String key : selected.getData().keySet()) {
                    	String value = selected.get(key);

                    	boolean isRelation = false;

                    	for(Attribute a : selected.getAttributes()) {
                    	    if(a.getName().equals(key)) {
                    	        isRelation = a.isRelation();
                    	        break;
                            }
                        }

                    	if(!isRelation) {
                    	    continue;
                        }

                    	for(TabItem tab : tabsBottom) {
                    		Set<Record> filtered = new TreeSet<>();
                    		Entity e = (Entity)tab.getModel();
                    		
                    		for(Record r1 : e.getRecords()) {
                    			if(r1.getPrimary().equals(value)) {
                    				filtered.add(r1);
                    			}
                    		}
                    		
                    		e.setFiltered(filtered);
                    	}
                    }
                    
                    for(TabItem tab : tabsBottom) {
                    	tab.refresh();
                    }
                    
                    callback.onChange(ChangeType.REFRESH);
                }
            });
        }

        return tab;
    }
}