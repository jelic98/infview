package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.view.frame.FrameTemplate;

public class DiscardSettingsAction extends Command {

    private final FrameTemplate frame;

    public DiscardSettingsAction(FrameTemplate frame) {
        this.frame = frame;

        setStatus(Res.STRINGS.STATUS_DISCARD_SETTINGS);
    }

    @Override
    public void execute() {
        if(frame != null) {
            frame.close();
        }
    }
}