package rs.raf.infview.model.options;

import rs.raf.infview.model.schema.SchemaField;

public class DateOptions implements AttributeOptions{

	 @SchemaField
	    private String minDate;

	    @SchemaField
	    private String maxDate;

	    @SchemaField
	    private String format;

	    public DateOptions() {
	        super();
	    }

	    private DateOptions(DateOptions options) {
	        this.minDate = options.minDate;
	        this.maxDate = options.maxDate;
	        this.format = options.format;
	    }

	    public String getMinDate() {
	        return minDate;
	    }

	    public void setMinDate(String minDate) {
	        this.minDate = minDate;
	    }

	    public String getMaxDate() {
	        return maxDate;
	    }

	    public void setMaxDate(String maxDate) {
	        this.maxDate = maxDate;
	    }

	    public String getFormat() {
	        return format;
	    }

	    public void setFormat(String format) {
	        this.format = format;
	    }

	    @Override
	    public AttributeOptions getClone() {
	        return new DateOptions(this);
	    }

	    @Override
	    public String getUid() {
	        return toString();
	    }
}
