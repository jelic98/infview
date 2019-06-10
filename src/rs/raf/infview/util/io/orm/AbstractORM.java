package rs.raf.infview.util.io.orm;

import rs.raf.infview.util.io.orm.component.*;

public abstract class AbstractORM {

    public abstract Connection getConnection();
    public abstract Matcher getMatcher();
    public abstract QueryBuilder getQueryBuilder();
    public abstract Transaction getTransaction();
}