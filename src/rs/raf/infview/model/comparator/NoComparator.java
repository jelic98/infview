package rs.raf.infview.model.comparator;

import rs.raf.infview.model.Record;

public class NoComparator extends RecordComparator {

    @Override
    public int compare(Record r1, Record r2) {
        return 0;
    }
}