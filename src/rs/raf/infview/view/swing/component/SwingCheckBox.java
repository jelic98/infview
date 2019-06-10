package rs.raf.infview.view.swing.component;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.component.CheckBox;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SwingCheckBox extends JCheckBox implements CheckBox {

    private ChangeObservableDelegate<Object> delegate;

    public SwingCheckBox() {
        delegate = new ChangeObservableDelegate<>();

        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                notifyObservers(ChangeType.UPDATE, e.getItem());
            }
        });
    }

    @Override
    public void addObserver(ChangeObserver observer) {
        delegate.addObserver(observer);
    }

    @Override
    public void notifyObservers(ChangeType type, Object bundle) {
        delegate.notifyObservers(type, bundle);
    }

    @Override
    public void setChecked(boolean checked) {
        setSelected(checked);
    }

    @Override
    public boolean isChecked() {
        return isSelected();
    }
}