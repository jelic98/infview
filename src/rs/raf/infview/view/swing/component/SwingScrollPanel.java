package rs.raf.infview.view.swing.component;

import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.ScrollPanel;
import javax.swing.*;

public class SwingScrollPanel extends JScrollPane implements ScrollPanel {

    @Override
    public void addComponent(Component component) {
        setViewportView((java.awt.Component) component);
    }
}