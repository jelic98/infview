package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaOptions;

@SchemaOptions
public class NumberOptions implements AttributeOptions {

    @SchemaField
    private int minInt;

    @SchemaField
    private int maxInt;

    public NumberOptions() {
        super();
    }

    private NumberOptions(NumberOptions options) {
        this.minInt = options.minInt;
        this.maxInt = options.maxInt;
    }

    public int getMinInt() {
        return minInt;
    }

    public void setMinInt(String maximum) {
        this.minInt = Integer.parseInt(maximum);
    }

    public int getMaxInt() {
        return maxInt;
    }

    public void setMaxInt(String maximum) {
        this.maxInt = Integer.parseInt(maximum);
    }

    @Override
    public AttributeOptions getClone() {
        return new NumberOptions(this);
    }

    @Override
    public String getUid() {
        return toString();
    }
}
