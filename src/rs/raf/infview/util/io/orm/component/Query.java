package rs.raf.infview.util.io.orm.component;

public interface Query {

    String dump();
    void append(String value);
    void append(String value, boolean enclose);
    void replace(String oldValue, String newValue);
    void clear();
}