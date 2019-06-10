package rs.raf.infview.util;

public class Bundle {

    private Object value;

    public Bundle(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}