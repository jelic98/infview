package rs.raf.infview.observer.command;

import rs.raf.infview.view.frame.FrameTemplate;
import rs.raf.infview.view.frame.SettingsFrame;

public class SettingsAction extends Command {

    private FrameTemplate frame;

    public SettingsAction() {
        frame = new SettingsFrame();
    }

    @Override
    public void execute() {
        frame.open();
    }
}