package rs.raf.infview.observer.command;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Parameters;
import rs.raf.infview.view.frame.FrameTemplate;
import java.util.Map;
import java.util.Set;

public class ApplySettingsAction extends Command {

    private final Set<String> keys;
    private final Map<String, String> draft;
    private final FrameTemplate frame;

    public ApplySettingsAction(Set<String> keys, Map<String, String> draft) {
        this(keys, draft, null);
    }

    public ApplySettingsAction(Set<String> keys, Map<String, String> draft, FrameTemplate frame) {
        this.keys = keys;
        this.draft = draft;
        this.frame = frame;
    }

    @Override
    public void execute() {
        applySettings();
    }

    private void applySettings() {
        for(String key : keys) {
            Parameters p;

            try {
                p = Parameters.valueOf(key);
            }catch(IllegalArgumentException e) {
                continue;
            }

            App.RUNTIME.set(p, draft.get(key));
        }

        if(frame != null) {
            frame.close();
        }
    }
}