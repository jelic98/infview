package rs.raf.infview.model;

import rs.raf.infview.model.schema.*;
import rs.raf.infview.model.type.RelationType;
import rs.raf.infview.model.validator.Validateable;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import java.util.Set;

@SchemaObject
public class Relation implements Validateable, SchemaSerializable, DeepCopy<Relation> {

    private Validateable parent;

    @SchemaRecursive
    @SchemaField
    private Entity entity;

    @SchemaField
    private RelationType type;

    public Relation() {
        super();
    }

    private Relation(Relation relation) {
        this.parent = relation.parent;
        this.entity = (Entity) relation.entity.getClone();
        this.type = relation.type;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    public void setParent(Validateable parent) {
        this.parent = parent;
    }

    @Override
    public Relation getClone() {
        return new Relation(this);
    }

    @Override
    public Validateable getParent() {
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
    public String getUid() {
        return entity.getUid();
    }
}