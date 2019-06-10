package rs.raf.infview.util.io.orm.microsoft;

import rs.raf.infview.util.io.orm.ConnectException;
import rs.raf.infview.util.io.orm.SyntaxException;
import rs.raf.infview.util.io.orm.component.Connection;
import rs.raf.infview.util.io.orm.component.Query;
import rs.raf.infview.util.io.orm.component.QueryResult;
import rs.raf.infview.util.log.Log;
import java.sql.*;

class MicrosoftConnection implements Connection {

    private java.sql.Connection connection;

    @Override
    public void open(ConnectionParams params) throws ConnectException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://" + params.getHost() + "/" + params.getDatabase(),
                    params.getUsername(), params.getPassword());
        }catch(Exception e) {
            throw new ConnectException(e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        }catch(SQLException e) {
            Log.e(e.getMessage());
        }
    }

    @Override
    public QueryResult run(Query query) throws SyntaxException {
        Log.d("Running " + query.dump());

        QueryResult result = new MicrosoftQueryResult();

        try {
            Statement statement = connection.createStatement();

            String sql = query.dump();

            if(sql.startsWith("SELECT")) {
                ResultSet set = statement.executeQuery(sql);

                extractResult(set, result);

                if(set != null) {
                    set.close();
                }
            }else {
                statement.execute(sql);
            }

            statement.close();
        }catch(SQLException e) {
            throw new SyntaxException(e.getMessage());
        }

        return result;
    }

    private void extractResult(ResultSet set, QueryResult result) throws SQLException {
        if(set == null || result == null) {
            return;
        }

        ResultSetMetaData meta = set.getMetaData();

        int cols = meta.getColumnCount();

        while(set.next()) {
            QueryResult.QueryRow row = new QueryResult.QueryRow();

            for(int i = 1; i <= cols; i++) {
                row.put(meta.getColumnName(i), set.getObject(i));
            }

            result.add(row);
        }
    }
}