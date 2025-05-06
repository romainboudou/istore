package org.user;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UserUpdateDisplay {

    /**
     * Displays the user profile update form.
     *
     * @param userID       The ID of the user.
     * @param parentFrame  The parent JFrame.
     */
    public static void displayUpdate(int userID, JFrame parentFrame) {
        JFrame userProfile = new JFrame("Your profile");
        userProfile.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userProfile.setSize(640, 400);
        userProfile.setLocationRelativeTo(null);
        userProfile.setResizable(false);

        JPanel profilePanel = new JPanel(new GridLayout(8, 1));

        String pseudo = org.user.UserController.getInfo("pseudo", userID);
        String email = org.user.UserController.getInfo("email", userID);
        String role = org.user.UserController.getInfo("role", userID);
        if(Objects.equals(role, "1")) {
            role = "Admin";
        }
        else {
            role = "Employee";
        }

        JLabel userPseudo = new JLabel("Your Pseudo : ");
        JTextField showUsername = new JTextField(pseudo);

        JLabel userEmail = new JLabel("Your Email : ");
        JTextField showEmail = new JTextField(email);

        JLabel userPassword = new JLabel("Your Password : ");
        JPasswordField showPassword = new JPasswordField("password");

        JLabel userRole = new JLabel("Your Role : ");
        JLabel showRole = new JLabel(role);

        JButton update = new JButton("Update");
        update.addActionListener(e -> {
            try {
                String newEmail = showEmail.getText();
                String newUsername = showUsername.getText();
                char[] passwordChars = showPassword.getPassword();
                String newPassword = new String(passwordChars);
                org.user.UserController.update(userID, newEmail, newUsername, newPassword);
            } catch (Exception ex) {
                System.out.println("pas un chiffre");
                ex.printStackTrace();
            }
        });

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            org.user.UserController.delete(userID);
            userProfile.dispose();
            parentFrame.dispose();
        });

        JPanel buttonPanel = new JPanel();

        profilePanel.add(userPseudo);
        profilePanel.add(showUsername);
        profilePanel.add(userEmail);
        profilePanel.add(showEmail);
        profilePanel.add(userPassword);
        profilePanel.add(showPassword);
        profilePanel.add(userRole);
        profilePanel.add(showRole);
        userProfile.add(profilePanel, BorderLayout.CENTER);
        buttonPanel.add(update, BorderLayout.NORTH);
        buttonPanel.add(delete, BorderLayout.SOUTH);
        userProfile.add(buttonPanel, BorderLayout.SOUTH);
        userProfile.setVisible(true);
    }
}
