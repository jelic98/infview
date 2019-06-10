package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaOptions;

@SchemaOptions
public class FileOptions implements ResourceOptions {

    @SchemaField
    private String path;

    @SchemaField
    private String extension;

    public FileOptions() {
        super();
    }

    public FileOptions(String path) {
		this.path = path;
	}

	private FileOptions(FileOptions options) {
        this.extension = options.extension;
        this.path = options.path;
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public ResourceOptions getClone() {
        return new FileOptions(this);
    }

    @Override
    public String getUid() {
        return toString();
    }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}