package rs.raf.infview.util.io.orm.component;

public interface Matcher {

    Matcher isEqual(String attr, Number number);
    Matcher isEqual(String attr, String value);
    Matcher isLess(String attr, Number number);
    Matcher isLessOrEqual(String attr, Number number);
    Matcher isGreater(String attr, Number number);
    Matcher isGreaterOrEqual(String attr, Number number);
    Matcher startsWith(String attr, String pattern);
    Matcher endsWith(String attr, String pattern);
    Matcher contains(String attr, String pattern);
    Matcher hasValue(String attr);
    Matcher inverse();
    Matcher and(Matcher matcher);
    Matcher or(Matcher matcher);
    String dump();
}