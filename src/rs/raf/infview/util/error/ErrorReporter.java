package rs.raf.infview.util.error;

import java.io.IOException;

interface ErrorReporter {

    void report(String message) throws IOException;
}