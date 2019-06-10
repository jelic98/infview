package rs.raf.infview.model.comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Record;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.AbstractResource.SortType;

public class SortRecordComparator implements Comparator<Record>{

	private Map<String, AbstractResource.SortType> sortInfo;
	
	public SortRecordComparator(Map<String, AbstractResource.SortType> sortInfo) {
		super();
		this.sortInfo = sortInfo;
	}
	
	public SortRecordComparator(ArrayList<Attribute> attrs) {
		super();
		sortInfo = new HashMap<>();
		for(Attribute a : attrs)
			sortInfo.put(a.getName(), SortType.ASCENDING);
	}

	@Override
	public int compare(Record o1, Record o2) {
		for(String key : sortInfo.keySet()) {
            for (Iterator iterator = o1.getAttributes().iterator(); iterator.hasNext();) {
				Attribute a = (Attribute) iterator.next();
				if(!a.getName().equals(key))
					continue;
				
                String value1 = o1.get(key);
                String value2 = o2.get(key);

                if(value1 == null || value2 == null) {
                    continue;
                }

                int valueCompare = a.getType().getComparator().on(a.getName()).compare(o1, o2);
                
                if(valueCompare != 0) {
                	if(sortInfo.get(a.getName()) == SortType.DESCENDING)
                		valueCompare *= -1;
                    return valueCompare;
                }
            }
        }

        return 0;
	}

}
