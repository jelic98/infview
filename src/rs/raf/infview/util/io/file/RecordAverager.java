package rs.raf.infview.util.io.file;

import java.util.Map;
import java.util.Set;

import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Record;

public interface RecordAverager {
	
	Set<Record> average(Map<Integer, Attribute> avgInfo);
}
