package rs.raf.infview.core;

import rs.raf.infview.util.Machine;
import rs.raf.infview.util.log.Log;

import java.io.File;
import java.io.IOException;

public class Paths {

    public static String getConfigPath() {
        return getPath(App.CONFIG_PATH);
    }

    public static String getLogPath() {
        return getPath(App.LOG_PATH);
    }

    public static String getResourcePath() {
        return getPath(App.RESOURCE_PATH);
    }

    public static String getHomePath() {
        String path = App.RUNTIME.get(Parameters.HOME_PATH);
        String separator = Machine.fileSeparator();

        if(!path.substring(path.length() - 1).equals(separator)) {
            path += separator;
        }

        return path;
    }

    public static void initialize() throws IOException {
        create(getConfigPath());
        create(getLogPath());
        create(getResourcePath());
    }

    public static File create(String path) throws IOException {
        File file = new File(path);

        if(!file.exists()) {
            if(file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            file.createNewFile();
        }

        return file;
    }

    public static String separate(String path) {
        String separator = Machine.fileSeparator();

        if(!path.substring(path.length() - 1).equals(separator)) {
            path += separator;
        }

        return path;
    }

    public static String[] getExtensions() {
        String[] extensions = new String[App.Extension.values().length];

        for(int i = 0; i < App.Extension.values().length; i++) {
            extensions[i] = App.Extension.values()[i].getSuffix();
        }

        return extensions;
    }

    private static String getPath(String path) {
        try {
            create(path);
        }catch(IOException e) {
            Log.e(e.getMessage());
        }

        return getHomePath() + path;
    }
}