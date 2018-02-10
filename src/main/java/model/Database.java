package model;

import java.sql.Connection;

public class Database {

    private static Connection connection;

    public static void setConnection(final Connection connection) {
        Database.connection = connection;
    }

    public static Connection getConnection() {
        return connection;
    }

}
