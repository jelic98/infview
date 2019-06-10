package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaField;

public class DateTimeOptions implements AttributeOptions{
	
	 	@SchemaField
	    private String minDateTime;

	    @SchemaField
	    private String maxDateTime;

	    @SchemaField
	    private String format;

	    public DateTimeOptions() {
	        super();
	    }

	    private DateTimeOptions(DateTimeOptions options) {
	        this.minDateTime = options.minDateTime;
	        this.maxDateTime = options.maxDateTime;
	        this.format = options.format;
	    }

	    public String getMinDateTime() {
	        return minDateTime;
	    }

	    public void setMinDateTime(String minDateTime) {
	        this.minDateTime = minDateTime;
	    }

	    public String getMaxDateTime() {
	        return maxDateTime;
	    }

	    public void setMaxDateTime(String maxDateTime) {
	        this.maxDateTime = maxDateTime;
	    }

	    public String getFormat(){
	        return format;
	    }

	    public void setFormat(String format) {
	        this.format = format;
	    }

	    @Override
	    public AttributeOptions getClone() {
	        return new DateTimeOptions(this);
	    }

	    @Override
	    public String getUid() {
	        return toString();
	    }
}
