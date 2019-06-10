package rs.raf.infview.view.swing.component;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.component.TextArea;
import javax.swing.*;

public class SwingTextArea extends JTextArea implements TextArea {

    private ChangeObservableDelegate<Object> delegate;

    public SwingTextArea() {
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