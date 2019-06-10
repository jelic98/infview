package rs.raf.infview.view.component;

import rs.raf.infview.observer.ChangeObservable;

public interface Table extends Component, ChangeObservable<Integer> {

    void setModel(TableModel model);
    int getSelectedRow();
    void refresh();
}