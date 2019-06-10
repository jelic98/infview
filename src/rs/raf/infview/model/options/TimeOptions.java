package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaField;

public class TimeOptions implements AttributeOptions{

	 @SchemaField
	    private String minTime;

	    @SchemaField
	    private String maxTime;

	    @SchemaField
	    private String format;

	    public TimeOptions() {
	        super();
	    }

	    private TimeOptions(TimeOptions options) {
	        this.minTime = options.minTime;
	        this.maxTime = options.maxTime;
	        this.format = options.format;
	    }

	    public String getMinTime() {
	        return minTime;
	    }

	    public void setMinTime(String minTime) {
	        this.minTime = minTime;
	    }

	    public String getMaxTime() {
	        return maxTime;
	    }

	    public void setMaxTime(String maxTime) {
	        this.maxTime = maxTime;
	    }

	    public String getFormat(){
	        return format;
	    }

	    public void setFormat(String format) {
	        this.format = format;
	    }

	    @Override
	    public AttributeOptions getClone() {
	        return new TimeOptions(this);
	    }

	    @Override
	    public String getUid() {
	        return toString();
	    }
}
