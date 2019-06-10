package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.state.Context;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;

public class DeleteAction extends Command {

    private Entity entity;
    private ChangeObserver<Entity> callback;

    public DeleteAction(Entity entity, ChangeObserver<Entity> callback) {
        this.entity = entity;
        this.callback = callback;
    }

    @Override
    public void execute() {
        Record record = Context.instance().record;

        if(record == null) {
            DialogAdapter.error(Res.STRINGS.ERROR_RECORD_NOT_SELECTED);
            return;
        }

        entity.removeRecord(record, false);

        callback.onChange(ChangeType.REFRESH, entity);
    }
}