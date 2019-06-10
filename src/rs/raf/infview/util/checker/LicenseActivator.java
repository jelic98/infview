package rs.raf.infview.util.checker;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.util.api.RequestBuilder;
import rs.raf.infview.util.api.ResponseListener;
import java.io.IOException;

public class LicenseActivator implements ResponseListener {

    private final CheckerListener listener;

    LicenseActivator(CheckerListener listener) {
        this.listener = listener;
    }

    void activate(String code) {
        try {
            new RequestBuilder(App.ACTIVATION_URL)
                    .addParameter("code", code)
                    .setListener(this)
                    .build();
        }catch(IOException e) {
            listener.onError(Res.STRINGS.ERROR_ACTIVATION_FAILED);
        }
    }

    @Override
    public void onResponse(int code, String content) {
        if(code == 200) {
            listener.onSuccess();
        }else {
            listener.onError(content);
        }
    }
}