package rs.raf.infview.observer.command;

import java.awt.*;
import java.net.URI;
import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;

public class HelpOnlineAction extends Command {

    @Override
    public void execute() {
        if(Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(App.HELP_URL));
            }catch(Exception e) {
                DialogAdapter.info(Res.STRINGS.INFO_ONLINE_HELP + App.HELP_URL);
            }
        }else {
            DialogAdapter.info(Res.STRINGS.INFO_ONLINE_HELP + App.HELP_URL);
        }
    }
}