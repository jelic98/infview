package rs.raf.infview.observer.command;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import rs.raf.infview.core.App;
import rs.raf.infview.core.Paths;
import rs.raf.infview.util.log.Log;

public class ReadSettingsAction extends Command {

    @Override
    public void execute() {
		try(Scanner s = new Scanner(new File(Paths.getConfigPath()))) {

			while(s.hasNextLine()) {
				App.RUNTIME.read(App.HASHER.dehash(s.nextLine()));
			}
		}catch(IOException e) {
			Log.e(e.getMessage());
		}
    }
}