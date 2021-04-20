package com.wau.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectorAPIImpl implements DatabaseConnectorAPI {

    private static DatabaseConnectorAPI databaseConnectorAPI = null;
    private Connection connection;
    private Statement statement;
    private static final String DATABASE_URL = "database_url";
    private static final String DATABASE_USERNAME = "database_username";
    private static final String DATABASE_PASSWORD = "database_password";

    public static DatabaseConnectorAPI instance() {
        if (null == databaseConnectorAPI) {
            databaseConnectorAPI = new DatabaseConnectorAPIImpl();
        }
        return databaseConnectorAPI;
    }

    @Override
    public void getConnection() throws SQLException {
        if (System.getenv(DATABASE_URL) == null) {
            throw new SQLException("Unable to establish connection with database");
        } else {
            connection =
                    DriverManager.getConnection(System.getenv(DATABASE_URL),
                            System.getenv(DATABASE_USERNAME),
                            System.getenv(DATABASE_PASSWORD));
        }
    }

    public void clearResources() throws SQLException {
        if (null == connection) {
            getConnection();
        }
        if (null == statement) {
            statement = connection.createStatement();
        }
        statement.close();
        connection.close();
    }

    @Override
    public Statement getStatement() throws SQLException {
        return connection.createStatement();
    }

}
