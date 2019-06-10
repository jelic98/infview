package rs.raf.infview.observer.command;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Paths;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.Relation;
import rs.raf.infview.model.type.RelationType;
import rs.raf.infview.state.Context;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Resource;
import rs.raf.infview.model.options.AttributeOptions;
import rs.raf.infview.model.options.DatabaseOptions;
import rs.raf.infview.model.options.FileOptions;
import rs.raf.infview.model.options.ResourceOptions;
import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaUtils;
import rs.raf.infview.model.type.AttributeType;
import rs.raf.infview.model.type.ResourceType;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.util.JsonParser;
import rs.raf.infview.util.Reflection;
import rs.raf.infview.util.io.file.AbstractFile;
import rs.raf.infview.util.io.file.RelationalDatabase;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.component.FileChooser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAction extends Command {
    
	private static final Reflection reflection = new Reflection();

	@Override
	public void execute() {
        File selected = App.UI.getFileChooser()
                .setStartPath(Paths.getHomePath())
                .setPathType(FileChooser.PathType.DIRECTORIES_ONLY)
                .setExtensions(Res.STRINGS.INFO_APP_FILES, Paths.getExtensions())
                .getSingle();

        if(selected == null) {
            return;
        }

        try {

        	String resourcePath = Paths.separate(selected.getAbsolutePath()) + App.RESOURCE_PATH;

        	JSONObject resourceSchema = new JsonParser().parse(new File(resourcePath));
        	SchemaUtils utils = new SchemaUtils();

        	String name = utils.getFieldSchema(resourceSchema, "name");
        	Resource resource = new Resource(name);
        	resource.setId(utils.getFieldSchema(resourceSchema, "id"));
        	String type = utils.getFieldSchema(resourceSchema, "type");
        	
        	ResourceType resourceType = ResourceType.valueOf(type.toUpperCase());
        	resource.setType(resourceType);
        	ResourceOptions resourceOptions = resourceType.getOptions();
        	
        	Field[] fields = reflection.getAnnotatedFields(resourceOptions.getClass(), SchemaField.class, false);
        	
        	for(Field f : fields) {
        		String fieldName = f.getName();
        		String value;
        		try {
        			JSONObject optionSchema = utils.getObjectSchema(resourceSchema, "options");
        			value = utils.getFieldSchema(optionSchema, fieldName);
        		}catch (ValidationException e) {
					value = "";
				}
      			reflection.invokeSetter(f, resourceOptions, value);
        	}
        	resource.setOptions(resourceOptions);
        	if(resourceOptions instanceof FileOptions) {
        		if(((FileOptions) resource.getOptions()).getPath() == null ||
        			((FileOptions) resource.getOptions()).getPath().isEmpty())
        		((FileOptions) resource.getOptions()).setPath(resourcePath);
        	}
        	
        	JSONArray entities = utils.getArraySchema(resourceSchema, "entities");
        	for(int i = 0; i < entities.length(); i++) {
        		JSONObject e = entities.getJSONObject(i);
        		String entityName = utils.getFieldSchema(e, "name");
        		Entity entity = new Entity(entityName);
        		entity.setId(utils.getFieldSchema(e, "id"));
        		resource.addChild(entity);
        	}
        	
        	for(Entity e : resource.getEntities()) {
        		JSONObject entity = entities.getJSONObject(0);
        		for(int i = 0; i < entities.length(); i++) {
        			String nameE = utils.getFieldSchema(entities.getJSONObject(i), "name");
        			if(e.getName().equals(nameE))
        				entity = entities.getJSONObject(i);
        		}
        		JSONArray attributes = utils.getArraySchema(entity, "attributes");
        		for(int j = 0; j < attributes.length(); j++) {
					// TODO Zasto su imena polja zakucana? Cemu onda refleksija!? (Stefan)
        			JSONObject a = attributes.getJSONObject(j);
        			String attributeName = utils.getFieldSchema(a, "name");
        			Attribute attribute = new Attribute(attributeName);
        			attribute.setId(utils.getFieldSchema(a, "id"));
        			attribute.setDefaultValue(utils.getFieldSchema(a, "defaultValue"));
					attribute.setLength(utils.getFieldSchema(a, "length"));
        			attribute.setPrimary(Boolean.parseBoolean(utils.getFieldSchema(a, "primary")));
        			attribute.setRequired(Boolean.parseBoolean(utils.getFieldSchema(a, "required")));
        			type = utils.getFieldSchema(a, "type");
        			
        			AttributeType attributeType = AttributeType.valueOf(type.toUpperCase());
        			attribute.setType(attributeType);
        			AttributeOptions attributeOptions = attributeType.getOptions();
        			fields = reflection.getAnnotatedFields(attributeOptions.getClass(), SchemaField.class, false);
                	
                	for(Field f : fields) {
                		String fieldName = f.getName();
                		String value;
                		try {
                			JSONObject optionSchema = utils.getObjectSchema(a, "options");
                			value = utils.getFieldSchema(optionSchema, fieldName);
                		}catch (ValidationException exception) {
        					value = attribute.getDefaultValue();
        				}
                		reflection.invokeSetter(f, attributeOptions, value);
                	}
                	
                	attribute.setOptions(attributeOptions);
                	
                	JSONObject r = utils.getObjectSchema(a, "relation");

                	if(r != null) {
						Relation relation = new Relation();
						String relationEntity = utils.getFieldSchema(r, "entity");
						int idE = 0;

						if(relationEntity == null)
							relation.setEntity(null);
						else idE = Integer.parseInt(utils.getFieldSchema(r, "entity"));

						if(idE != 0)
							for(Entity tEntity : resource.getEntities())
								if(tEntity.getId() == idE)
									relation.setEntity(tEntity);

						String rType = utils.getFieldSchema(r, "type");
						RelationType relationType;

						if(rType == null)
							relationType = null;
						else relationType = RelationType.valueOf(rType.toUpperCase());

						relation.setType(relationType);
						attribute.setRelation(relation);
					}

        			e.addChild(attribute);
                }

        		if(resourceOptions instanceof FileOptions) {
	        		AbstractFile file = (AbstractFile) resource.getType().getResource();
	
	                String extension = resource.getOptions().getExtension();
	
	                if(extension == null) {
	                     extension = "";
	                }
	
	                String entityPath = Paths.separate(selected.getAbsolutePath()) + e.getName() + extension;
	
	               	try {
	                	file.open(entityPath, false, e.getAttributes());
	                }catch(IOException exception) {
	                     continue;
	                }

					Context.instance().fileMatcher.add(e, file);
        		}else if(resourceOptions instanceof DatabaseOptions) {
					RelationalDatabase database = (RelationalDatabase) resource.getType().getResource();
					database.open(e.getName(), e.getAttributes(), (DatabaseOptions) resourceOptions);

					Context.instance().fileMatcher.add(e, database);
				}
        	}
        	
        	Context.instance().tree.getRoot().addChild(resource);
        }catch(Exception e) {
        	e.printStackTrace();
            DialogAdapter.error(e.getMessage());
        }

        Context.instance().tree.reload();
    }
}