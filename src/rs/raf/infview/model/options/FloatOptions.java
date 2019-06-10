package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaField;

public class FloatOptions implements AttributeOptions{

	@SchemaField
    private float minFloat;

    @SchemaField
    private float maxFloat;

    public FloatOptions() {
        super();
    }

    private FloatOptions(FloatOptions options) {
        this.minFloat = options.minFloat;
        this.maxFloat = options.maxFloat;
    }

    public float getMinFloat() {
        return minFloat;
    }

    public void setMinFloat(String minFloat) {
        this.minFloat = Float.parseFloat(minFloat);
    }

    public float getMaxFloat() {
        return maxFloat;
    }

    public void setMaxFloat(String maxFloat) {
        this.maxFloat = Float.parseFloat(maxFloat);
    }

    @Override
    public AttributeOptions getClone() {
        return new FloatOptions(this);
    }

    @Override
    public String getUid() {
        return toString();
    }
}
