package rs.raf.infview.view.component;

import rs.raf.infview.observer.ChangeObservable;

public interface ComboBox<T> extends Component, ChangeObservable<Object> {

    void setItems(T[] items);
    void addItem(T item);
    void clear();
    Object getSelectedItem();
    void setSelectedItem(Object item);
    void setEnabled(boolean enabled);
}