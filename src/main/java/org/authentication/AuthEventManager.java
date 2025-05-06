package org.authentication;

import org.admin.AdminPanel;
import org.user.UserMain;

import javax.swing.*;
import java.sql.SQLException;

public class AuthEventManager {

    /**
     * Attempts to log in the user using the provided email and password.
     *
     * @param email  The user's email address.
     * @param password  The user's password.
     * @param frame  The JFrame associated with the login process.
     */
    public static void loginUser(String email, String password, JFrame frame) {
        try {
            // Check if the email is whitelisted
            if (AuthDatabaseManager.isEmailWhitelisted(email)) {
                String storedPassword = AuthDatabaseManager.getUserPassword(email);

                // Validate password length
                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long!");
                } else {
                    // Verify the password
                    if (PasswordHash.verifyPassword(password, storedPassword)) {
                        // Retrieve user role and ID
                        int userRole = AuthDatabaseManager.getUserRole(email);
                        int userID = AuthDatabaseManager.getUserId(email);
                        // Handle user role
                        handleUserRole(userRole, userID);
                        // Close the login window
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid password!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid email or invalid password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error accessing database: " + e.getMessage());
        }
    }

    /**
     * Handles the user role by launching the appropriate main application.
     *
     * @param userRole  The role of the user.
     * @param userID  The ID of the user.
     */
    private static void handleUserRole(int userRole, int userID) {
        switch (userRole) {
            case 0:
                UserMain.main(null, userID);
                break;
            case 1:
                AdminPanel.main(null, userID);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid user role!");
        }
    }

    /**
     * Creates a new user account with the provided email and password.
     *
     * @param email  The user's email address.
     * @param password  The user's password.
     */
    public static void createAccount(String email, String password) {
        if (AuthDatabaseManager.isEmailWhitelisted(email)) {
            JOptionPane.showMessageDialog(null, "Email is already associated with an existing account!");
        } else {
            // Validate password length
            if (password.length() < 8) {
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long!");
            } else {
                // Generate the next user ID
                int nextId = AuthDatabaseManager.getMaxUserId() + 1;
                // Hash the password
                String hashedPassword = PasswordHash.hashPassword(password);
                // Insert the new user into the database
                AuthDatabaseManager.insertUser(nextId, email, hashedPassword);
                JOptionPane.showMessageDialog(null, "Account created successfully!");
            }
        }
    }
}
