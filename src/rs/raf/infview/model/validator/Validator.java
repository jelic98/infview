package rs.raf.infview.model.validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.json.JSONObject;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.*;
import rs.raf.infview.model.options.*;
import rs.raf.infview.model.schema.SchemaUtils;
import rs.raf.infview.model.type.AttributeType;

public class Validator {

	private JSONObject schema;
	private SchemaUtils utils;

	public Validator() throws ValidationException {
		utils = new SchemaUtils();
		schema = utils.loadSchema();
	}
	

	public void validate(Resource resource) throws ValidationException {
		JSONObject resourceSchema = utils.getObjectSchema(schema, "resource");
		JSONObject optionsSchema = utils.getObjectSchema(resourceSchema, "options");

		ResourceOptions options = resource.getOptions();

		if(resource.getName().isEmpty() || resource.getType() == null)
			throw new ValidationException(Res.STRINGS.VALIDATOR_NO_NAME);
		
		if(options instanceof DatabaseOptions) {
			DatabaseOptions databaseOptions = (DatabaseOptions) options;
			String userType = utils.getFieldSchema(optionsSchema, "username");
			String passType = utils.getFieldSchema(optionsSchema, "password");
			
			if(!(databaseOptions.getUsername().isEmpty() && databaseOptions.getPassword().isEmpty()))
				throw new ValidationException(Res.STRINGS.VALIDATOR_CANNOT_BE_EMPTY);
			
		}else if(options instanceof FileOptions) {
			if(resource.getOptions().getExtension() != null && !resource.getOptions().getExtension().isEmpty()) {
				StringBuilder extension = new StringBuilder(".");
				for (int i = 0; i < resource.getOptions().getExtension().length(); i++) {
					char c = resource.getOptions().getExtension().charAt(i);
					if(c != '.')
						extension.append(c);
				}
				resource.getOptions().setExtension(extension.toString());
			}
		}
		
		validateChildren(resource);
	}

	public void validate(Entity entity) throws ValidationException {
		if(entity.getName().isEmpty())
			throw new ValidationException(Res.STRINGS.VALIDATOR_NO_NAME);

		validateChildren(entity);
		validateRecords(entity);
	}
	
	public void validate(Attribute attribute) throws ValidationException {
		AttributeOptions options = attribute.getOptions();
		String defaultValue = attribute.getDefaultValue();
		
		validateOptions(options, attribute.getLength());
		validateAttributeValue(options, attribute.getDefaultValue(), attribute.getLength());
		
		if(attribute.getName() == null || attribute.getName().isEmpty())
			throw new ValidationException(Res.STRINGS.VALIDATOR_NO_NAME);
		
		if(attribute.getType() == AttributeType.BOOLEAN) {
			if(!(defaultValue.equalsIgnoreCase("true") || defaultValue.equalsIgnoreCase("false")))
				throw new ValidationException(Res.STRINGS.VALIDATOR_DEFAULT_VALUE_NOT_BOOLEAN);
		}
		
		if(attribute.isPrimary())
			attribute.setRequired(true);
		
		validateChildren(attribute);
	}

	private void validateChildren(Validateable parent) throws ValidationException {
		if(!parent.hasValidateables()) {
			return;
		}
		
		boolean primary = false;
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> ids = new ArrayList<>();

		for(Validateable child : parent.getValidateables()) {
			if(parent instanceof Root) {
				if(names.contains(((Resource) child).getName()))
					throw new ValidationException(Res.STRINGS.VALIDATOR_RESOURCE_SAME_NAME);
				else if(ids.contains(((Resource) child).getId()))
					throw new ValidationException(Res.STRINGS.VALIDATOR_RESOURCE_SAME_ID);

				names.add(((Resource) child).getName());
				ids.add(((Resource) child).getName());
			}else if(parent instanceof Resource) {
				if(names.contains(((Entity) child).getName()))
					throw new ValidationException(Res.STRINGS.VALIDATOR_RESOURCE_SAME_NAME);
				else if(ids.contains(((Entity) child).getId()))
					throw new ValidationException(Res.STRINGS.VALIDATOR_RESOURCE_SAME_ID);

				names.add(((Entity) child).getName());
				ids.add(((Entity) child).getName());
			}else if(parent instanceof Entity) {
				if(names.contains(((Attribute) child).getName()))
					throw new ValidationException(Res.STRINGS.VALIDATOR_ATTRIBUTES_SAME_NAME);
				else if(ids.contains(((Attribute) child).getId()))
					throw new ValidationException(Res.STRINGS.VALIDATOR_ATTRIBUTES_SAME_ID);

				names.add(((Attribute) child).getName());
				ids.add(((Attribute) child).getName());

				if(!primary) {
					primary = ((Attribute) child).isPrimary();
				}
			}
			
			child.validate(this);
		}
		
		if(parent instanceof Entity && !primary) {
			throw new ValidationException(Res.STRINGS.VALIDATOR_NO_PRIMARY_ATTRIBUTES);
		}
	
		if(parent instanceof Entity && names.isEmpty()) {
			throw new ValidationException(Res.STRINGS.VALIDATOR_NO_ATTRIBUTES);
		}
	}

	public void validate(Relation relation) throws ValidationException {
		if(relation.getEntity() == null) {
			return;
		}

		boolean entityExists = false, parentExists = false;
		
		if(relation.getParent() == null || relation.getParent().getParent() == null)
			throw new ValidationException(Res.STRINGS.VALIDATOR_NO_PARENT_ENTITY);
			
		Entity grandparent = (Entity) relation.getParent().getParent();
		Resource resource = (Resource) grandparent.getParent();
		
		for (Entity e : resource.getEntities()) {
			if(relation.getEntity().equals(e))
				entityExists = true;
			if(grandparent.equals(e))
				parentExists = true;
		}
		
		if(!entityExists || !parentExists)
			throw new ValidationException(Res.STRINGS.VALIDATOR_NO_CONNECT_ENTITIES);
		
		if(relation.getType() == null)
			throw new ValidationException(Res.STRINGS.VALIDATOR_NO_TYPE);
	}

	public void validate(Record record) throws ValidationException {
		Entity parent = record.getParent();

		for(Attribute a : parent.getAttributes()) {
			String key = a.getName();
			String value = record.get(key);

			if(a.isRequired() && (value == null || value.isEmpty())) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_ATTRIBUTE_NO_VALUE);
			}

			validateAttributeValue(a.getOptions(), value, a.getLength());
		}
	}

	private void validateRecords(Entity entity) throws ValidationException {
		HashMap<String, Boolean> mapa = null;
		for(Record r : entity.getRecords()) {
			String primary = r.getPrimary();
			if(mapa != null &&	((mapa.get(primary) != null) || mapa.get(primary))) 
				throw new ValidationException(Res.STRINGS.VALIDATOR_ATTRIBUTES_NOT_UNIQUE);
		
			if(mapa != null)
				mapa.put(primary, true);
		}
	}
	
	private void validateAttributeValue(AttributeOptions options, String value, int length) throws ValidationException{
		if(value == null || value.isEmpty())
			return;
		
		if(options instanceof NumberOptions) {
			NumberOptions numberOptions = (NumberOptions) options;
			int num;
			
			try {
				num = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_INTEGER);
			}
			
			if(num < numberOptions.getMinInt())
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			if(numberOptions.getMaxInt() == 0) {
				if(value.length() > length)
					throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			}else if(num > numberOptions.getMaxInt())
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			
		}else if(options instanceof StringOptions) {
			StringOptions stringOptions = (StringOptions) options;

            if(stringOptions.getPattern() != null && !value.matches(stringOptions.getPattern()))
				throw new ValidationException(Res.STRINGS.VALIDATOR_PATTERN_NOT_MATCHED);
			
			if(value.length() < stringOptions.getMinLength())
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			if(stringOptions.getMaxLength() == 0) {
				if(value.length() > length) 
					throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			}else if(value.length() > stringOptions.getMaxLength())
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			
		}else if(options instanceof FloatOptions) {
			FloatOptions floatOptions = (FloatOptions) options;
			float num;
			
			try {
				num = Float.parseFloat(value);
			} catch (NumberFormatException e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_DEFAULT_NOT_FLOAT);
			}
			
			if(num < floatOptions.getMinFloat())
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			if(floatOptions.getMaxFloat() == 0) {
				if(value.length() > length)
					throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			}else if(floatOptions.getMaxFloat() < num)
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			
		}else if(options instanceof DateOptions) {
			DateOptions dateOptions = (DateOptions) options;
			SimpleDateFormat parser = new SimpleDateFormat(dateOptions.getFormat());
			Date min, max, def;

			try {
				def = parser.parse(value);
				if(dateOptions.getMinDate().isEmpty())
					min = new Date(Long.MIN_VALUE);
				else min = parser.parse(dateOptions.getMinDate());
				if(dateOptions.getMaxDate().isEmpty())
					max = new Date(Long.MAX_VALUE);
				else max = parser.parse(dateOptions.getMaxDate());
			}catch(Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATES_NOT_MATCHED);
			}
			
			if(def.after(max) || min.after(def))
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATESTIMES_NOT_RANGE);
			
		}else if(options instanceof TimeOptions) {
			TimeOptions timeOptions = (TimeOptions) options;
			SimpleDateFormat parser = new SimpleDateFormat(timeOptions.getFormat());
			Date min, max, def;

			try {
				def = parser.parse(value);
				if(timeOptions.getMinTime().isEmpty())
					min = new Date(Long.MIN_VALUE);
				else min = parser.parse(timeOptions.getMinTime());
				if(timeOptions.getMaxTime().isEmpty())
					max = new Date(Long.MAX_VALUE);
				else max = parser.parse(timeOptions.getMaxTime());
			}catch(Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATES_NOT_MATCHED);
			}
			
			if(def.after(max) || min.after(def))
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATESTIMES_NOT_RANGE);
			
		}else if(options instanceof DateTimeOptions) {
			DateTimeOptions dateTimeOptions = (DateTimeOptions) options;
			SimpleDateFormat parser;

			try {
				parser = new SimpleDateFormat(dateTimeOptions.getFormat());
			}catch (NullPointerException e) {
				parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				dateTimeOptions.setFormat("yyyy-MM-dd HH:mm:ss");
			}
			Date min, max, def;

			try {
				def = parser.parse(value);
				if(dateTimeOptions.getMinDateTime() == null || dateTimeOptions.getMinDateTime().isEmpty())
					min = new Date(Long.MIN_VALUE);
				else min = parser.parse(dateTimeOptions.getMinDateTime());
				if(dateTimeOptions.getMaxDateTime() == null || dateTimeOptions.getMaxDateTime().isEmpty())
					max = new Date(Long.MAX_VALUE);
				else max = parser.parse(dateTimeOptions.getMaxDateTime());
			}catch(Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATES_NOT_MATCHED);
			}
			
			if(def.after(max) || min.after(def))
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATESTIMES_NOT_RANGE);
			
		}
	}


	private void validateOptions(AttributeOptions options, int length) throws ValidationException {
		if(options instanceof NumberOptions) {
			NumberOptions numberOptions = (NumberOptions) options;
			int max = numberOptions.getMaxInt(), min = numberOptions.getMinInt();
			if(max == 0)
				max = length;

			if(min > max)
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			String size = min + "";
			if(size.length() > length)
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			size = max + "";
			if(size.length() > length)
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			
		}else if(options instanceof StringOptions) {
			StringOptions stringOptions = (StringOptions) options;
			Pattern pattern;
			
			String stringPattern = stringOptions.getPattern();
			if(stringPattern != null) {
				try {
					pattern = Pattern.compile(stringOptions.getPattern());
				}catch(PatternSyntaxException e) {
					throw new ValidationException(Res.STRINGS.VALIDATOR_PATTERN_NOT_VALID);
				}
			}
	
			if(stringOptions.getMaxLength() < stringOptions.getMinLength() || 
					stringOptions.getMaxLength() > length || stringOptions.getMinLength() > length)
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_LENGHT_RANGE);

	
		}else if(options instanceof FloatOptions) {
			FloatOptions floatOptions = (FloatOptions) options;
			
			if(floatOptions.getMinFloat() > floatOptions.getMaxFloat())
				throw new ValidationException(Res.STRINGS.VALIDATOR_DEFAULT_NOT_RANGE);
			String size = floatOptions.getMinFloat() + "";
			if(size.length() > length)
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			size = floatOptions.getMaxFloat() + "";
			if(size.length() > length)
				throw new ValidationException(Res.STRINGS.VALIDATOR_NOT_IN_RANGE);
			
		}else if(options instanceof DateOptions) {
			DateOptions dateOptions = (DateOptions) options;
			SimpleDateFormat parser;

			try {
				parser = new SimpleDateFormat(dateOptions.getFormat());
			}catch (NullPointerException e) {
				parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				dateOptions.setFormat("yyyy-MM-dd HH:mm:ss");
			}catch (Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_FORMAT_NOT_VALID);
			}
			Date min, max;

			try {
				if(dateOptions.getMinDate() == null)
					min = new Date(Long.MIN_VALUE);
				else min = parser.parse(dateOptions.getMinDate());
				if(dateOptions.getMaxDate() == null)
					max = new Date(Long.MAX_VALUE);
				else max = parser.parse(dateOptions.getMaxDate());
			}catch(Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATES_NOT_MATCHED);
			}
			if(min.after(max) || dateOptions.getFormat().length() > length)
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATES_NOT_MATCHED);
			
		}else if(options instanceof TimeOptions) {
			TimeOptions timeOptions = (TimeOptions) options;
			SimpleDateFormat parser;

			try {
				parser = new SimpleDateFormat(timeOptions.getFormat());
			}catch (NullPointerException e) {
				parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeOptions.setFormat("yyyy-MM-dd HH:mm:ss");
			}catch (Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_FORMAT_NOT_VALID);
			}
			Date min, max;

			try {
				if(timeOptions.getMinTime().isEmpty())
					min = new Date(Long.MIN_VALUE);
				else min = parser.parse(timeOptions.getMinTime());
				if(timeOptions.getMaxTime().isEmpty())
					max = new Date(Long.MAX_VALUE);
				else max = parser.parse(timeOptions.getMaxTime());
			}catch(Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_TIME_NOT_MATCHED);
			}
			if(min.after(max) || timeOptions.getFormat().length() > length)
				throw new ValidationException(Res.STRINGS.VALIDATOR_DEFAULT_TIME_NOT_RANGE);

		}else if(options instanceof DateTimeOptions) {
			DateTimeOptions dateTimeOptions = (DateTimeOptions) options;
			SimpleDateFormat parser;

			try {
				parser = new SimpleDateFormat(dateTimeOptions.getFormat());
			}catch (NullPointerException e) {
				parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				dateTimeOptions.setFormat("yyyy-MM-dd HH:mm:ss");
			}catch (Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_FORMAT_NOT_VALID);
			}
			Date min, max;

			try {
				if(dateTimeOptions.getMinDateTime() == null || dateTimeOptions.getMinDateTime().isEmpty())
					min = new Date(Long.MIN_VALUE);
				else min = parser.parse(dateTimeOptions.getMinDateTime());
				if(dateTimeOptions.getMaxDateTime() == null || dateTimeOptions.getMaxDateTime().isEmpty())
					max = new Date(Long.MIN_VALUE);
				else max = parser.parse(dateTimeOptions.getMaxDateTime());
			}catch(Exception e) {
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATESTIMES_NOT_MATCHED);
			}
			if(min.after(max) || dateTimeOptions.getFormat().length() > length)
				throw new ValidationException(Res.STRINGS.VALIDATOR_DATESTIMES_NOT_RANGE);
		}
	}

}