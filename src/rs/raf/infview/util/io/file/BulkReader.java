package rs.raf.infview.util.io.file;

import rs.raf.infview.model.Record;
import rs.raf.infview.util.io.file.AbstractResource.SortType;

import java.util.Map;
import java.util.Set;

public interface BulkReader {

    Set<Record> bulkRead();
    Set<Record> bulkSort(Map<String, SortType> map);
}