package rs.raf.infview.util.log;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Paths;
import java.io.*;

public class FileLogger implements Logger {

    @Override
    public void log(String tag, String message) {
        String line = tag  + ": " +  message;

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(Paths.getLogPath(), true));
            writer.println(App.HASHER.hash(line));
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null) {
                writer.close();
            }
        }
    }
}