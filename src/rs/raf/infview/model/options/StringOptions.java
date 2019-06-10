package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaOptions;

@SchemaOptions
public class StringOptions implements AttributeOptions {

    @SchemaField
    private int minLength;

    @SchemaField
    private int maxLength;

    @SchemaField
    private String pattern;

    public StringOptions() {
        super();
    }

    private StringOptions(StringOptions options) {
        this.minLength = options.minLength;
        this.maxLength = options.maxLength;
        this.pattern = options.pattern;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = Integer.parseInt(minLength);
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = Integer.parseInt(maxLength);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public AttributeOptions getClone() {
        return new StringOptions(this);
    }

    @Override
    public String getUid() {
        return toString();
    }
}
