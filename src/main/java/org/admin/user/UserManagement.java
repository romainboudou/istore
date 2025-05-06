package org.admin.user;

import org.h2config.Configuration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * Represents the user management functionality in the admin panel.
 */
public class UserManagement extends JFrame {
    private JTable userTable;
    private JButton newUserButton, saveButton, deleteButton;
    private DefaultTableModel tableModel;
    protected ArrayList<User> userList;

    /**
     * Initializes a new instance of the UserManagement class.
     */
    public UserManagement() {
        setTitle("User Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents();
        loadData();
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        userTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(buttonPanel, BorderLayout.SOUTH);

        newUserButton = new JButton("New");
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewUserWindow();
            }
        });
        buttonPanel.add(newUserButton);

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });
        buttonPanel.add(saveButton);

        deleteButton = new JButton("Delete by ID");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRowByID();
            }
        });
        buttonPanel.add(deleteButton);

        JButton whitelistButton = new JButton("Whitelist by ID");
        whitelistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whitelistUserByID();
            }
        });
        buttonPanel.add(whitelistButton);

        JButton unwhitelistButton = new JButton("Unwhitelist by ID");
        unwhitelistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unwhitelistUserByID();
            }
        });
        buttonPanel.add(unwhitelistButton);

        JButton updateRoleButton = new JButton("Update Role by ID");
        updateRoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoleByID();
            }
        });
        buttonPanel.add(updateRoleButton);
    }

    /**
     * Loads user data by fetching it from the database and populating the table model.
     */
    private void loadData() {
        userList = fetchDataFromDatabase();
        populateTableModel();
    }

    /**
     * Updates the user list with the provided list and refreshes the table model.
     * @param updatedList The updated list of users.
     */
    public void updateUserList(ArrayList<User> updatedList) {
        userList = updatedList;
        populateTableModel();
    }

    /**
     * Fetches user data from the database.
     * @return The list of users fetched from the database.
     */
    private ArrayList<User> fetchDataFromDatabase() {
        ArrayList<User> userList = new ArrayList<>();
        try (Connection conn = Configuration.getConnection()) {
            String query = "SELECT * FROM \"user\"";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String name = resultSet.getString("pseudo");
                int role = resultSet.getInt("role");
                int whitelisted = resultSet.getInt("whitelisted");
                userList.add(new User(id, email, name, role, whitelisted));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching user data from database: " + e.getMessage());
        }
        return userList;
    }

    /**
     * Populates the table model with user data.
     */
    private void populateTableModel() {
        String[] columnNames = {"ID", "Email", "Pseudo", "Role", "Whitelisted"};
        tableModel = new DefaultTableModel(columnNames, 0);
        for (User user : userList) {
            Object[] rowData = {user.getId(), user.getEmail(), user.getPseudo(), user.getRole(), user.getWhitelisted()};
            tableModel.addRow(rowData);
        }
        userTable.setModel(tableModel);
    }

    /**
     * Opens a new user window for creating a new user.
     */
    private void openNewUserWindow() {
        NewUserManagement newUserWindow = new NewUserManagement(this);
        newUserWindow.setVisible(true);
    }

    /**
     * Saves changes made to user data back to the database.
     */
    private void saveChanges() {
        try (Connection conn = Configuration.getConnection()) {
            for (User user : userList) {
                String updateQuery = "UPDATE \"user\" SET \"email\"=?, \"pseudo\"=?, \"role\"=?, \"whitelisted\"=? WHERE \"id\"=?";
                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                updateStatement.setString(1, user.getEmail());
                updateStatement.setString(2, user.getPseudo());
                updateStatement.setInt(3, user.getRole());
                updateStatement.setInt(4, user.getWhitelisted());
                updateStatement.setInt(5, user.getId());
                updateStatement.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Changes saved successfully!");
            userList = fetchDataFromDatabase();
            populateTableModel();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving changes to database: " + ex.getMessage());
        }
    }

    /**
     * Deletes a row from the database based on the user ID.
     */
    private void deleteRowByID() {
        String input = JOptionPane.showInputDialog(this, "Enter ID of the row to delete:");
        try {
            int id = Integer.parseInt(input);
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete row with ID " + id + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try (Connection conn = Configuration.getConnection()) {
                    String deleteQuery = "DELETE FROM \"user\" WHERE \"id\"=?";
                    PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, id);
                    deleteStatement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Row with ID " + id + " deleted successfully!");
                    userList = fetchDataFromDatabase();
                    populateTableModel();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting row from database: " + ex.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID entered!");
        }
    }

    /**
     * Whitelists a user based on the provided user ID.
     */
    private void whitelistUserByID() {
        String input = JOptionPane.showInputDialog(this, "Enter ID of the user to whitelist:");
        try {
            int id = Integer.parseInt(input);
            int result = JOptionPane.showConfirmDialog(this, "Whitelist user with ID " + id + "?", "Confirm Whitelisting", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try (Connection conn = Configuration.getConnection()) {
                    String updateQuery = "UPDATE \"user\" SET \"whitelisted\"=1 WHERE \"id\"=?";
                    PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                    updateStatement.setInt(1, id);
                    updateStatement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User with ID " + id + " whitelisted successfully!");
                    userList = fetchDataFromDatabase();
                    populateTableModel();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error whitelisting user: " + ex.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID entered!");
        }
    }

    /**
     * Unwhitelists a user based on the provided user ID.
     */
    private void unwhitelistUserByID() {
        String input = JOptionPane.showInputDialog(this, "Enter ID of the user to unwhitelist:");
        try {
            int id = Integer.parseInt(input);
            int result = JOptionPane.showConfirmDialog(this, "Remove user with ID " + id + " from whitelist?", "Confirm Unwhitelisting", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try (Connection conn = Configuration.getConnection()) {
                    String updateQuery = "UPDATE \"user\" SET \"whitelisted\"=0 WHERE \"id\"=?";
                    PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                    updateStatement.setInt(1, id);
                    updateStatement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User with ID " + id + " removed from whitelist successfully!");
                    userList = fetchDataFromDatabase();
                    populateTableModel();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error removing user from whitelist: " + ex.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID entered!");
        }
    }

    /**
     * Updates the role of a user based on the provided user ID.
     */
    private void updateRoleByID() {
        String input = JOptionPane.showInputDialog(this, "Enter ID of the user to update role:");
        try {
            int id = Integer.parseInt(input);
            int result = JOptionPane.showConfirmDialog(this, "Set role to Admin (Yes) or User (No) for user with ID " + id + "?", "Update Role", JOptionPane.YES_NO_OPTION);
            int role = result == JOptionPane.YES_OPTION ? 1 : 0;
            if (result != JOptionPane.CLOSED_OPTION) {
                try (Connection conn = Configuration.getConnection()) {
                    String updateQuery = "UPDATE \"user\" SET \"role\"=? WHERE \"id\"=?";
                    PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                    updateStatement.setInt(1, role);
                    updateStatement.setInt(2, id);
                    updateStatement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Role updated successfully for user with ID " + id);
                    userList = fetchDataFromDatabase();
                    populateTableModel();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating role: " + ex.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID entered!");
        }
    }



}
