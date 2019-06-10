package rs.raf.infview.util.checker;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Parameters;
import rs.raf.infview.core.Res;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.adapter.dialog.Input;

public class LicenseChecker extends Checker implements CheckerListener {

    private final CheckerListener listener;
    private final LicenseActivator activator;

    public LicenseChecker(CheckerListener listener) {
        this.listener = listener;
        activator = new LicenseActivator(this);
    }

    @Override
    public void check() {
        if(alreadyActivated()) {
            onSuccess();
            return;
        }

        String code = DialogAdapter.input(new Input(Res.STRINGS.INPUT_LICENSE_TITLE, Res.STRINGS.INPUT_LICENSE_MESSAGE));

        if(code == null || code.isEmpty()) {
            System.exit(0);
        }

        activator.activate(code);
    }

    @Override
    public void onError(String message) {
        DialogAdapter.error(message);

        check();

        listener.onError(message);
    }

    @Override
    public void onSuccess() {
        App.RUNTIME.set(Parameters.ACTIVATED, "true");

        listener.onSuccess();
    }

    private boolean alreadyActivated() {
        return Boolean.parseBoolean(App.RUNTIME.get(Parameters.ACTIVATED));
    }
}