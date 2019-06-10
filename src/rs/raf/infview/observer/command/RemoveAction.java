package rs.raf.infview.observer.command;

import rs.raf.infview.core.*;
import rs.raf.infview.model.AbstractModel;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.ObservableSet;
import rs.raf.infview.view.adapter.tab.TabItem;
import rs.raf.infview.view.component.Node;

public class RemoveAction extends UndoableCommand {

    private ObservableSet<TabItem> tabs;
    private AbstractModel node;

    public RemoveAction(ObservableSet<TabItem> tabs) {
        this.tabs = tabs;

        setStatus(Res.STRINGS.STATUS_REMOVE);
    }

    public RemoveAction(AbstractModel node) {
        this.node = node;

        setStatus(Res.STRINGS.STATUS_REMOVE);
    }

    @Override
    public void execute() {
        if(node == null) {
            Node selectedNode = Context.instance().tree.getSelectedNode();

            if(selectedNode != null) {
                node = (AbstractModel) selectedNode.getDelegateModel();
            }
        }

        if(node == null) {
            return;
        }

        removeNode();
    }

    @Override
    public void revert() {
        CommandQueue.push(new AddAction(node)
                .setState(ExecutionState.REDO)
                .skipHistory());
    }

    @Override
    public UndoableCommand getClone() {
        RemoveAction clone = new RemoveAction(tabs);
        clone.node = node;
        clone.setState(getState());
        clone.skipHistory(isSkipHistory());

        return clone;
    }

    private void removeNode() {
        closeTab(node);
        removeFromTree(node);
    }

    private void closeTab(AbstractModel node) {
        if(tabs == null) {
            return;
        }

        CommandQueue.push(new CloseTabAction(node, tabs).skipHistory(isSkipHistory()));

        for(Node child : node.getChildren()) {
            Node model = child.getDelegateModel();

            if(model instanceof AbstractModel) {
                closeTab((AbstractModel) model);
            }
        }
    }

    private void removeFromTree(AbstractModel node) {
        if(node.getAncestor() != null) {
            node.removeFromParent();
        }else if(node == Context.instance().tree.getRoot()) {
            Context.instance().tree.removeChildren();
        }

        Context.instance().tree.reload();
    }
}