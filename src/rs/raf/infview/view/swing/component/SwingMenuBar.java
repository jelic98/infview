package rs.raf.infview.view.swing.component;

import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.MenuBar;
import javax.swing.*;

public class SwingMenuBar extends JMenuBar implements MenuBar {

    @Override
    public void addComponent(Component component) {
        add((java.awt.Component) component);
    }

    @Override
    public void addGlue() {
        add(Box.createHorizontalGlue());
    }
}