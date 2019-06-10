package rs.raf.infview.model.comparator;

import rs.raf.infview.model.Record;
import java.util.Comparator;

public abstract class RecordComparator implements Comparator<Record> {

    String attribute;

    @Override
    public abstract int compare(Record r1, Record r2);

    public RecordComparator on(String attribute) {
        this.attribute = attribute;

        return this;
    }
}