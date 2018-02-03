package model;

import org.hsqldb.Database;
import org.hsqldb.Table;

import java.sql.Connection;

public class Base {

    private static Connection connection;

    private static Table table;

    private static Database database;

    public static Database getDatabase() {
        return database;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static Table getTable() {
        return table;
    }

}
