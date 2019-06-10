package rs.raf.infview.view.component;

import rs.raf.infview.observer.ChangeObservable;

public interface TextArea extends Component, ChangeObservable<String> {

    String getText();
    void setText(String text);
    void setRows(int rows);
    void setColumns(int columns);
    void setEditable(boolean editable);
}