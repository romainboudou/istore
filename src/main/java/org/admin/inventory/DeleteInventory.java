package org.admin.inventory;

import org.h2config.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a dialog for deleting an inventory.
 */
public class DeleteInventory extends JDialog {
    private JTextField inventoryIdField;

    /**
     * Constructs a new instance of DeleteInventory.
     *
     * @param parent The parent frame.
     */
    public DeleteInventory(JFrame parent) {
        super(parent, "Delete Inventory", true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes the components of the DeleteInventory dialog.
     */
    private void initializeComponents() {
        JPanel panel = new JPanel(new FlowLayout());

        JLabel inventoryIdLabel = new JLabel("Inventory ID:");
        inventoryIdField = new JTextField(10);
        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inventoryId = inventoryIdField.getText();
                try {
                    int id = Integer.parseInt(inventoryId);
                    deleteInventoryById(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Inventory ID");
                }
            }
        });

        panel.add(inventoryIdLabel);
        panel.add(inventoryIdField);
        panel.add(deleteButton);

        add(panel);
    }

    /**
     * Deletes an inventory by its ID.
     *
     * @param inventoryId The ID of the inventory to be deleted.
     */
    public static void deleteInventoryById(int inventoryId) {
        try (Connection conn = Configuration.getConnection()) {
            String deleteQuery = "DELETE FROM \"inventory\" WHERE \"id\"=?";
            PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, inventoryId);

            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Inventory with ID " + inventoryId + " deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Inventory with ID " + inventoryId + " not found!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting inventory from database: " + ex.getMessage());
        }
    }
}
