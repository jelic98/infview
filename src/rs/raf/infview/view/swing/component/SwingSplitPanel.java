package rs.raf.infview.view.swing.component;

import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.SplitPanel;
import javax.swing.*;

public class SwingSplitPanel extends JSplitPane implements SplitPanel {

    @Override
    public void setTop(Component component) {
        setOrientation(JSplitPane.VERTICAL_SPLIT);
        setTopComponent((java.awt.Component) component);
    }

    @Override
    public void setBottom(Component component) {
        setOrientation(JSplitPane.VERTICAL_SPLIT);
        setBottomComponent((java.awt.Component) component);
    }

    @Override
    public void setLeft(Component component) {
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setLeftComponent((java.awt.Component) component);
    }

    @Override
    public void setRight(Component component) {
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setRightComponent((java.awt.Component) component);
    }
}