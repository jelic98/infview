package rs.raf.infview.util.io.orm.microsoft;

import rs.raf.infview.util.io.orm.component.QueryResult;
import java.util.ArrayList;
import java.util.List;

class MicrosoftQueryResult implements QueryResult {

    private List<QueryRow> rows;

    MicrosoftQueryResult() {
        rows = new ArrayList<>();
    }

    @Override
    public void add(QueryRow row) {
        rows.add(row);
    }

    @Override
    public QueryRow get(int index) {
        return rows.get(index);
    }

    @Override
    public int getCount() {
        return rows.size();
    }
}