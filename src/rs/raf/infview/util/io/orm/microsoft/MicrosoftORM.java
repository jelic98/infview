package rs.raf.infview.util.io.orm.microsoft;

import rs.raf.infview.util.io.orm.AbstractORM;
import rs.raf.infview.util.io.orm.component.*;

public class MicrosoftORM extends AbstractORM {

    @Override
    public Connection getConnection() {
        return new MicrosoftConnection();
    }

    @Override
    public Matcher getMatcher() {
        return new MicrosoftMatcher();
    }

    @Override
    public QueryBuilder getQueryBuilder() {
        return new MicrosoftQueryBuilder();
    }

    @Override
    public Transaction getTransaction() {
        return new MicrosoftTransaction();
    }
}