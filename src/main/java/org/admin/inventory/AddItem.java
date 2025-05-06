package org.admin.inventory;

import org.h2config.Configuration;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Represents a dialog for adding a new item to the inventory.
 */
public class AddItem extends JDialog {
    private JTextField nameField, priceField, quantityField, inventoryIdField;

    /**
     * Constructs a new instance of AddItem.
     *
     * @param parent The parent frame.
     */
    public AddItem(JFrame parent) {
        super(parent, "Add New Item", true);
        setSize(600, 400);
        initializeComponents();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Initializes the components of the AddItem dialog.
     */
    private void initializeComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField(20);
        panel.add(priceLabel);
        panel.add(priceField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(20);
        panel.add(quantityLabel);
        panel.add(quantityField);

        JLabel inventoryIdLabel = new JLabel("Inventory ID:");
        inventoryIdField = new JTextField(20);
        panel.add(inventoryIdLabel);
        panel.add(inventoryIdField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addItem());
        panel.add(addButton);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(panel);

        add(centerPanel);
    }

    /**
     * Adds a new item to the inventory based on the entered details.
     */
    private void addItem() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        int inventoryId = Integer.parseInt(inventoryIdField.getText());

        try (Connection conn = Configuration.getConnection()) {
            int nextId = 0;
            String maxIdQuery = "SELECT MAX(\"id\") FROM \"item\"";
            Statement maxIdStatement = conn.createStatement();
            ResultSet maxIdResult = maxIdStatement.executeQuery(maxIdQuery);
            if (maxIdResult.next()) {
                nextId = maxIdResult.getInt(1) + 1;
            }

            String insertQuery = "INSERT INTO \"item\" (\"id\", \"name\", \"price\", \"quantity\", \"inventory_id\") VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setInt(1, nextId);
            insertStatement.setString(2, name);
            insertStatement.setDouble(3, price);
            insertStatement.setInt(4, quantity);
            insertStatement.setInt(5, inventoryId);

            insertStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item added successfully!");

            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding item to database: " + ex.getMessage());
        }
    }
}
