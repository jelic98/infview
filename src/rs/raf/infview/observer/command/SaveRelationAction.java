package rs.raf.infview.observer.command;

import rs.raf.infview.model.Relation;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.frame.RelationFrame;

public class SaveRelationAction extends Command {

    private Relation relation;
    private RelationFrame frame;

    public SaveRelationAction(Relation relation, RelationFrame frame) {
        this.relation = relation;
        this.frame = frame;
    }

    @Override
    public void execute() {
        try {
            new Validator().validate(relation);
        }catch(ValidationException e) {
            DialogAdapter.error(e.getMessage());
            return;
        }

        frame.close();
    }
}