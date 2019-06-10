package rs.raf.infview.observer.command;

import rs.raf.infview.view.adapter.dialog.DialogAdapter;

public class HelpOfflineAction extends Command {

    @Override
    public void execute() {
        DialogAdapter.info("Offline help should show up");
    }
}