package rs.raf.infview.util.io.orm.microsoft;

import rs.raf.infview.util.io.orm.component.Query;
import rs.raf.infview.util.io.orm.component.Transaction;
import java.util.List;

class MicrosoftTransaction extends MicrosoftQuery implements Transaction {

    @Override
    public Transaction wrap(List<Query> queries) {
        builder.append("BEGIN TRANSACTION;");

        for(Query query : queries) {
            builder.append(query.dump());
            builder.append(";");
        }

        builder.append("COMMIT;");

        return this;
    }
}