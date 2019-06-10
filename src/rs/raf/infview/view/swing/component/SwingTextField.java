package rs.raf.infview.view.swing.component;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.component.TextField;
import javax.swing.*;

public class SwingTextField extends JTextField implements TextField {

    private ChangeObservableDelegate<Object> delegate;

    public SwingTextField() {
        delegate = new ChangeObservableDelegate<>();

        getDocument().addDocumentListener(new SwingDocumentAdapter() {
            @Override
            public void onUpdate() {
                notifyObservers(ChangeType.UPDATE, getText());
            }
        });
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