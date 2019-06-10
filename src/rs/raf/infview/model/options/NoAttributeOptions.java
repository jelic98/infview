package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaOptions;

@SchemaOptions
public class NoAttributeOptions implements AttributeOptions {

    @Override
    public AttributeOptions getClone() {
        return new NoAttributeOptions();
    }

    @Override
    public String getUid() {
        return toString();
    }
}