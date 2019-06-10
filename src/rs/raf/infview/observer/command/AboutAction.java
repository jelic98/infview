package rs.raf.infview.observer.command;

import rs.raf.infview.view.frame.AboutFrame;
import rs.raf.infview.view.frame.FrameTemplate;

public class AboutAction extends Command {

    private FrameTemplate frame;

    public AboutAction() {
        frame = new AboutFrame();
    }

    @Override
    public void execute() {
        frame.open();
    }
}