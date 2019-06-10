package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaOptions;

@SchemaOptions
public class DatabaseOptions implements ResourceOptions {

    @SchemaField
    private String host;

    @SchemaField
    private String database;
	
    @SchemaField
    private String username;

    @SchemaField
    private String password;

    @SchemaField
    private String extension;

    public DatabaseOptions() {
        super();
    }

    private DatabaseOptions(DatabaseOptions options) {
    	this.host = options.host;
        this.database = options.database;
        this.username = options.username;
        this.password = options.password;
        this.extension = options.extension;
    }
    
    public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return new DatabaseOptions(this);
    }

    @Override
    public String getUid() {
        return toString();
    }
}