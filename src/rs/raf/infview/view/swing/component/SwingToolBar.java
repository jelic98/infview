package rs.raf.infview.view.swing.component;

import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.ToolBar;
import javax.swing.*;

public class SwingToolBar extends JToolBar implements ToolBar {

    @Override
    public void addComponent(Component component) {
        add((java.awt.Component) component);
    }
}