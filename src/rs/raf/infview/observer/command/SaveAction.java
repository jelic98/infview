package rs.raf.infview.observer.command;

import rs.raf.infview.core.*;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.Resource;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.file.AbstractFile;
import rs.raf.infview.util.io.file.FileOperator;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.adapter.dialog.Question;
import rs.raf.infview.view.component.Node;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class SaveAction extends Command {

    private String path;
    private Resource resource;
    private boolean skipDialog;

    public SaveAction() {
        this.path = Paths.getHomePath();
        this.skipDialog = false;

        setStatus(Res.STRINGS.STATUS_SAVE);
    }

    public SaveAction(Resource resource) {
        this.resource = resource;
    }

    public SaveAction(String path) {
        this.path = path;
    }

    @Override
    public void execute() {
        if(skipDialog) {
            saveNode();
            return;
        }

        DialogAdapter.question(new Question(Res.STRINGS.QUESTION_SAVE)
                .addOption(Res.STRINGS.OPTION_YES, new Command() {
                    @Override
                    public void execute() {
                        saveNode();
                    }
                })
                .addOption(Res.STRINGS.OPTION_NO));
    }

    private void saveNode() {
        Resource resource = this.resource;

        if(resource == null) {
            try {
                Node selected = Context.instance().tree.getSelectedNode();
                resource = (Resource) selected.getDelegateModel();
            }catch(Exception e) {
                DialogAdapter.error(Res.STRINGS.ERROR_CANNOT_WRITE);
                return;
            }
        }

        String resourcePath = path + resource.getName();

        String schemaPath = Paths.separate(resourcePath) + App.RESOURCE_PATH;

        CommandQueue.push(new SaveSchemaAction(resource, schemaPath), ThreadOptions.SINGLE_THREAD);
    }

    public SaveAction skipDialog() {
        this.skipDialog = true;

        return this;
    }
}