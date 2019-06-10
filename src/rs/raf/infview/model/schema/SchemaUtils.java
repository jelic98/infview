package rs.raf.infview.model.schema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.util.JsonParser;

public class SchemaUtils {

    public String getFieldSchema(JSONObject schema, String key) throws ValidationException {
        if(schema == null || !schema.has(key)) {
            return null;
        }

        try {
            return schema.getString(key);
        }catch(JSONException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public JSONObject getObjectSchema(JSONObject schema, String key) throws ValidationException {
        if(schema == null || !schema.has(key)) {
            return null;
        }

        try {
            return schema.getJSONObject(key);
        }catch(JSONException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public JSONArray getArraySchema(JSONObject schema, String key) throws ValidationException{
        if(schema == null || !schema.has(key)) {
            return null;
        }

        try {
            return schema.getJSONArray(key);
        }catch(JSONException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public JSONObject loadSchema() throws ValidationException {
        try {
            return new JsonParser().parse(Res.SCHEMA.META_META_SCHEMA);
        }catch(Exception e) {
            throw new ValidationException(Res.STRINGS.ERROR_CANNOT_LOAD_SCHEMA);
        }
    }
}
