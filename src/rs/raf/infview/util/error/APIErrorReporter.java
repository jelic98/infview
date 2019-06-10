package rs.raf.infview.util.error;

import rs.raf.infview.core.App;
import rs.raf.infview.util.api.RequestBuilder;
import java.io.IOException;

public class APIErrorReporter implements ErrorReporter {

    @Override
    public void report(String message) throws IOException {
        new RequestBuilder(App.REPORT_API_URL)
                .addHeader("API_KEY", App.REPORT_API_KEY)
                .addParameter("message", message)
                .build();
    }
}