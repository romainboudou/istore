package org.admin.inventory;

import org.h2config.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a dialog for linking inventory to a store.
 */
public class LinkInventoryToStore extends JDialog {
    private JTextField inventoryIdField, storeIdField;
    private JButton linkButton;

    /**
     * Constructs a new instance of LinkInventoryToStore.
     *
     * @param parent The parent frame.
     */
    public LinkInventoryToStore(JFrame parent) {
        super(parent, "Link Inventory to Store", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes the components of the dialog.
     */
    private void initializeComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel inventoryIdLabel = new JLabel("Inventory ID:");
        inventoryIdField = new JTextField(10);
        panel.add(inventoryIdLabel);
        panel.add(inventoryIdField);

        JLabel storeIdLabel = new JLabel("Store ID:");
        storeIdField = new JTextField(10);
        panel.add(storeIdLabel);
        panel.add(storeIdField);

        linkButton = new JButton("Link");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(linkButton);

        panel.add(buttonPanel);

        add(panel);
    }

    /**
     * Links the inventory to the store.
     */
    private void linkInventoryToStore() {
        try (Connection conn = Configuration.getConnection()) {
            int inventoryId = Integer.parseInt(inventoryIdField.getText());
            int storeId = Integer.parseInt(storeIdField.getText());

            if (inventoryExistsForStore(conn, storeId)) {
                JOptionPane.showMessageDialog(this, "An inventory already exists for this store!");
                return;
            }

            String insertQuery = "INSERT INTO \"inventory\" (\"id\", \"store_id\") VALUES (?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setInt(1, inventoryId);
            insertStatement.setInt(2, storeId);

            insertStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Inventory linked to store successfully!");

            dispose();
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error linking inventory to store: " + ex.getMessage());
        }
    }

    /**
     * Checks if an inventory already exists for the specified store.
     *
     * @param conn    The database connection.
     * @param storeId The ID of the store.
     * @return True if an inventory already exists for the store, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    private boolean inventoryExistsForStore(Connection conn, int storeId) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM \"inventory\" WHERE \"store_id\" = ?";
        PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
        checkStatement.setInt(1, storeId);
        ResultSet resultSet = checkStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }
}
