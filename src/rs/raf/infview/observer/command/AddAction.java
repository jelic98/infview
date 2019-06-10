package rs.raf.infview.observer.command;

import rs.raf.infview.core.*;
import rs.raf.infview.model.*;
import rs.raf.infview.state.Context;
import rs.raf.infview.view.component.Node;

public class AddAction extends UndoableCommand {

    private AbstractModel parent, child;
    private boolean firstLevel;

    public AddAction(AbstractModel parent) {
        this(parent, true);
    }

    public AddAction(AbstractModel parent, boolean firstLevel) {
        this.parent = parent;
        this.firstLevel = firstLevel;

        setStatus(Res.STRINGS.STATUS_ADD);
    }

    @Override
    public void execute() {
        if(parent == null || !firstLevel) {
            Node selectedNode = Context.instance().tree.getSelectedNode();

            if(selectedNode != null) {
                parent = (AbstractModel) selectedNode.getDelegateModel();
            }
        }

        if(parent == null) {
            return;
        }

        child = parent.getChild(parent.getChildType());

        addNode();
    }

    private void addNode() {
        if(child == null) {
            return;
        }

        parent.addChild(child);

        Context.instance().tree.reload();
    }

    @Override
    public void revert() {
        CommandQueue.push(new RemoveAction(child)
                .setState(ExecutionState.REDO)
                .skipHistory());
    }

    @Override
    public UndoableCommand getClone() {
        AddAction clone = new AddAction(parent, firstLevel);
        clone.child = child;
        clone.setState(getState());
        clone.skipHistory(isSkipHistory());

        return clone;
    }
}