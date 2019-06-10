package rs.raf.infview.view.swing.component;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.Menu;
import javax.swing.*;
import javax.swing.event.MenuEvent;

public class SwingMenu extends JMenu implements Menu {

    private ChangeObservableDelegate<Object> delegate;

    public SwingMenu() {
        delegate = new ChangeObservableDelegate<>();

        addMenuListener(new SwingMenuAdapter() {
            @Override
            public void menuSelected(MenuEvent e) {
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
}