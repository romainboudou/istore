package org.admin.user;

import org.h2config.Configuration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.awt.FlowLayout;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NewUserManagement extends JFrame {
    private JTextField emailField, pseudoField;
    private JCheckBox whitelistedCheckBox;
    private JButton saveButton;

    private UserManagement userManagement;

    /**
     * Creates a new instance of the NewUserManagement window.
     * @param userManagement The parent UserManagement window.
     */
    public NewUserManagement(UserManagement userManagement) {
        this.userManagement = userManagement;

        setTitle("New User");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes the GUI components of the NewUserManagement window.
     */
    private void initializeComponents() {
        setLayout(new FlowLayout());

        JLabel emailLabel = new JLabel("Email:");
        add(emailLabel);

        emailField = new JTextField(15);
        add(emailField);

        JLabel nameLabel = new JLabel("Pseudo:");
        add(nameLabel);

        pseudoField = new JTextField(15);
        add(pseudoField);

        whitelistedCheckBox = new JCheckBox("Whitelisted");
        add(whitelistedCheckBox);

        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(15);
        add(passwordField);

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                saveUser(emailField.getText(), pseudoField.getText(), password, whitelistedCheckBox.isSelected());
            }
        });
        add(saveButton);
    }

    /**
     * Saves the new user to the database and updates the user list in the parent UserManagement window.
     * @param email The email of the new user.
     * @param name The pseudo of the new user.
     * @param password The password of the new user.
     * @param whitelisted Whether the new user is whitelisted or not.
     */
    private void saveUser(String email, String name, String password, boolean whitelisted) {
        Object[] options = {"Administrator", "User"};
        int role = JOptionPane.showOptionDialog(
                this,
                "Select role:",
                "Role",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address!");
            return;
        }

        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long!");
            return;
        }

        try (Connection conn = Configuration.getConnection()){
            int nextId = 0;
            String maxIdQuery = "SELECT MAX(\"id\") FROM \"user\"";
            Statement maxIdStatement = conn.createStatement();
            ResultSet maxIdResult = maxIdStatement.executeQuery(maxIdQuery);
            if (maxIdResult.next()) {
                nextId = maxIdResult.getInt(1) + 1;
            }

            String hashedPassword = hashPassword(password);

            String insertQuery = "INSERT INTO \"user\" (\"id\", \"email\", \"password\", \"role\", \"whitelisted\", \"pseudo\") VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setInt(1, nextId);
            insertStatement.setString(2, email);
            insertStatement.setString(3, hashedPassword);
            insertStatement.setInt(4, role == 0 ? 1 : 0);
            insertStatement.setInt(5, whitelisted ? 1 : 0);
            insertStatement.setString(6, name);

            insertStatement.executeUpdate();

            ArrayList<User> updatedList = new ArrayList<>(userManagement.userList);
            updatedList.add(new User(nextId, email, name, role == 0 ? 1 : 0, whitelisted ? 1 : 0));
            userManagement.updateUserList(updatedList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving new user to database: " + ex.getMessage());
        }

        dispose();
    }

    /**
     * Checks if the given email is valid.
     * @param email The email to be validated.
     * @return True if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * Hashes the given password using SHA-256.
     * @param password The password to be hashed.
     * @return The hashed password.
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());

            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
