package rs.raf.infview.util.io.orm.microsoft;

import rs.raf.infview.util.io.orm.component.Matcher;

class MicrosoftMatcher implements Matcher {

    private String matcher;

    @Override
    public Matcher isEqual(String attr, Number number) {
        matcher = attr + " = " + number;

        return this;
    }

    @Override
    public Matcher isLess(String attr, Number number) {
        matcher = attr + " < " + number;

        return this;
    }

    @Override
    public Matcher isLessOrEqual(String attr, Number number) {
        matcher = attr + " <= " + number;

        return this;
    }

    @Override
    public Matcher isGreater(String attr, Number number) {
        matcher = attr + " > " + number;

        return this;
    }

    @Override
    public Matcher isGreaterOrEqual(String attr, Number number) {
        matcher = attr + " >= " + number;

        return this;
    }

    @Override
    public Matcher isEqual(String attr, String value) {
        matcher = attr + " = '" + value + "' ";

        return this;
    }

    @Override
    public Matcher startsWith(String attr, String pattern) {
        matcher = attr + " LIKE '" + pattern + "%' ";

        return this;
    }

    @Override
    public Matcher endsWith(String attr, String pattern) {
        matcher = attr + " LIKE '%" + pattern + "' ";

        return this;
    }

    @Override
    public Matcher contains(String attr, String pattern) {
        matcher = attr + " LIKE '%" + pattern + "%' ";

        return this;
    }

    @Override
    public Matcher hasValue(String attr) {
        matcher = attr + " IS NOT NULL ";

        return this;
    }

    @Override
    public Matcher inverse() {
        matcher = " NOT " + matcher;

        return this;
    }

    @Override
    public Matcher and(Matcher matcher) {
        this.matcher = " ( " + this.matcher + " AND " + matcher.dump() + " ) ";

        return this;
    }

    @Override
    public Matcher or(Matcher matcher) {
        this.matcher = " ( " + this.matcher + " OR " + matcher.dump() + " ) ";

        return this;
    }

    @Override
    public String dump() {
        return matcher;
    }
}