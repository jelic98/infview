package rs.raf.infview.util.io.orm.component;

public interface QueryBuilder {

    enum SortType {
        ASCENDING,
        DESCENDING
    }

    QueryBuilder find(String entity);
    QueryBuilder find(String entity, String[] attrs);
    QueryBuilder add(String entity, String[] attrs, String[] values);
    QueryBuilder change(String entity, String[] attrs, String[] values);
    QueryBuilder remove(String entity);
    QueryBuilder sort(String[] attrs, String[] sortTypes);
    QueryBuilder with(String remoteEntity, String remoteAttr, String currentAttr);
    QueryBuilder match(Matcher matcher);
    QueryBuilder group(String[] attrs);
    Query raw(String query);
    Query first();
    Query last();
    Query all();
    String dump();
}