package rs.raf.infview.model.comparator;

import rs.raf.infview.model.Record;

public class BooleanComparator extends RecordComparator {

    @Override
    public int compare(Record r1, Record r2) {
        if(attribute == null) {
            return 0;
        }

        boolean value1 = Boolean.parseBoolean(r1.get(attribute));
        boolean value2 = Boolean.parseBoolean(r2.get(attribute));

        return Boolean.compare(value1, value2);
    }
}