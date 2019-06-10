package rs.raf.infview.view.swing.component;

import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.PopupMenu;
import javax.swing.*;

public class SwingPopupMenu extends JPopupMenu implements PopupMenu {

    @Override
    public void addComponent(Component component) {
        add((java.awt.Component) component);
    }

    @Override
    public void show(int x, int y, Object parent) {
        show((java.awt.Component) parent, x, y);
    }
}