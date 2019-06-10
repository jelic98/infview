package rs.raf.infview.view.swing.component;

import rs.raf.infview.view.component.Label;

import javax.swing.*;

public class SwingLabel extends JLabel implements Label {

    @Override
    public void setPosition(Position position) {
        switch(position) {
            case TOP:
                setVerticalAlignment(SwingConstants.TOP);
                break;
            case BOTTOM:
                setVerticalAlignment(SwingConstants.BOTTOM);
                break;
            case LEFT:
                setHorizontalAlignment(SwingConstants.LEFT);
                break;
            case RIGHT:
                setHorizontalAlignment(SwingConstants.RIGHT);
                break;
            case CENTER:
                setVerticalAlignment(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.CENTER);
                break;

        }
    }
}