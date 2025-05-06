package org.store;

import org.h2config.Configuration;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDatabaseManager {

    /**
     * Retrieves the name of a store based on its ID.
     *
     * @param storeId The ID of the store.
     * @return The name of the store.
     */
    public static String getStoreNameById(int storeId) {
        try (Connection conn = Configuration.getConnection()) {
            String storeNameQuery = "SELECT \"name\" FROM \"store\" WHERE \"id\" = ?";
            PreparedStatement storeNameStatement = conn.prepareStatement(storeNameQuery);
            storeNameStatement.setInt(1, storeId);

            ResultSet storeNameResult = storeNameStatement.executeQuery();

            if (storeNameResult.next()) {
                return storeNameResult.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving store name: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the ID of the store associated with a user.
     *
     * @param userId The ID of the user.
     * @return The ID of the associated store.
     */
    public static int getStoreIdForUser(int userId) {
        try (Connection conn = Configuration.getConnection()) {
            String storeQuery = "SELECT \"store_id\" FROM \"store_access\" WHERE \"user_id\" = ?";
            PreparedStatement storeStatement = conn.prepareStatement(storeQuery);
            storeStatement.setInt(1, userId);

            ResultSet storeResult = storeStatement.executeQuery();

            if (storeResult.next()) {
                return storeResult.getInt("store_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving store_id for user: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves information about users associated with a store.
     *
     * @param storeId The ID of the store.
     * @return A list of user information as Object arrays.
     */
    public static List<Object[]> getUsersForStore(int storeId) {
        List<Object[]> users = new ArrayList<>();
        try (Connection conn = Configuration.getConnection()) {
            String userQuery = "SELECT u.\"id\", u.\"email\", u.\"pseudo\", u.\"role\" FROM \"user\" u " +
                    "JOIN \"store_access\" sa ON u.\"id\" = sa.\"user_id\" " +
                    "WHERE sa.\"store_id\" = ?";
            PreparedStatement userStatement = conn.prepareStatement(userQuery);
            userStatement.setInt(1, storeId);

            ResultSet userResult = userStatement.executeQuery();

            while (userResult.next()) {
                int userIdResult = userResult.getInt("id");
                String userEmail = userResult.getString("email");
                String userName = userResult.getString("pseudo");
                int userRole = userResult.getInt("role");

                users.add(new Object[]{userIdResult, userEmail, userName, userRole});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving users for store: " + e.getMessage());
        }
        return users;
    }
}
