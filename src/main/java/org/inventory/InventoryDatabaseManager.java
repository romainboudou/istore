package org.inventory;

import org.h2config.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDatabaseManager {

    /**
     * Retrieves the ResultSet containing inventory information for a specific store.
     *
     * @param storeId The ID of the store for which the inventory information is retrieved.
     * @return ResultSet containing inventory information.
     * @throws SQLException If an SQL exception occurs.
     */
    public static ResultSet getInventoryResultSet(int storeId) throws SQLException {
        Connection conn = null;
        try {
            conn = Configuration.getConnection();
            String query = "SELECT * FROM \"item\" WHERE \"inventory_id\" IN (SELECT \"id\" FROM \"inventory\" WHERE \"store_id\" = ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, storeId);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Updates the quantity of an item in the inventory.
     *
     * @param itemId         The ID of the item to be updated.
     * @param quantityChange The change in quantity (positive for increase, negative for decrease).
     * @throws SQLException If an SQL exception occurs.
     */
    public static void updateItemQuantity(int itemId, int quantityChange) throws SQLException {
        Connection conn = null;
        try {
            conn = Configuration.getConnection();
            String query = "SELECT * FROM \"item\" WHERE \"id\" = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, itemId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (quantityChange >= 0) {
                    String updateQuery = "UPDATE \"item\" SET \"quantity\" = ? WHERE \"id\" = ?";
                    PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                    updateStatement.setInt(1, quantityChange);
                    updateStatement.setInt(2, itemId);

                    updateStatement.executeUpdate();
                } else {
                    throw new SQLException("Cannot decrease quantity below 0!");
                }
            } else {
                throw new SQLException("Item not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
