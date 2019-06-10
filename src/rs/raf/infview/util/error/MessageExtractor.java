package rs.raf.infview.util.error;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class MessageExtractor {

    static String extract(Throwable t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(out));
        return new String(out.toByteArray());
    }
}