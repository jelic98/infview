package rs.raf.infview.util;

import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaSkip;
import rs.raf.infview.util.log.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Reflection {

    public Map<String, Object> getSchema(Object obj) {
        Map<String, Object> schema = new LinkedHashMap<>();

        Field[] fields = getAnnotatedFields(obj.getClass(), SchemaField.class, false);

        for(Field f : fields) {
            if(f.isAnnotationPresent(SchemaSkip.class)) {
                continue;
            }

            f.setAccessible(true);

            String name = f.getName();
            Object value;

            try {
                value = f.get(obj);
            }catch(IllegalAccessException e) {
                value = null;
            }

            schema.put(name, value);
        }

        return schema;
    }

    public Object instantiate(Class clazz) {
        try {
            return clazz.newInstance();
        }catch(Exception e) {
            return null;
        }
    }

    public Object get(Field field, Object object) {
        field.setAccessible(true);

        try {
            return field.get(object);
        }catch(IllegalAccessException e) {
            Log.e(e.getMessage());
            return null;
        }finally {
            field.setAccessible(false);
        }
    }

    public void set(Field field, Object object, Object value) {
        field.setAccessible(true);

        try {
            field.set(object, value);
        }catch(IllegalAccessException e) {
            Log.e(e.getMessage());
        }finally {
            field.setAccessible(false);
        }
    }

    public void invokeSetter(Field field, Object object, Object value) {
        if(object == null || value == null) {
            return;
        }

        String setterName = "set" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));

        try {
            Method method = object.getClass().getMethod(setterName, value.getClass());
            method.invoke(object, value);
        }catch(Exception e) {
            Log.e(e.getMessage());
        }
    }

    public Field getField(Class<?> clazz, String name) {
        Field[] fields = getFields(clazz);

        for(Field f : fields) {
            if(f.getName().equals(name)) {
                return f;
            }
        }

        return null;
    }

    private Field[] getFields(Class<?> clazz) {
        List<Field> fields = new LinkedList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Collections.addAll(fields, declaredFields);

        Class superClass = clazz.getSuperclass();

        if(superClass != null) {
            Field[] declaredFieldsOfSuper = getFields(superClass);

            if(declaredFieldsOfSuper.length > 0)
                Collections.addAll(fields, declaredFieldsOfSuper);
        }

        return fields.toArray(new Field[0]);
    }

    public Field[] getAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotation, boolean typeAnnotation) {
        List<Field> annotatedFields = new LinkedList<>();

        while(clazz.getSuperclass() != null) {
            Field[] allFields = getFields(clazz);

            for(Field field : allFields) {
                if(typeAnnotation) {
                    if(field.getType().isAnnotationPresent(annotation)) {
                        annotatedFields.add(field);
                    }
                }else {
                    if(field.isAnnotationPresent(annotation)) {
                        annotatedFields.add(field);
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }

        return annotatedFields.toArray(new Field[0]);
    }
}