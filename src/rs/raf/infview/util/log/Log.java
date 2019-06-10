package rs.raf.infview.util.log;

import rs.raf.infview.core.App;

public class Log {

    private static final Logger[] loggers = new Logger[] {
            new ConsoleLogger(),
            new FileLogger()
    };

    public static void e(String message) {
        log("ERROR", message);
    }

    public static void i(String message) {
        log("INFO", message);
    }

    public static void d(String message) {
        if(App.DEBUG) {
            log("DEBUG", message);
        }
    }

    private static void log(String tag, String message) {
        for(Logger logger : loggers) {
            logger.log(tag, message);
        }
    }
}