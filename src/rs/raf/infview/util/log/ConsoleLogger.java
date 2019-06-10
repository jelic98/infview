package rs.raf.infview.util.log;

public class ConsoleLogger implements Logger {

    @Override
    public void log(String tag, String message) {
        System.out.println(tag  + ": " +  message);
    }
}