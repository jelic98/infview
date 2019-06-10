package rs.raf.infview.observer.command;

import java.io.FileWriter;
import java.io.PrintWriter;
import rs.raf.infview.core.App;
import rs.raf.infview.core.Paths;
import rs.raf.infview.core.Res;
import rs.raf.infview.util.log.Log;

public class WriteSettingsAction extends Command {

    public WriteSettingsAction() {
        setStatus(Res.STRINGS.STATUS_WRITE_SETTINGS);
    }

    @Override
    public void execute() {
        try(PrintWriter writer = new PrintWriter(new FileWriter(Paths.getConfigPath(), false))) {
            writer.write(App.HASHER.hash(App.RUNTIME.write(new StringBuilder())));
        }catch(Exception e) {
            Log.e(e.getMessage());
        }
    }
}