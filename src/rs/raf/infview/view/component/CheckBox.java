package rs.raf.infview.view.component;

import rs.raf.infview.observer.ChangeObservable;

public interface CheckBox extends Component, ChangeObservable<Object> {

    boolean isChecked();
    void setChecked(boolean checked);
    void setEnabled(boolean enabled);
}