package org.h2config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Configuration {

    private static final String JDBC_URL = "jdbc:h2:./ressources/istore";

    /**
     * Get a connection to the H2 database.
     *
     * @return A connection to the database.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, "root", "");
    }
}
