package rs.raf.infview.util.io.orm.component;

import java.util.List;

public interface Transaction extends Query {

    Transaction wrap(List<Query> queries);
}