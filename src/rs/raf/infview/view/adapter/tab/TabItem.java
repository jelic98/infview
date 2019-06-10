package rs.raf.infview.view.adapter.tab;

import rs.raf.infview.model.AbstractModel;
import rs.raf.infview.view.adapter.action.BarItem;
import rs.raf.infview.view.component.Component;

public class TabItem extends BarItem implements Comparable<TabItem> {

    private final AbstractModel model;
    private final TabComponent tab;

    public TabItem(AbstractModel model, TabComponent tab) {
        super(model.getNodeName(), model.getIcon());
        this.model = model;
        this.tab = tab;
    }

    public AbstractModel getModel() {
        return model;
    }

    public TabComponent getTab() {
        return tab;
    }

    public Component getComponent() {
        return tab.getComponent();
    }

    @Override
    public int compareTo(TabItem obj) {
        return getModel().compareTo(obj.getModel());
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TabItem)) {
            return false;
        }

        TabItem item = (TabItem) obj;

        return getModel().equals(item.getModel());
    }

    public void refresh() {
        tab.refresh();
    }
}