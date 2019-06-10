package rs.raf.infview.view.adapter.tab;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.ChangeObservable;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.command.*;
import rs.raf.infview.view.adapter.action.CompositeActionItem;
import rs.raf.infview.view.adapter.action.PopupMenuFactory;
import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.TabPanel;
import java.util.LinkedList;
import java.util.List;

public class ObservableTabPanel extends ChangeObserverAdapter<TabItem> implements ChangeObservable<TabItem> {

    private final ChangeObservableDelegate<TabItem> delegate;
    private final List<TabItem> items;
    private final TabPanel panel;

    ObservableTabPanel() {
        delegate = new ChangeObservableDelegate<>();
        items = new LinkedList<>();
        panel = App.UI.getTabPanel();
        panel.addObserver(this);
        panel.setPopupMenu(new PopupMenuFactory().getPopupMenu(new CompositeActionItem[] {
                new CompositeActionItem(Res.STRINGS.MENU_CLOSE, new CloseTabAction(null, null) {
                    @Override
                    public void execute() {
                        removeItem(items.get(panel.getSelectedIndex()));
                    }
                })
        }));
    }

    public void addItem(TabItem item) {
        if(items.contains(item)) {
            return;
        }

        panel.add(item.getText(), item.getIcon(), item.getComponent());
        items.add(item);
    }

    public void removeItem(TabItem item) {
        int index = items.indexOf(item);

        if(index == -1) {
            return;
        }

        panel.removeAt(index);
        items.remove(item);

        notifyObservers(ChangeType.REMOVAL, item);
    }

    public Component getView() {
        return panel;
    }

    @Override
    public void addObserver(ChangeObserver observer) {
        delegate.addObserver(observer);
        panel.addObserver(observer);
    }

    @Override
    public void notifyObservers(ChangeType type, TabItem bundle) {
        delegate.notifyObservers(type, bundle);
    }

	@Override
	public void onChange(ChangeType type) {
		if(type == ChangeType.SELECTION) {
            int index = panel.getSelectedIndex();

		    if(items.isEmpty() || index == -1) {
		        return;
            }

		    notifyObservers(type, items.get(index));
		}
	}
}