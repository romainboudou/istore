package org.admin.store;

import org.h2config.Configuration;
import org.store.StoreAppManagement;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

/**
 * Represents the management of stores, providing functionalities to add, delete, and associate stores with users.
 */
public class StoreManagement extends JFrame {

    private JTable storeTable;
    private JButton addButton, deleteButton;
    private DefaultTableModel tableModel;
    private ArrayList<Store> storeList;

    /**
     * Constructs a new instance of StoreManagement.
     */
    public StoreManagement() {
        setTitle("Store Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents();
        loadData();
        setVisible(true);
    }

    /**
     * Initializes the components of the StoreManagement interface.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());

        storeTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(storeTable);
        add(scrollPane, BorderLayout.CENTER);

        storeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = storeTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int id = (int) storeTable.getValueAt(selectedRow, 0);
                        String name = (String) storeTable.getValueAt(selectedRow, 1);
                        StoreAppManagement.main(null, id);
                        System.out.println("ID: " + id + "\nName: " + name);
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(buttonPanel, BorderLayout.SOUTH);

        addButton = new JButton("Add Store");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddStore(StoreManagement.this);
            }
        });
        buttonPanel.add(addButton);

        deleteButton = new JButton("Delete by ID");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDeleteStoreWindow();
            }
        });
        buttonPanel.add(deleteButton);

        JButton associateButton = new JButton("Associate Store to User");
        associateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StoreUserAssociation(StoreManagement.this).setVisible(true);
            }
        });
        buttonPanel.add(associateButton);
    }

    /**
     * Loads store data from the database.
     */
    public void loadData() {
        storeList = fetchDataFromDatabase();
        populateTableModel();
    }

    /**
     * Fetches store data from the database.
     *
     * @return List of stores fetched from the database.
     */
    private ArrayList<Store> fetchDataFromDatabase() {
        ArrayList<Store> storeList = new ArrayList<>();
        try (Connection conn = Configuration.getConnection()) {
            String query = "SELECT * FROM \"store\"";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                storeList.add(new Store(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching store data from database: " + e.getMessage());
        }
        return storeList;
    }

    /**
     * Populates the table model with store data.
     */
    public void populateTableModel() {
        String[] columnNames = {"ID", "Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        for (Store store : storeList) {
            Object[] rowData = {store.getId(), store.getName()};
            tableModel.addRow(rowData);
        }
        storeTable.setModel(tableModel);
    }

    /**
     * Opens the window to delete a store by ID.
     */
    private void openDeleteStoreWindow() {
        deleteStoreByID(JOptionPane.showInputDialog(this, "Enter ID of the store to delete:"));
    }

    /**
     * Deletes a store by its ID.
     *
     * @param storeId The ID of the store to delete.
     */
    private void deleteStoreByID(String storeId) {
        try {
            int storeIdInt = Integer.parseInt(storeId);

            try (Connection conn = Configuration.getConnection()) {
                String deleteQuery = "DELETE FROM \"store\" WHERE \"id\"=?";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, storeIdInt);

                int rowsAffected = deleteStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Store with ID " + storeId + " deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Store with ID " + storeId + " not found!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting store from database: " + ex.getMessage());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID entered!");
        }
    }
}
