package rs.raf.infview.core;

public enum Parameters {

    ACTIVATED("false", false),
    SESSION_HASH("default", false),
    SESSION_USER("default", false),
    HOME_PATH(App.DEFAULT_HOME_PATH, true),
    LANGUAGE(App.DEFAULT_LANGUAGE, true);

    private final String defaultValue;
    private final boolean editable;

    Parameters(String defaultValue, boolean editable) {
        this.defaultValue = defaultValue;
        this.editable = editable;
    }

    public String defaultValue() {
        return defaultValue;
    }

    public boolean isEditable() {
        return editable;
    }
}