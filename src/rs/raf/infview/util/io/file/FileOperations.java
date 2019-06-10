package rs.raf.infview.util.io.file;

import rs.raf.infview.model.Record;
import java.util.List;
import java.util.Map;

interface FileOperations {

    boolean create(Record record);
    Record read();
    boolean update(Record oldRecord, Record newRecord);
    boolean delete(Record record);
    List<Record> search(Record record, boolean single);
    void sort(Map<String, AbstractResource.SortType> map);
    AbstractResource convert();
}