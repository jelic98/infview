package rs.raf.infview.view.component;

import rs.raf.infview.observer.ChangeObservable;
import rs.raf.infview.view.adapter.tab.TabItem;

public interface TabPanel extends Component, ChangeObservable<TabItem> {

    void add(String title, byte[] icon, Component component);
    void removeAt(int index);
    int getSelectedIndex();
    void setPopupMenu(PopupMenu menu);
    void clear();
}