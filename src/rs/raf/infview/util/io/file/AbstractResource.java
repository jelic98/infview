package rs.raf.infview.util.io.file;

import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.options.ResourceOptions;
import rs.raf.infview.util.io.file.AbstractResource.SortType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractResource<T extends ResourceOptions> implements FileOperations {

    protected String entity;
    protected Set<Attribute> attributes;
    protected T options;
    protected SearchType searchType;

    public enum SearchType {
        LINEAR_SEARCH,
        LINEAR_SEARCH_FROM_CURRENT,
        BINARY_SEARCH
    }

    public enum InfoEntry {
        TOTAL_RECORDS,
        RECORD_SIZE
    }

    public enum SortType {
    	ASCENDING,
    	DESCENDING
    }

    public void open(String entity, Set<Attribute> attributes, T options) {
        this.entity = entity;
        this.attributes = attributes;
        this.options = options;
    }
    public abstract void close();

    protected abstract int getTotalRecords();
    protected abstract int getRecordSize();

    public Map<InfoEntry, Integer> getInfo() {
        Map<InfoEntry, Integer> info = new HashMap<>();

        info.put(InfoEntry.TOTAL_RECORDS, getTotalRecords());
        info.put(InfoEntry.RECORD_SIZE, getRecordSize());
        
        return info;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }
}