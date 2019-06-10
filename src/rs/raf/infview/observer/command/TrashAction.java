package rs.raf.infview.observer.command;

import rs.raf.infview.model.Entity;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.view.frame.TrashFrame;

public class TrashAction extends Command {

    private TrashFrame frame;

    public TrashAction(Entity entity, ChangeObserver<Entity> callback) {
        this.frame = new TrashFrame(entity, callback);
    }

    @Override
    public void execute() {
        frame.open();
    }
}