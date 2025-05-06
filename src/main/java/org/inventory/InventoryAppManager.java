package org.inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class InventoryAppManager extends JFrame {
    private JButton showInventoryButton, updateQuantityButton;
    private JTable inventoryTable;

    /**
     * Constructor to initialize the InventoryAppManager for a specific store.
     *
     * @param storeID The ID of the store for which the inventory is managed.
     */
    public InventoryAppManager(int storeID) {
        setTitle("Inventory");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents(storeID);
        setLayout(null);
        setVisible(true);
    }

    /**
     * Initializes the components of the inventory manager.
     *
     * @param storeID The ID of the store for which the inventory is managed.
     */
    private void initializeComponents(int storeID) {
        showInventoryButton = new JButton("Show Inventory");
        showInventoryButton.setBounds(30, 20, 150, 25);
        showInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInventory(storeID);
            }
        });
        add(showInventoryButton);

        updateQuantityButton = new JButton("Update Quantity");
        updateQuantityButton.setBounds(400, 20, 150, 25);
        updateQuantityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemIdInput = JOptionPane.showInputDialog("Enter Item ID:");
                String quantityChangeInput = JOptionPane.showInputDialog("Enter Quantity Change:");

                try {
                    int itemId = Integer.parseInt(itemIdInput);
                    int quantityChange = Integer.parseInt(quantityChangeInput);
                    InventoryDatabaseManager.updateItemQuantity(itemId, quantityChange);
                    showInventory(storeID);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating quantity: " + ex.getMessage());
                }
            }
        });
        add(updateQuantityButton);

        inventoryTable = new JTable();
        inventoryTable.setBounds(17, 70, 550, 250);
        add(inventoryTable);
    }

    /**
     * Displays the inventory for the specified store in the JTable.
     *
     * @param storeID The ID of the store for which the inventory is displayed.
     */
    private void showInventory(int storeID) {
        try {
            ResultSet resultSet = InventoryDatabaseManager.getInventoryResultSet(storeID);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Item ID");
            tableModel.addColumn("Name");
            tableModel.addColumn("Price");
            tableModel.addColumn("Quantity");

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("id"));
                row.add(resultSet.getString("name"));
                row.add(resultSet.getDouble("price"));
                row.add(resultSet.getInt("quantity"));
                tableModel.addRow(row);
            }

            inventoryTable.setModel(tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error accessing database: " + e.getMessage());
        }
    }

    /**
     * Main method to launch the InventoryAppManager.
     *
     * @param args     Command line arguments.
     * @param storeID The ID of the store for which the inventory is managed.
     */
    public static void main(String[] args, int storeID) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryAppManager(storeID);
            }
        });
    }
}
