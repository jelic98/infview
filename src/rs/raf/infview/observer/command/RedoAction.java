package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;

public class RedoAction extends Command {

    public RedoAction() {
        setStatus(Res.STRINGS.STATUS_REDO);
    }

    @Override
    public void execute() {
        CommandQueue.redo();
    }
}