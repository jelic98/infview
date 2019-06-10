package rs.raf.infview;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Parameters;
import rs.raf.infview.core.User;
import rs.raf.infview.observer.command.CommandQueue;
import rs.raf.infview.observer.command.ReadSettingsAction;
import rs.raf.infview.observer.command.ThreadOptions;
import rs.raf.infview.util.error.ThreadErrorListener;
import rs.raf.infview.util.checker.CheckerAdapter;
import rs.raf.infview.util.checker.LicenseChecker;
import rs.raf.infview.util.checker.SessionChecker;
import rs.raf.infview.view.frame.LoginFrame;
import rs.raf.infview.view.frame.MainFrame;

/**
 * @author Lazar Jelic - ljelic2718rn@raf.rs
 * @author Stefan Zivkovic - szivkovic17@raf.rs
 * @author Marko Stojanovic - mstojanovic17@raf.rs
 * <p>
 * Activation code: TESTCODE
 * Username: admin
 * Password: 123
 * </p>
 */
class InfView {

    public static void main(String[] args) {
        new InfView().start();
    }

	private void start() {
        new ThreadErrorListener().listen();

        CommandQueue.push(new ReadSettingsAction(), ThreadOptions.SINGLE_THREAD);

        if(App.DEBUG) {
            App.RUNTIME.set(Parameters.SESSION_USER, User.ADMINISTRATOR.name());
            new MainFrame().open();
            return;
        }else {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        new LicenseChecker(new CheckerAdapter() {
            @Override
            public void onSuccess() {
                new SessionChecker(new CheckerAdapter() {
                    @Override
                    public void onSuccess() {
                        new MainFrame().open();
                    }

                    @Override
                    public void onError(String message) {
                        new LoginFrame().open();
                    }
                }).check();
            }
        }).check();
    }
}