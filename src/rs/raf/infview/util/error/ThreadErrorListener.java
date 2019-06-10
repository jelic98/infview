package rs.raf.infview.util.error;

import java.io.IOException;
import rs.raf.infview.core.App;
import rs.raf.infview.util.log.Log;

public class ThreadErrorListener implements ErrorListener {

    private final ErrorReporter reporter = new APIErrorReporter();

    @Override
    public void listen() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                String message = MessageExtractor.extract(throwable);

                try {
                    if(App.DEBUG) {
                        throw new IOException();
                    }

                    reporter.report(message);
                }catch(IOException e) {
                    Log.e(message);
                }
            }
        });
    }
}