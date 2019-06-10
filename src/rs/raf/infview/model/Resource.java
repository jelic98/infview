package rs.raf.infview.model;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.options.NoResourceOptions;
import rs.raf.infview.model.options.ResourceOptions;
import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaObject;
import rs.raf.infview.model.schema.SchemaSet;
import rs.raf.infview.model.schema.SchemaTreeSet;
import rs.raf.infview.model.type.ResourceType;
import rs.raf.infview.model.validator.Validateable;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import rs.raf.infview.view.component.Node;

import java.util.Set;

@SchemaObject
public class Resource extends AbstractModel {

    @SchemaField
    private int id;

    @SchemaField
    private String name;

    @SchemaField
    private ResourceType type;

    @SchemaField
    private ResourceOptions options;

    @SchemaField
    private SchemaSet<Entity> entities;

    public Resource(String name) {
        super(name, Res.ICONS.RESOURCE);

        setName(name);
        setId(String.valueOf((int) System.currentTimeMillis()));

        entities = new SchemaTreeSet<>();
        this.options = new NoResourceOptions();
    }

    private Resource(Resource resource) {
        super(resource);

        this.id = resource.id;
        this.name = resource.name;
        this.type = resource.type;
        this.options = resource.options.getClone();

        SchemaSet<Entity> copySet = new SchemaTreeSet<>();

        for(Entity e : resource.entities) {
            Entity clone = (Entity) e.getClone();

            super.addChild(clone);

            copySet.add(clone);
        }

        this.entities = copySet;
    }

    @Override
    public AbstractModel getClone() {
        return new Resource(this);
    }

    @Override
    public AbstractModel getChild(String name) {
        return new Entity(name);
    }

    @Override
    public String getChildType() {
        return Entity.class.getSimpleName();
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Resource)) {
            return false;
        }

        Resource resource = (Resource) obj;

        if(getId() - resource.getId() != 0) {
            return false;
        }

        return getNodeName().equals(resource.getNodeName());
    }

    @Override
    public int compareTo(Node node) {
        if(node == null) {
            return 1;
        }

        AbstractModel model = (AbstractModel) node.getDelegateModel();

        if(!(model instanceof Resource)) {
            return 1;
        }

        Resource resource = (Resource) model;

        int compare = getId() - resource.getId();

        if(compare != 0) {
            return compare;
        }

        return getNodeName().compareTo(resource.getNodeName());
    }

    @Override
    public boolean hasValidateables() {
        return entities != null && !entities.isEmpty();
    }

    @Override
    public Set<? extends Validateable> getValidateables() {
        return entities;
    }

    @Override
    public String getUid() {
        return String.valueOf(id);
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;

        setNodeName(name);
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;

        options = type.getOptions();
    }

    public ResourceOptions getOptions() {
        return options;
    }

    public void setOptions(ResourceOptions options) {
        this.options = options;
    }

    public SchemaSet<Entity> getEntities() {
        return entities;
    }

    public void setEntities(SchemaSet<Entity> entities) {
        this.entities = entities;

        for(Entity entity : entities) {
            super.addChild(entity);
        }
    }

    @Override
    public void validate(Validator validator) throws ValidationException {
        validator.validate(this);
    }

    @Override
    public boolean addChild(Node node) {
        boolean result = super.addChild(node);

        entities.add((Entity) node.getDelegateModel());

        return result;
    }

    @Override
    public void removeChildren() {
        super.removeChildren();

        entities.clear();
    }
}