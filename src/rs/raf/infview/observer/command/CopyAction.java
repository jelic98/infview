package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.state.Context;
import rs.raf.infview.view.component.Node;

public class CopyAction extends Command {

    public CopyAction() {
        setStatus(Res.STRINGS.STATUS_COPY);
    }

    @Override
    public void execute() {
        Context.instance().tree.clearClipboard();

        for(Node selected : Context.instance().tree.getSelectedNodes()) {
            Context.instance().tree.addClipboardNode(selected);
        }
    }
}