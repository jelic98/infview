package rs.raf.infview.observer.command;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Parameters;
import rs.raf.infview.state.Context;
import rs.raf.infview.view.frame.FrameTemplate;
import rs.raf.infview.view.frame.LoginFrame;

public class LogoutAction extends Command {

    private FrameTemplate frame;

    public LogoutAction(FrameTemplate frame) {
        this.frame = frame;
    }

    @Override
    public void execute() {
        App.RUNTIME.reset(Parameters.SESSION_HASH);
        App.RUNTIME.reset(Parameters.SESSION_USER);

        Context.instance().reset();

        new LoginFrame().open();

        frame.close();
    }
}