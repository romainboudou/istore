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
 * Represents a dialog for deleting an item.
 */
public class DeleteItem extends JDialog {
    private JTextField itemIdField;

    /**
     * Constructs a new instance of DeleteItem.
     *
     * @param parent The parent frame.
     */
    public DeleteItem(JFrame parent) {
        super(parent, "Delete Items", true);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes the components of the DeleteItem dialog.
     */
    private void initializeComponents() {
        JPanel panel = new JPanel(new FlowLayout());

        JLabel itemIdLabel = new JLabel("Item ID:");
        itemIdField = new JTextField(10);
        JButton deleteButton = new JButton("Delete");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemId = itemIdField.getText();
                try {
                    int id = Integer.parseInt(itemId);
                    deleteItemById(id);
                } catch (NumberFormatException ex) {
                    // Gérer l'exception si l'ID n'est pas un entier valide
                    JOptionPane.showMessageDialog(null, "Invalid Item ID");
                }
            }
        });

        panel.add(itemIdLabel);
        panel.add(itemIdField);
        panel.add(deleteButton);

        add(panel);
    }

    /**
     * Deletes an item by its ID.
     *
     * @param itemId The ID of the item to be deleted.
     */
    public static void deleteItemById(int itemId) {
        try (Connection conn = Configuration.getConnection()) {
            String deleteQuery = "DELETE FROM \"item\" WHERE \"id\"=?";
            PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, itemId);

            int rowsAffected = deleteStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Item with ID " + itemId + " deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Item with ID " + itemId + " not found!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting item from database: " + ex.getMessage());
        }
    }
}
