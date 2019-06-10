package rs.raf.infview.view.component;

import rs.raf.infview.observer.ChangeObservable;

public interface TextField extends Component, ChangeObservable<String> {

    String getText();
    void setText(String text);
    void setColumns(int columns);
    void setEditable(boolean editable);
}