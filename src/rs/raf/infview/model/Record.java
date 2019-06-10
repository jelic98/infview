package rs.raf.infview.model;

import rs.raf.infview.model.validator.Validateable;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import rs.raf.infview.util.io.file.AbstractFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Record implements Validateable, Comparable<Record>, DeepCopy<Record> {

	public static final String PRIMARY_PARSER = ",";
	
    private Entity parent;
    private Set<Attribute> attributes;
    private Map<String, String> data;

    public Record() {
        this.data = new HashMap<>();
    }

    private Record(Record record) {
        this();

        copyData(record);
    }

    public Map<String, String> getPrimaries() {
        Map<String, String> copy = new HashMap<>(data.size());

        for(Attribute a : getAttributes()) {
            if(a.isPrimary()) {
                String key = a.getName();

                copy.put(key, data.get(key));
            }
        }

        return copy;
    }

    public Map<String, String> getData() {
        Map<String, String> copy = new HashMap<>(data.size());

        for(String key : data.keySet()) {
            copy.put(key, data.get(key));
        }

        return copy;
    }

    public void copyData(Record record) {
        for(String key : record.data.keySet()) {
            set(key, record.data.get(key));
        }
    }

    public String get(String key) {
        return data.get(key);
    }

    public void set(String key, String value) {
        data.put(key, value);
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public String serialize(Set<Attribute> attributes) {
        StringBuilder builder = new StringBuilder();

        for(Attribute a : attributes) {
            String value = get(a.getName());

            if(value != null) {
                builder.append(value);

                int diff = a.getLength() - value.length();

                for(int i = 0; i < diff; i++) {
                    builder.append(AbstractFile.SEPARATOR);
                }
            }
        }

        return builder.toString();
    }

    public Set<Attribute> getAttributes() {
        if(attributes == null) {
            return parent.getAttributes();
        }

        return attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Entity getParent() {
        return parent;
    }

    @Override
    public boolean hasValidateables() {
        return false;
    }

    @Override
    public Set<? extends Validateable> getValidateables() {
        return null;
    }

    @Override
    public void validate(Validator validator) throws ValidationException {
        validator.validate(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Record)) {
            return false;
        }

        Record record = (Record) obj;

        return compareTo(record) == 0;
    }

    @Override
    public int compareTo(Record o) {
        for(Attribute a : getAttributes()) {
            if(a.isPrimary()) {
                String value1 = data.get(a.getName());
                String value2 = o.data.get(a.getName());

                if(value1 == null || value2 == null) {
                    continue;
                }

                int valueCompare = a.getType().getComparator().on(a.getName()).compare(this, o);
                
                if(valueCompare != 0) {
                    return valueCompare;
                }
            }
        }

        return 0;
    }

    @Override
    public Record getClone() {
        return new Record(this);
    }
    
    public String getPrimary() {
    	StringBuilder builder = new StringBuilder();
    	
    	int i = 0;
    	
		for(String key : getPrimaries().keySet()){
			if(i++ > 0) {
				builder.append(PRIMARY_PARSER);	
			}
			builder.append(get(key));
		}
		return builder.toString();
    }
}