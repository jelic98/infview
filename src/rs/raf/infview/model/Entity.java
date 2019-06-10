package rs.raf.infview.model;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.schema.SchemaField;
import rs.raf.infview.model.schema.SchemaObject;
import rs.raf.infview.model.schema.SchemaSet;
import rs.raf.infview.model.schema.SchemaTreeSet;
import rs.raf.infview.model.validator.Validateable;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import rs.raf.infview.view.component.Node;
import rs.raf.infview.view.component.TableModel;
import java.util.Set;
import java.util.TreeSet;

@SchemaObject
public class Entity extends AbstractModel implements TableModel {

    @SchemaField
    private int id;

    @SchemaField
    private String name;

    @SchemaField
    private SchemaSet<Attribute> attributes;

    private Set<Record> records, trash, filtered;
    private String[] columns;
    private Object[][] rows;

    public Entity(String name) {
        super(name, Res.ICONS.ENTITY);

        setName(name);
        setId(String.valueOf((int) System.currentTimeMillis()));

        attributes = new SchemaTreeSet<>();
        records = new TreeSet<>();
        trash = new TreeSet<>();
        filtered = new TreeSet<>();
        columns = new String[0];
        rows = new Object[0][0];
    }

    private Entity(Entity entity) {
        super(entity);

        this.id = entity.id;
        this.name = entity.name;

        SchemaSet<Attribute> copySet = new SchemaTreeSet<>();

        for(Attribute a : entity.attributes) {
            Attribute clone = (Attribute) a.getClone();

            super.addChild(clone);

            copySet.add(clone);
        }

        this.attributes = copySet;
        this.records = entity.records;
        this.trash = entity.trash;
        this.filtered = entity.filtered;
        this.columns = entity.columns;
        this.rows = entity.rows;
    }

    @Override
    public AbstractModel getClone() {
        return new Entity(this);
    }

    @Override
    public AbstractModel getChild(String name) {
        return new Attribute(name);
    }

    @Override
    public String getChildType() {
        return Attribute.class.getSimpleName();
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public String[] getColumns() {
        if(columns.length == attributes.size()) {
            return columns;
        }

        updateColumns();

        return columns;
    }

    @Override
    public Object[][] getRows() {
    	if(!filtered.isEmpty()) {
    		updateRows(filtered);
    		return rows;
    	}
    	
        if(rows.length == records.size()) {
            return rows;
        }

        updateRows(records);

        return rows;
    }

    private void updateColumns() {
        columns = new String[attributes.size()];

        int i = 0;

        for(Attribute attribute : attributes) {
            columns[i++] = attribute.getName();
        }
    }

    private void updateRows(Set<Record> records) {
        rows = new Object[records.size()][attributes.size()];

        int i = 0;

        for(Record record : records) {
            int j = 0;

            for(Attribute attribute : attributes) {
                rows[i][j++] = record.get(attribute.getName());
            }

            i++;
        }
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Entity)) {
            return false;
        }

        Entity entity = (Entity) obj;

        if(getId() - entity.getId() != 0) {
            return false;
        }

        return getNodeName().equals(entity.getNodeName());
    }

    @Override
    public int compareTo(Node node) {
        if(!(node instanceof AbstractModel)) {
            node = node.getDelegateModel();
        }

        if(!(node instanceof Entity)) {
            return 1;
        }

        Entity entity = (Entity) node;

        if(getAncestor() == null || entity.getAncestor() == null) {
            return 0;
        }

        Resource resource1 = (Resource) getAncestor().getDelegateModel();
        Resource resource2 = (Resource) entity.getAncestor().getDelegateModel();

        int compare = resource1.getId() - resource2.getId();

        if(compare != 0) {
            return compare;
        }

        compare = getId() - entity.getId();

        if(compare != 0) {
            return compare;
        }

        return getNodeName().compareTo(entity.getNodeName());
    }

    @Override
    public boolean hasValidateables() {
        return attributes != null && !attributes.isEmpty();
    }

    @Override
    public Set<? extends Validateable> getValidateables() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;

        setNodeName(name);
    }

    public SchemaSet<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(SchemaSet<Attribute> attributes) {
        this.attributes = attributes;

        for(Attribute attribute : attributes) {
            super.addChild(attribute);
        }
    }

    public void addRecord(Record record) {
        if(record == null) {
            return;
        }

    	record.setParent(this);

        records.add(record);
        trash.remove(record);

        updateRows(records);
    }

    public void removeRecord(Record record, boolean permanently) {
        if(permanently) {
            trash.remove(record);
            return;
        }

        trash.add(record);
        records.remove(record);

        updateRows(records);
    }

    public Set<Record> getRecords() {
        return records;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;

        updateRows(records);
    }
    
    public void setFiltered(Set<Record> filtered) {
        this.filtered = filtered;
    }
    
    public void clearFiltered() {
    	filtered.clear();
    }

    public Set<Record> getTrash() {
        return trash;
    }

    @Override
    public void validate(Validator validator) throws ValidationException {
        validator.validate(this);
    }

    @Override
    public boolean addChild(Node node) {
        boolean result = super.addChild(node);

        attributes.add((Attribute) node.getDelegateModel());

        return result;
    }

    @Override
    public void removeChildren() {
        super.removeChildren();

        attributes.clear();
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();

        ((Resource) getAncestor().getDelegateModel()).getEntities().remove(this);
    }
}