package rs.raf.infview.observer.command;

import rs.raf.infview.state.Context;
import rs.raf.infview.util.ObservableSet;
import rs.raf.infview.view.adapter.tab.TabItem;
import rs.raf.infview.view.component.Node;

public class CloseTabAction extends Command {

    private Node node;
    private ObservableSet<TabItem> tabs;

    public CloseTabAction(Node node, ObservableSet<TabItem> tabSet) {
        this.node = node;
        this.tabs = tabSet;
    }

    @Override
    public void execute() {
        if(node == null) {
            node = Context.instance().tree.getSelectedNode();
        }

        if(node == null) {
            return;
        }

        TabItem closingTab = null;

        for(TabItem tab : tabs) {
            if(tab.getModel() == node) {
               closingTab = tab;
               break;
            }
        }

        if(closingTab != null) {
            this.tabs.delete(closingTab);
        }

        node = null;
    }
}