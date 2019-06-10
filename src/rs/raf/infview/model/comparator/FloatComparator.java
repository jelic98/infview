package rs.raf.infview.model.comparator;

import rs.raf.infview.model.Record;

public class FloatComparator extends RecordComparator {

    @Override
    public int compare(Record r1, Record r2) {
        if(attribute == null) {
            return 0;
        }

        float value1;
        float value2;

        try {
            value1 = Float.parseFloat(r1.get(attribute));
            value2 = Float.parseFloat(r2.get(attribute));
        }catch(NumberFormatException e) {
            return 0;
        }

        return Float.compare(value1, value2);
    }
}