package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.Entity;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.file.AbstractFile;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.FileOperator;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;

public class ConvertAction extends Command {

    private Entity entity;

    public ConvertAction(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void execute() {
        AbstractResource resource = Context.instance().fileMatcher.get(entity);

        if(resource instanceof AbstractFile) {
            AbstractFile file = (AbstractFile) resource;

            if(file == null) {
                DialogAdapter.error(Res.STRINGS.ERROR_ENTITY_NOT_SERIALIZED);
                return;
            }

            FileOperator operator = new FileOperator(file);

            DialogAdapter.info(Res.STRINGS.INFO_FILE_CONVERTED + ((AbstractFile) operator.convert()).getPath());
        }
    }
}