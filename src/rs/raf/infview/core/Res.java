package rs.raf.infview.core;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Res {

    public static final Strings STRINGS = new StringsEN();
    public static final Icons ICONS = new Icons();
    public static final Schema SCHEMA = new Schema();
    public static final Formats FORMATS = new Formats();

    static byte[] getFromPath(String path, Class<?> clazz) {
        byte[] res;

        try {
            InputStream is = clazz.getResourceAsStream(path);
            res = new byte[is.available()];
            new DataInputStream(is).readFully(res);
        }catch(IOException e) {
            res = new byte[0];
        }

        return res;
    }
}