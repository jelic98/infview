package rs.raf.infview.util.io.file;

import rs.raf.infview.core.App;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.comparator.SortRecordComparator;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.options.DatabaseOptions;
import rs.raf.infview.model.type.AttributeType;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.orm.ConnectException;
import rs.raf.infview.util.io.orm.OrmException;
import rs.raf.infview.util.io.orm.SyntaxException;
import rs.raf.infview.util.io.orm.component.Connection;
import rs.raf.infview.util.io.orm.component.Matcher;
import rs.raf.infview.util.io.orm.component.Query;
import rs.raf.infview.util.io.orm.component.QueryResult;
import rs.raf.infview.util.log.Log;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.frame.ResultFrame;

import java.util.*;

public class RelationalDatabase extends AbstractResource<DatabaseOptions> implements BulkReader, RecordCounter, RecordAverager {

    private Connection c;

    @Override
    public boolean create(Record record) {
    	try {
            c = App.ORM.getConnection();
            c.open(new Connection.ConnectionParams(
                    options.getHost(),
                    options.getDatabase(),
                    options.getUsername(),
                    options.getPassword()));

            Map<String, String> data = record.getData();

            String[] attrs = new String[data.size()];
            String[] values = new String[data.size()];
            
            int i = 0;
            
            for(String attr : data.keySet()) {
            	attrs[i] = attr;
                values[i++] = data.get(attr);
            }
            
            c.run(App.ORM.getQueryBuilder()
                    .add(entity, attrs, values)
                    .all());
        }catch(OrmException e) {
            DialogAdapter.error(e.getMessage());
            return false;
        }
        
        return true;
    }

    @Override
    public Set<Record> bulkRead() {
        QueryResult result = null;

        try {
            result = c.run(App.ORM.getQueryBuilder()
                    .find(entity)
                    .all());
        }catch(SyntaxException e) {
            DialogAdapter.error(e.getMessage());

        }

        return extractRecords(result, null);
    }

    @Override
	public Set<Record> bulkSort(Map<String, SortType> map) {
    	Set<Record> records = new TreeSet<>(new SortRecordComparator(map));
    	
    	ArrayList<String> attrs = new ArrayList<>();
    	ArrayList<String> types = new ArrayList<>();
    	
    	for(String key : map.keySet()) {
    		String type = map.get(key) == SortType.ASCENDING ? "ASC" : (map.get(key) == SortType.DESCENDING ? "DESC" : null);
    		if(type != null && !type.isEmpty()) {
    			attrs.add(key);
    			types.add(type);
    		}
    	}
    	
    	String[] sortAttrs = new String[attrs.size()];
    	String[] sortTypes = new String[types.size()];
    	
    	sortAttrs = attrs.toArray(sortAttrs);
    	sortTypes = types.toArray(sortTypes);
    	 
    	try {
            QueryResult result = c.run(App.ORM.getQueryBuilder()
                    .find(entity, null)
                    .sort(sortAttrs, sortTypes)
                    .all());

            records.addAll(extractRecords(result, null));
        }catch(OrmException e) {
            DialogAdapter.error(e.getMessage());
        }
    	return records;
	}

	@Override
    public boolean update(Record oldRecord, Record newRecord) {
        try {
            Matcher matcher = null;

            Map<String, String> primaries = oldRecord.getPrimaries();
            
            for(String attr : primaries.keySet()) {
            	String value = primaries.get(attr);
            	Matcher nextMatcher = App.ORM.getMatcher();
            	
            	nextMatcher.isEqual(attr, value);

            	if(matcher == null) {
            	    matcher = nextMatcher;
                }else {
                    matcher.and(nextMatcher);
                }
            }

            Map<String, String> data = newRecord.getData();

            String[] attrs = new String[data.size()];
            String[] values = new String[data.size()];

            int i = 0;
            
            for(String attr : data.keySet()) {
            	attrs[i] = attr;
            	values[i++] = data.get(attr);
            }
            
            c.run(App.ORM.getQueryBuilder()
                    .change(entity, attrs, values)
                    .match(matcher)
                    .all());
        }catch(OrmException e) {
            DialogAdapter.error(e.getMessage());
            return false;
        }
        
        return true;
    }

    @Override
    public List<Record> search(Record record, boolean single) {
        List<Record> records = new LinkedList<>();

        findFilterRecord(record, records);

        return records;
    }

    @Override
    public void sort(Map<String, SortType> map) {
    	
    }

    @Override
    public AbstractResource convert() {
        return this;
    }

    private void findFilterRecord(Record record, List<Record> records) {
        try {
            Matcher matcher = null;

            for(String attr : record.getData().keySet()) {
                Matcher nextMatcher = App.ORM.getMatcher();

                String value = record.get(attr);

                if(value == null) {
                    continue;
                }

                if(value.startsWith(">") && isNumber(value.substring(1))) {
                    nextMatcher.isGreater(attr, Float.parseFloat(value.substring(1)));
                }else if(value.startsWith(">=") && isNumber(value.substring(2))) {
                    nextMatcher.isGreaterOrEqual(attr, Float.parseFloat(value.substring(2)));
                }else if(value.startsWith("<") && isNumber(value.substring(1))) {
                    nextMatcher.isLess(attr, Float.parseFloat(value.substring(1)));
                }else if(value.startsWith("<=") && isNumber(value.substring(2))) {
                    nextMatcher.isLessOrEqual(attr, Float.parseFloat(value.substring(2)));
                }else if(value.startsWith("%")) {
                    nextMatcher.endsWith(attr, value.substring(1));
                }else if(value.endsWith("%")) {
                    nextMatcher.startsWith(attr, value.substring(0, value.length() - 1));
                }else if(value.startsWith("%") && value.endsWith("%")) {
                    nextMatcher.contains(attr, value.substring(1, value.length() - 1));
                }else if(isNumber(value)) {
                    nextMatcher.isEqual(attr, Float.parseFloat(value));
                }else {
                    nextMatcher.isEqual(attr, value);
                }

                if(matcher == null) {
                    matcher = nextMatcher;
                }else {
                    matcher.and(nextMatcher);
                }
            }

            QueryResult result = c.run(App.ORM.getQueryBuilder()
                    .find(entity)
                    .match(matcher)
                    .all());

            records.addAll(extractRecords(result, null));
        }catch(OrmException e) {
            DialogAdapter.error(e.getMessage());
        }
    }

    @Override
    public void open(String entity, Set<Attribute> attributes, DatabaseOptions options) {
        super.open(entity, attributes, options);

        try {
            c = App.ORM.getConnection();
            c.open(new Connection.ConnectionParams(
                    options.getHost(),
                    options.getDatabase(),
                    options.getUsername(),
                    options.getPassword()));
        }catch(ConnectException e) {
            DialogAdapter.error(e.getMessage());
        }
    }

    private Set<Record> extractRecords(QueryResult result, ArrayList<Attribute> comp) {
        Set<Record> records;
        if(comp != null)
        	records = new TreeSet<>(new SortRecordComparator(comp));
        else records = new TreeSet<>();

        if(result != null) {
            for(int i = 0; i < result.getCount(); i++) {
                Record resultRecord = new Record();
                resultRecord.setAttributes(attributes);

                Map<String, Object> row = result.get(i).getValues();

                for(String attr : row.keySet()) {
                    resultRecord.set(attr, row.get(attr).toString());
                }

                records.add(resultRecord);
            }
        }

        return records;
    }

    @Override
	public Set<Record> count(Map<Integer, Attribute> avgInfo) {
		Set<Record> records = new TreeSet<>();
    	
    	StringBuilder builder = new StringBuilder();
    	String countatt = null;
    	
    	for(Integer i : avgInfo.keySet()) {
    		if(i == 0)
    			countatt = avgInfo.get(i).getName();
    		else {
	    		builder.append(avgInfo.get(i).getName());
	    		builder.append(",");
    		}
    	}
    	
    	String attrs = builder.toString();
    	if(!attrs.isEmpty())
    		attrs = attrs.substring(0, attrs.length() - 1);
    	 
    	try {
    		QueryResult result;
    		if(attrs.isEmpty()) {
    			result = c.run(App.ORM.getQueryBuilder()
                        .find(entity, new String[]{"COUNT(" + countatt + ") as COUNT"})
                        .all());
    		}else {
    			result = c.run(App.ORM.getQueryBuilder()
                    .find(entity, new String[]{"COUNT(" + countatt + ") as COUNT"})
                    .group(new String[] {attrs}).all());
    		}
            records.addAll(extractRecords(result, null));
        }catch(OrmException e) {
            DialogAdapter.error(e.getMessage());
        }
    	
		return records;
	}
    
	@Override
	public Set<Record> average(Map<Integer, Attribute> avgInfo) {
		ArrayList<Attribute> comp = new ArrayList<>();
		for(Integer key : avgInfo.keySet())
			if(key != 0)
				comp.add(avgInfo.get(key));
		
		Set<Record> records = new TreeSet<>(new SortRecordComparator(comp));
		
    	StringBuilder builder = new StringBuilder();
    	String avg = null;
    	
    	for(Integer i : avgInfo.keySet()) {
    		if(i == 0 && (avgInfo.get(i).getType() != AttributeType.INTEGER && avgInfo.get(i).getType() != AttributeType.FLOAT))
    			continue;
    		if(i == 0)
    			avg = avgInfo.get(i).getName();
    		else {
	    		builder.append(avgInfo.get(i).getName());
	    		builder.append(",");
    		}
    	}
    	
    	String attrs = builder.toString();
    	if(!attrs.isEmpty())
    		attrs = attrs.substring(0, attrs.length() - 1);
    	 
    	try {
    		QueryResult result;
    		if(attrs.isEmpty()) {
    			result = c.run(App.ORM.getQueryBuilder()
                        .find(entity, new String[]{"AVG(" + avg + ") as AVERAGE"})
                        .all());
    		}else {
    			result = c.run(App.ORM.getQueryBuilder()
                    .find(entity, new String[]{attrs + ",AVG(" + avg + ") as AVERAGE"})
                    .group(new String[] {attrs}).all());
    		}
            records.addAll(extractRecords(result, comp));
        }catch(OrmException e) {
            DialogAdapter.error(e.getMessage());
        }
    	
		return records;
	}
	
	@Override
    public void close() {
        if(c != null) {
            c.close();
        }
    }

    @Override
    public Record read() {
        return null;
    }

    @Override
    public boolean delete(Record record) {
        return false;
    }

    @Override
    protected int getTotalRecords() {
        return 0;
    }

    @Override
    protected int getRecordSize() {
        return 0;
    }

    private boolean isNumber(String value) {
        try {
            Float.parseFloat(value);
        }catch(NumberFormatException e) {
            return false;
        }

        return true;
    }
}