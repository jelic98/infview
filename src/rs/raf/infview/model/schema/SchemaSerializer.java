package rs.raf.infview.model.schema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rs.raf.infview.util.Reflection;
import java.lang.reflect.Field;

public class SchemaSerializer {

	private static final Reflection reflection = new Reflection();

	public JSONObject serialize(SchemaSerializable model) throws SchemaException {
		if(model == null) {
			return null;
		}

		JSONObject json = new JSONObject();

		Field[] fields = reflection.getAnnotatedFields(model.getClass(), SchemaField.class, false);

		for(Field field : fields) {
			try {
				addField(field, model, json);
			}catch(Exception e) {
				throw new SchemaException(e.getMessage());
			}
		}

		return json;
	}

	private void addField(Field field, Object object, JSONObject json) throws JSONException, SchemaException {
		Class type = field.getType();
		Object value = reflection.get(field, object);

		if(value == null) {
			return;
		}

		if(type.isAnnotationPresent(SchemaObject.class) || type.isAnnotationPresent(SchemaOptions.class)) {
			if(field.isAnnotationPresent(SchemaRecursive.class)) {
				json.put(field.getName(), ((SchemaSerializable) value).getUid());
			}else {
				json.put(field.getName(), serialize((SchemaSerializable) value));
			}
		}else if(type.isAnnotationPresent(SchemaType.class)) {
			json.put(field.getName(), value.toString().toLowerCase());
		}else if(type.isAnnotationPresent(SchemaCollection.class)) {
			JSONArray array = new JSONArray();

			SchemaSet<SchemaSerializable> set = (SchemaSet<SchemaSerializable>) value;

			for(SchemaSerializable s : set) {
				array.put(serialize(s));
			}

			json.put(field.getName(), array);
		}else {
			json.put(field.getName(), value);
		}
	}

	private String toProperCase(String s, boolean firstPart) {
		if(firstPart) {
			return s.toLowerCase();
		}else {
			return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
		}
	}
}