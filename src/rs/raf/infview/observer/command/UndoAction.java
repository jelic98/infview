package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;

public class UndoAction extends Command {

    public UndoAction() {
        setStatus(Res.STRINGS.STATUS_UNDO);
    }

    @Override
    public void execute() {
        CommandQueue.undo();
    }
}