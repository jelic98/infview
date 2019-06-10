package rs.raf.infview.util.io.orm.component;

import java.util.LinkedHashMap;
import java.util.Map;

public interface QueryResult {

    void add(QueryRow row);
    QueryRow get(int index);
    int getCount();

    class QueryRow {

        private Map<String, Object> values;

        public QueryRow() {
            values = new LinkedHashMap<>();
        }

        public void put(String attr, Object value) {
            values.put(attr, value);
        }

        public Map<String, Object> getValues() {
            return values;
        }
    }
}