package rs.raf.infview.util;

import org.json.JSONObject;
import org.json.JSONTokener;
import rs.raf.infview.core.Res;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class JsonParser {

    public JSONObject parse(byte[] bytes) throws Exception {
        File temp = createTempFile(bytes);

        if(temp == null) {
            return null;
        }

        return parse(temp);
    }

    public JSONObject parse(File file) throws Exception {
        FileReader reader = new FileReader(file);
        JSONTokener tokener = new JSONTokener(reader);
        JSONObject object = new JSONObject(tokener);

        return object;
    }

    private File createTempFile(byte[] bytes) {
        try {
            File file = File.createTempFile(Res.STRINGS.APP_NAME, null, null);
            file.deleteOnExit();

            new FileOutputStream(file).write(bytes);

            return file;
        }catch(IOException e) {
            return null;
        }
    }
}
