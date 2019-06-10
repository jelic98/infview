package rs.raf.infview.util.checker;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Parameters;
import rs.raf.infview.core.Res;
import rs.raf.infview.util.api.RequestBuilder;
import rs.raf.infview.util.api.ResponseListener;
import java.io.IOException;

public class SessionChecker extends Checker implements ResponseListener {

    private final CheckerListener listener;

    public SessionChecker(CheckerListener listener) {
        this.listener = listener;
    }

    @Override
    public void check() {
        String sessionHash = App.RUNTIME.get(Parameters.SESSION_HASH);

        try {
            new RequestBuilder(App.SESSION_URL)
                    .addParameter("hash", sessionHash)
                    .setListener(this)
                    .build();
        }catch(IOException e) {
            listener.onError(Res.STRINGS.ERROR_LOGIN_FAILED);
        }
    }

    @Override
    public void onResponse(int code, String content) {
        if(code == 200) {
            App.RUNTIME.set(Parameters.SESSION_HASH, content.substring(0, content.indexOf(' ')));
            App.RUNTIME.set(Parameters.SESSION_USER, content.substring(content.indexOf(' ') + 1));

            listener.onSuccess();
        }else {
            listener.onError(content);
        }
    }
}