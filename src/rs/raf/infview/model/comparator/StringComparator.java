package rs.raf.infview.model.comparator;

import rs.raf.infview.model.Record;

public class StringComparator extends RecordComparator {

    @Override
    public int compare(Record r1, Record r2) {
        if(attribute == null) {
            return 0;
        }

        String value1 = r1.get(attribute);
        String value2 = r2.get(attribute);

        return value1.compareTo(value2);
    }
}