package org.authentication;

import org.h2config.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDatabaseManager {

    /**
     * Retrieves a connection to the database.
     *
     * @return A Connection object.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return Configuration.getConnection();
    }

    /**
     * Checks if the provided email is whitelisted.
     *
     * @param email The user's email address.
     * @return True if the email is whitelisted, false otherwise.
     */
    public static boolean isEmailWhitelisted(String email) {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM \"user\" WHERE \"email\" = ? AND \"whitelisted\" = 1";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the maximum user ID from the database.
     *
     * @return The maximum user ID.
     */
    public static int getMaxUserId() {
        try (Connection conn = getConnection()) {
            String maxIdQuery = "SELECT MAX(\"id\") FROM \"user\"";
            PreparedStatement preparedStatement = conn.prepareStatement(maxIdQuery);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Inserts a new user into the database.
     *
     * @param id The user's ID.
     * @param email The user's email address.
     * @param hashedPassword The hashed password.
     */
    public static void insertUser(int id, String email, String hashedPassword) {
        try (Connection conn = getConnection()) {
            String insertQuery = "INSERT INTO \"user\" (\"id\", \"email\", \"password\", \"role\", \"whitelisted\", \"pseudo\") VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setInt(1, id);
            insertStatement.setString(2, email);
            insertStatement.setString(3, hashedPassword);
            insertStatement.setInt(4, 0);
            insertStatement.setInt(5, 0);
            insertStatement.setString(6, "USER" + id);

            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the hashed password for a given email.
     *
     * @param email The user's email address.
     * @return The hashed password.
     */
    public static String getUserPassword(String email) {
        try (Connection conn = getConnection()) {
            String query = "SELECT \"password\" FROM \"user\" WHERE \"email\" = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the role of a user based on their email.
     *
     * @param email The user's email address.
     * @return The user's role.
     * @throws SQLException If a database access error occurs.
     */
    public static int getUserRole(String email) throws SQLException {
        try (Connection conn = getConnection()) {
            String password = getUserPassword(email);
            String query = "SELECT \"role\" FROM \"user\" WHERE \"email\" = ? AND \"password\" = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("role");
            } else {
                return -1; // User not found
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Retrieves the ID of a user based on their email.
     *
     * @param email The user's email address.
     * @return The user's ID.
     * @throws SQLException If a database access error occurs.
     */
    public static int getUserId(String email) throws SQLException {
        try (Connection conn = getConnection()) {
            String password = getUserPassword(email);
            String query = "SELECT \"id\" FROM \"user\" WHERE \"email\" = ? AND \"password\" = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1; // User not found
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}
