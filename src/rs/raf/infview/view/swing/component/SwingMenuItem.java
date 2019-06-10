package rs.raf.infview.view.swing.component;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.MenuItem;
import rs.raf.infview.view.swing.SwingUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingMenuItem extends JMenuItem implements MenuItem {

    private ChangeObservableDelegate<Object> delegate;

    public SwingMenuItem() {
        delegate = new ChangeObservableDelegate<>();

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(ChangeType.ACTIVATE, e.getSource());
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
    public void addComponent(Component component) {
        add((java.awt.Component) component);
    }

    @Override
    public void setIcon(byte[] icon) {
        if(icon != null) {
            setIcon(new ImageIcon(icon));
        }
    }

    @Override
    public void setAccelerator(char accelerator) {
        setAccelerator(KeyStroke.getKeyStroke(accelerator, SwingUI.KEY_MASK));
    }

    @Override
    public void setPadding(int top, int right, int bottom, int left) {
        setBorder(new EmptyBorder(top, left, bottom, right));
    }
}