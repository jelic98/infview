package rs.raf.infview.util.io.orm.component;

import rs.raf.infview.util.io.orm.ConnectException;
import rs.raf.infview.util.io.orm.SyntaxException;

public interface Connection {

    void open(ConnectionParams params) throws ConnectException;
    void close();
    QueryResult run(Query query) throws SyntaxException;

    class ConnectionParams {

        private String host, database, username, password;

        public ConnectionParams(String host, String database, String username, String password) {
            this.host = host;
            this.database = database;
            this.username = username;
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public String getDatabase() {
            return database;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}