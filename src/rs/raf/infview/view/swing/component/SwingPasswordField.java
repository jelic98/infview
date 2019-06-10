package rs.raf.infview.view.swing.component;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.component.PasswordField;

import javax.swing.*;

public class SwingPasswordField extends JPasswordField implements PasswordField {

    private ChangeObservableDelegate<Object> delegate;

    public SwingPasswordField() {
        delegate = new ChangeObservableDelegate<>();
    }

    @Override
    public void addObserver(ChangeObserver observer) {
        delegate.addObserver(observer);
    }

    @Override
    public void notifyObservers(ChangeType type, String bundle) {
        delegate.notifyObservers(type, bundle);
    }
}