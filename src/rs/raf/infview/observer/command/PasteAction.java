package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.state.Context;
import rs.raf.infview.model.*;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.component.Node;
import java.util.*;

public class PasteAction extends UndoableCommand {

    private List<Node> currentNodes;
    private List<List<Node>> pastedNodes;

    public PasteAction() {
        this(new LinkedList<>());
    }

    PasteAction(List<Node> pastedNodes) {
        this.pastedNodes = new LinkedList<>();
        this.pastedNodes.add(0, pastedNodes);

        setStatus(Res.STRINGS.STATUS_PASTE);
    }

    @Override
    public void execute() {
        currentNodes = new LinkedList<>();

        List<Node> children;

        AbstractModel parent = null;

        if(getState() == ExecutionState.REDO) {
            children = pastedNodes.get(0);
            pastedNodes.remove(0);
        }else {
            Node selectedNode = Context.instance().tree.getSelectedNode();

            if(selectedNode != null) {
                parent = (AbstractModel) selectedNode.getDelegateModel();
            }

            children = Context.instance().tree.getClipboardNodes();
        }

        for(Node child : children) {
            if(parent != null && parent.getLevel() != ((AbstractModel) child.getDelegateModel()).getLevel() - 1) {
                DialogAdapter.error(Res.STRINGS.ERROR_NOT_DIRECT_PARENT);
                continue;
            }

            handle(parent == null ? child.getAncestor() : parent, child);
        }

        if(!currentNodes.isEmpty()) {
            pastedNodes.add(0, currentNodes);
        }

        Context.instance().tree.reload();
    }

    private void handle(Node parent, Node child) {
        Node clone = ((AbstractModel) child.getDelegateModel()).getClone();

        copyChildren(child, clone);

        parent.addChild(clone);

        currentNodes.add(clone);
    }

    private void copyChildren(Node src, Node dest) {
        for(Node child : src.getChildren()) {
            Node model = child.getDelegateModel();

            if(!(model instanceof AbstractModel)) {
                continue;
            }

            Node clone = ((AbstractModel) model).getClone();

            dest.addChild(clone);

            copyChildren(child, clone);
        }
    }

    @Override
    public void revert() {
        CommandQueue.push(new CutAction(pastedNodes.get(0))
                .setState(ExecutionState.REDO)
                .skipHistory());
    }

    @Override
    public UndoableCommand getClone() {
        return (UndoableCommand) new PasteAction(pastedNodes.get(0))
                .setState(getState())
                .skipHistory(isSkipHistory());
    }
}