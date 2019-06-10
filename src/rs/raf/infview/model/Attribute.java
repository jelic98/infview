package rs.raf.infview.model;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.options.AttributeOptions;
import rs.raf.infview.model.options.NoAttributeOptions;
import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaObject;
import rs.raf.infview.model.schema.SchemaSkip;
import rs.raf.infview.model.type.AttributeType;
import rs.raf.infview.model.validator.Validateable;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import rs.raf.infview.view.component.Node;

import java.util.Set;

@SchemaObject
public class Attribute extends AbstractModel {

    @SchemaField
    private int id;

    @SchemaField
    private String name;

    @SchemaField
    private String defaultValue;

    @SchemaField
    private boolean primary;

    @SchemaField
    private boolean required;

    @SchemaField
    private int length;

    @SchemaField
    private AttributeType type;

    @SchemaField
    private AttributeOptions options;

    @SchemaSkip
    @SchemaField
    private Relation relation;

    public Attribute(String name) {
        super(name, Res.ICONS.ATTRIBUTE);

        setName(name);
        setId(String.valueOf((int) System.currentTimeMillis()));

        this.options = new NoAttributeOptions();
    }

    private Attribute(Attribute attribute) {
        super(attribute);

        this.id = attribute.id;
        this.name = attribute.name;
        this.defaultValue = attribute.defaultValue;
        this.primary = attribute.primary;
        this.required = attribute.required;
        this.length = attribute.length;
        this.type = attribute.type;
        this.options = attribute.options.getClone();
    }

    @Override
    public AbstractModel getClone() {
        return new Attribute(this);
    }

    @Override
    public AbstractModel getChild(String name) {
        return null;
    }

    @Override
    public String getChildType() {
        return null;
    }

    @Override
    public int getLevel() {
        return 3;
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
    public boolean equals(Object obj) {
        if(!(obj instanceof Attribute)) {
            return false;
        }

        Attribute attribute = (Attribute) obj;

        if(getId() - attribute.getId() != 0) {
            return false;
        }

        return getNodeName().equals(attribute.getNodeName());
    }

    @Override
    public int compareTo(Node node) {
        if(!(node instanceof AbstractModel)) {
            node = node.getDelegateModel();
        }

        if(!(node instanceof Attribute)) {
            return 1;
        }

        Attribute attribute = (Attribute) node;

        if(getAncestor() == null || attribute.getAncestor() == null) {
            return 0;
        }

        Entity entity1 = (Entity) getAncestor().getDelegateModel();
        Entity entity2 = (Entity) attribute.getAncestor().getDelegateModel();

        int compare = entity1.getId() - entity2.getId();

        if(compare != 0) {
            return compare;
        }

        compare = getId() - attribute.getId();

        if(compare != 0) {
            return compare;
        }

        return getNodeName().compareTo(attribute.getNodeName());
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

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = Integer.parseInt(length);
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;

        options = type.getOptions();
    }

    public AttributeOptions getOptions() {
        return options;
    }

    public void setOptions(AttributeOptions options) {
        this.options = options;
    }

    public boolean isRelation() {
        return relation != null;
    }

    public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
        this.relation.setParent(this);
    }

    @Override
    public void validate(Validator validator) throws ValidationException {
    	validator.validate(this);
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();

        ((Entity) getAncestor().getDelegateModel()).getAttributes().remove(this);
    }
}