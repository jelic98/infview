package rs.raf.infview.model.options;

import rs.raf.infview.model.DeepCopy;
import rs.raf.infview.model.schema.SchemaOptions;
import rs.raf.infview.model.schema.SchemaSerializable;

@SchemaOptions
public interface ResourceOptions extends SchemaSerializable, DeepCopy<ResourceOptions> {

    String getExtension();
    void setExtension(String extension);
}