package rs.raf.infview.model.type;

import rs.raf.infview.model.options.FileOptions;
import rs.raf.infview.model.options.DatabaseOptions;
import rs.raf.infview.model.options.ResourceOptions;
import rs.raf.infview.model.schema.SchemaType;
import rs.raf.infview.util.Reflection;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.SerialFile;
import rs.raf.infview.util.io.file.SequentialFile;
import rs.raf.infview.util.io.file.IndexFile;
import rs.raf.infview.util.io.file.RelationalDatabase;

@SchemaType
public enum ResourceType {

    SERIAL_FILE(FileOptions.class, SerialFile.class),
    SEQUENTIAL_FILE(FileOptions.class, SequentialFile.class),
    INDEX_FILE(FileOptions.class, IndexFile.class),
	RELATIONAL_DATABASE(DatabaseOptions.class, RelationalDatabase.class);

    private static final Reflection reflection = new Reflection();

    private Class options, file;
    
    ResourceType(Class options, Class file) {
		this.options = options;
		this.file = file;
	}
    
    ResourceType(Class options) {
        this.options = options;
    }

    public ResourceOptions getOptions() {
        return (ResourceOptions) reflection.instantiate(options);
    }
    
    public AbstractResource getResource() {
        return (AbstractResource) reflection.instantiate(file);
    }
}