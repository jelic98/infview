package rs.raf.infview.view.adapter.tab;

import rs.raf.infview.view.component.Component;

public abstract class TabComponent {

    Component component;

    public Component getComponent() {
        return component;
    }

    public abstract void refresh();
}