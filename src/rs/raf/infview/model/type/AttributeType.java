package rs.raf.infview.model.type;

import rs.raf.infview.model.comparator.*;
import rs.raf.infview.model.options.*;
import rs.raf.infview.model.schema.SchemaType;
import rs.raf.infview.util.Reflection;

@SchemaType
public enum AttributeType {

    STRING(StringOptions.class, StringComparator.class),
    INTEGER(NumberOptions.class, IntegerComparator.class),
    FLOAT(FloatOptions.class, FloatComparator.class),
    DATE(DateOptions.class, NoComparator.class),
    TIME(TimeOptions.class, NoComparator.class),
    DATETIME(DateTimeOptions.class, NoComparator.class),
    BOOLEAN(NoAttributeOptions.class, BooleanComparator.class),
    OBJECT(NoAttributeOptions.class, NoComparator.class);

    private Class options, comparator;

    AttributeType(Class options, Class comparator) {
        this.options = options;
        this.comparator = comparator;
    }

    public AttributeOptions getOptions() {
        return (AttributeOptions) new Reflection().instantiate(options);
    }

    public RecordComparator getComparator() {
        return (RecordComparator) new Reflection().instantiate(comparator);
    }
}