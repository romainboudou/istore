package org.admin.store;

import org.h2config.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Dialog for adding a new store.
 */
public class AddStore extends JDialog {

    private JLabel nameLabel;
    private JTextField nameField;
    private JButton saveButton;
    private StoreManagement storeManagement;

    /**
     * Constructor for the AddStore dialog.
     *
     * @param storeManagement The parent StoreManagement instance.
     */
    public AddStore(StoreManagement storeManagement) {
        this.storeManagement = storeManagement;
        setTitle("Add Store");
        setSize(600, 400);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes the components of the dialog.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        nameLabel = new JLabel("Name:");
        inputPanel.add(nameLabel);
        nameField = new JTextField(15);
        inputPanel.add(nameField);

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStore();
            }
        });

        add(inputPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    /**
     * Saves the new store to the database.
     */
    private void saveStore() {
        String name = nameField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name for the store !");
            return;
        }

        try (Connection conn = Configuration.getConnection()) {
            int nextId = 0;
            String maxIdQuery = "SELECT MAX(\"id\") FROM \"store\"";
            PreparedStatement maxIdStatement = conn.prepareStatement(maxIdQuery);
            java.sql.ResultSet maxIdResult = maxIdStatement.executeQuery();
            if (maxIdResult.next()) {
                nextId = maxIdResult.getInt(1) + 1;
            }

            String insertQuery = "INSERT INTO \"store\" (\"id\", \"name\") VALUES (?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setInt(1, nextId);
            insertStatement.setString(2, name);
            insertStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Store saved successfully!");

            storeManagement.loadData();
            storeManagement.populateTableModel();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving store to database: " + ex.getMessage());
        }

        dispose();
    }
}
