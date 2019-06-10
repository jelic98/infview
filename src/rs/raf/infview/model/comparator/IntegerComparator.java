package rs.raf.infview.model.comparator;

import rs.raf.infview.model.Record;

public class IntegerComparator extends RecordComparator {

    @Override
    public int compare(Record r1, Record r2) {
        if(attribute == null) {
            return 0;
        }

        int value1;
        int value2;

        try {
            value1 = Integer.parseInt(r1.get(attribute));
            value2 = Integer.parseInt(r2.get(attribute));
        }catch(NumberFormatException e) {
            return 0;
        }

        return Integer.compare(value1, value2);
    }
}