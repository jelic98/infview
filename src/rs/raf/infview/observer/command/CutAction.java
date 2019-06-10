package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.state.Context;
import rs.raf.infview.view.component.Node;
import java.util.*;

public class CutAction extends UndoableCommand {

    private List<Node> currentNodes;
    private List<List<Node>> cutNodes;

    public CutAction() {
        this(new LinkedList<>());
    }

    CutAction(List<Node> cutNodes) {
        this.cutNodes = new LinkedList<>();
        this.cutNodes.add(0, cutNodes);

        setStatus(Res.STRINGS.STATUS_CUT);
    }

    @Override
    public void execute() {
        Context.instance().tree.clearClipboard();

        currentNodes = new LinkedList<>();

        List<Node> children;

        if(getState() == ExecutionState.REDO) {
            children = cutNodes.get(0);
            cutNodes.remove(0);
        }else {
            children = Context.instance().tree.getSelectedNodes();
        }

        for(Node child : children) {
            handle(child);
        }

        if(!currentNodes.isEmpty()) {
            cutNodes.add(0, currentNodes);
        }

        Context.instance().tree.reload();
    }

    private void handle(Node selected) {
        Context.instance().tree.addClipboardNode(selected);

        selected.removeFromParent();

        currentNodes.add(selected);
    }

    @Override
    public void revert() {
        CommandQueue.push(new PasteAction(cutNodes.get(0))
                .setState(ExecutionState.REDO)
                .skipHistory());
    }

    @Override
    public UndoableCommand getClone() {
        return (UndoableCommand) new CutAction(cutNodes.get(0))
                .setState(getState())
                .skipHistory(isSkipHistory());
    }
}