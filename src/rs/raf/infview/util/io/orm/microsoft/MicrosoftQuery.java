package rs.raf.infview.util.io.orm.microsoft;

import rs.raf.infview.util.io.orm.component.Query;

class MicrosoftQuery implements Query {

    StringBuilder builder;

    MicrosoftQuery() {
        this.builder = new StringBuilder();
    }

    @Override
    public String dump() {
        return builder.toString().trim().replaceAll(" +", " ");
    }

    @Override
    public void append(String value) {
        append(value, false);
    }

    @Override
    public void append(String value, boolean enclose) {
        builder.append(" ");
        builder.append(enclose ? "'" : "");
        builder.append(value);
        builder.append(enclose ? "'" : "");
        builder.append(" ");
    }

    @Override
    public void replace(String oldValue, String newValue) {
        String builder = this.builder.toString().replace(oldValue, " " + newValue + " ");

        this.builder.setLength(0);
        this.builder.append(builder);
    }

    @Override
    public void clear() {
        builder.setLength(0);
    }
}