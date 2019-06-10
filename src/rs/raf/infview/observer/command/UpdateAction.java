package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.FileOperator;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.frame.RecordFrame;

public class UpdateAction extends Command {

    private Entity entity;
    private ChangeObserver<Entity> callback;

    public UpdateAction(Entity entity, ChangeObserver<Entity> callback) {
        this.entity = entity;
        this.callback = callback;
    }

    @Override
    public void execute() {
        Record selected = Context.instance().record;

        if(selected == null) {
            DialogAdapter.error(Res.STRINGS.ERROR_RECORD_NOT_SELECTED);
            return;
        }

        new RecordFrame(entity, selected.getClone(), new ChangeObserverAdapter<Record>() {
            @Override
            public void onChange(ChangeType type, Record bundle) {
                if(type != ChangeType.APPLY) {
                    return;
                }

                try {
                    new Validator().validate(bundle);
                }catch(ValidationException e) {
                    DialogAdapter.error(e.getMessage());
                    return;
                }

                AbstractResource file = Context.instance().fileMatcher.get(entity);

                if(file != null) {
                    FileOperator operator = new FileOperator(file);
                    operator.update(selected, bundle);
                }

                selected.copyData(bundle);

                entity.addRecord(bundle);

                callback.onChange(ChangeType.REFRESH, entity);
            }
        }).open();
    }
}