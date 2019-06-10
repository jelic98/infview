package rs.raf.infview.observer.command;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Paths;
import rs.raf.infview.core.Res;
import rs.raf.infview.view.component.FileChooser;
import java.io.File;

public class SaveAsAction extends Command {

    @Override
    public void execute() {
        File selected = App.UI.getFileChooser()
                .setStartPath(Paths.getHomePath())
                .setPathType(FileChooser.PathType.FILES_AND_DIRECTORIES)
                .setExtensions(Res.STRINGS.INFO_APP_FILES, Paths.getExtensions())
                .getSingle();

        if(selected != null) {
            CommandQueue.push(new SaveAction(selected.getPath()));
        }
    }
}