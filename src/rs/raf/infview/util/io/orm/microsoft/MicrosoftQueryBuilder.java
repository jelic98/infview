package rs.raf.infview.util.io.orm.microsoft;

import rs.raf.infview.util.io.orm.component.Matcher;
import rs.raf.infview.util.io.orm.component.Query;
import rs.raf.infview.util.io.orm.component.QueryBuilder;

class MicrosoftQueryBuilder implements QueryBuilder {

    private static final String DEFAULT_PRIMARY_KEY = "id";

    private Query query;
    private String entity;

    MicrosoftQueryBuilder() {
        this.query = new MicrosoftQuery();
    }

    @Override
    public QueryBuilder find(String entity) {
        return find(entity, null);
    }

    @Override
    public QueryBuilder find(String entity, String[] attrs) {
        this.entity = entity;

        query.append("SELECT");

        if(attrs != null && attrs.length > 0) {
            for(int i = 0; i < attrs.length; i++) {
                query.append(i > 0 ? "," : "");
                query.append(attrs[i]);
            }
        }else {
            query.append("*");
        }

        query.append("FROM");
        query.append(entity);

        return this;
    }

    @Override
    public QueryBuilder add(String entity, String[] attrs, String[] values) {
        this.entity = entity;

        if(attrs.length != values.length) {
            return this;
        }

        query.append("INSERT INTO");
        query.append(entity);
        query.append("(");

        boolean firstPrinted = false;

        for(int i = 0; i < attrs.length; i++) {
            if(values[i] == null) {
                continue;
            }

            query.append(i > 0 && firstPrinted ? "," : "");
            query.append(attrs[i]);

            firstPrinted = true;
        }

        query.append(")");
        query.append("VALUES");
        query.append("(");

        firstPrinted = false;

        for(int i = 0; i < values.length; i++) {
            if(values[i] == null) {
                continue;
            }

            query.append(i > 0 && firstPrinted ? "," : "");
            query.append(values[i], true);

            firstPrinted = true;
        }

        query.append(")");

        return this;
    }

    @Override
    public QueryBuilder change(String entity, String[] attrs, String[] values) {
        this.entity = entity;

        if(attrs.length != values.length) {
            return this;
        }

        query.append("UPDATE");
        query.append(entity);
        query.append("SET");

        boolean firstPrinted = false;

        for(int i = 0; i < attrs.length; i++) {
            if(values[i] == null) {
                continue;
            }

            query.append(i > 0 && firstPrinted ? "," : "");
            query.append(attrs[i]);
            query.append("=");
            query.append(values[i], true);

            firstPrinted = true;
        }

        return this;
    }

    @Override
    public QueryBuilder remove(String entity) {
        this.entity = entity;

        query.append("DELETE FROM");
        query.append(entity);

        return this;
    }

    @Override
    public QueryBuilder sort(String[] attrs, String[] sortTypes) {
        query.append("ORDER BY");
        
        if(attrs == null) {
        	query.append(DEFAULT_PRIMARY_KEY, true);
        	query.append(sortTypes[0]);
        	
        	return this;
        }
        
        for(int i = 0; i < attrs.length; i++) {
            query.append(i > 0 ? "," : "");
            query.append(attrs[i] + " " + sortTypes[i]);
            
        }

        return this;
    }

    @Override
    public QueryBuilder with(String remoteEntity, String remoteAttr, String currentAttr) {
        query.append("INNER JOIN");
        query.append(remoteEntity);
        query.append("ON");
        query.append(entity);
        query.append(".");
        query.append(currentAttr, true);
        query.append("=");
        query.append(remoteEntity);
        query.append(".");
        query.append(remoteAttr, true);

        return this;
    }

    @Override
    public Query raw(String query) {
        this.query.append(query);

        return this.query;
    }

    @Override
    public QueryBuilder match(Matcher matcher) {
        query.append("WHERE");
        query.append(matcher.dump());

        return this;
    }

    @Override
    public QueryBuilder group(String[] attrs) {
        query.append("GROUP BY");

        for(int i = 0; i < attrs.length; i++) {
            query.append(i > 0 ? "," : "");
            query.append(attrs[i]);
        }

        return this;
    }

    @Override
    public Query first() {
        sort(null, new String[] {"ASC"});
        query.append("LIMIT 1");

        return query;
    }

    @Override
    public Query last() {
        sort(null, new String[] {"DESC"});
        query.append("LIMIT 1");

        return query;
    }

    @Override
    public Query all() {
        return query;
    }

    @Override
    public String dump() {
        return query.dump();
    }
}