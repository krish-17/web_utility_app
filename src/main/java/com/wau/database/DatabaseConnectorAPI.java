package com.wau.database;

import java.sql.SQLException;
import java.sql.Statement;

public interface DatabaseConnectorAPI {

    Statement getStatement() throws SQLException;

    void clearResources() throws SQLException;

    void getConnection() throws SQLException;

}
