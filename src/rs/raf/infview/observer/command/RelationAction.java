package rs.raf.infview.observer.command;

import rs.raf.infview.model.Relation;
import rs.raf.infview.model.Resource;
import rs.raf.infview.view.frame.FrameTemplate;
import rs.raf.infview.view.frame.RelationFrame;

public class RelationAction extends Command {

    private FrameTemplate frame;

    public RelationAction(Resource resource, Relation relation) {
        frame = new RelationFrame(resource, relation);
    }

    @Override
    public void execute() {
        frame.open();
    }
}