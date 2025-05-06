package org.admin.store;

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
 * Represents the association of a store with a user.
 */
class StoreUserAssociation extends JDialog {

    /**
     * Constructs a new instance of StoreUserAssociation.
     *
     * @param parent The parent frame.
     */
    public StoreUserAssociation(JFrame parent) {
        super(parent, "Associate Store to User", true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents();
    }

    /**
     * Initializes the components of the StoreUserAssociation interface.
     */
    private void initializeComponents() {
        JLabel storeLabel = new JLabel("Store ID:");
        JTextField storeIdField = new JTextField(10);

        JLabel userLabel = new JLabel("User ID:");
        JTextField userIdField = new JTextField(10);

        JButton associateButton = new JButton("Associate");
        associateButton.addActionListener(e -> associateStoreToUser(storeIdField.getText(), userIdField.getText()));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(storeLabel, gbc);
        gbc.gridy++;
        panel.add(storeIdField, gbc);

        gbc.gridy++;
        panel.add(userLabel, gbc);
        gbc.gridy++;
        panel.add(userIdField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(associateButton, gbc);

        add(panel);
    }


    /**
     * Associates a store with a user based on the provided store and user IDs.
     *
     * @param storeId The ID of the store.
     * @param userId  The ID of the user.
     */
    private void associateStoreToUser(String storeId, String userId) {
        try {
            int storeIdInt = Integer.parseInt(storeId);
            int userIdInt = Integer.parseInt(userId);

            if (userRoleIsZero(userIdInt)) {
                if (!userIsAssociatedWithStore(userIdInt)) {
                    try (Connection conn = Configuration.getConnection()) {
                        String insertQuery = "INSERT INTO \"store_access\" (\"store_id\", \"user_id\") VALUES (?, ?)";
                        PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                        insertStatement.setInt(1, storeIdInt);
                        insertStatement.setInt(2, userIdInt);

                        insertStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Store associated to user successfully!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error associating store to user: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User already associated with a store!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "User must have role 0 to associate with a store!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID entered!");
        }
    }

    /**
     * Checks if a user is already associated with a store.
     *
     * @param userId The ID of the user.
     * @return True if the user is associated with a store, false otherwise.
     */
    private boolean userIsAssociatedWithStore(int userId) {
        try (Connection conn = Configuration.getConnection()) {
            String checkQuery = "SELECT * FROM \"store_access\" WHERE \"user_id\" = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
            checkStatement.setInt(1, userId);

            ResultSet checkResult = checkStatement.executeQuery();

            return checkResult.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking user association with store: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Checks if a user has role 0.
     *
     * @param userId The ID of the user.
     * @return True if the user has role 0, false otherwise.
     */
    private boolean userRoleIsZero(int userId) {
        try (Connection conn = Configuration.getConnection()) {
            String checkQuery = "SELECT \"role\" FROM \"user\" WHERE \"id\" = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
            checkStatement.setInt(1, userId);

            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                int role = checkResult.getInt("role");
                return role == 0;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking user role: " + ex.getMessage());
            return false;
        }
    }
}
