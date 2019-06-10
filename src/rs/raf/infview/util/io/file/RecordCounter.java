package rs.raf.infview.util.io.file;

import java.util.Map;
import java.util.Set;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.Attribute;

public interface RecordCounter {

	Set<Record> count(Map<Integer, Attribute> avgInfo);
}