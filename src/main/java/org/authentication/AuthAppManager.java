package org.authentication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthAppManager extends JFrame {
    private JTextField emailField, passwordField;
    private JButton loginButton, createAccountButton;

    // Constructor to initialize the authentication application window
    public AuthAppManager() {
        setTitle("iStore");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        setLayout(null);
        setVisible(true);
    }

    // Method to initialize UI components (labels, text fields, buttons)
    private void initializeComponents() {
        // Email label
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 20, 80, 25);
        add(emailLabel);

        // Email text field
        emailField = new JTextField(20);
        emailField.setBounds(100, 20, 250, 25);
        add(emailField);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 50, 80, 25);
        add(passwordLabel);

        // Password text field
        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 250, 25);
        add(passwordField);

        // Login button with ActionListener
        loginButton = new JButton("Login");
        loginButton.setBounds(20, 90, 100, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action performed when the login button is clicked
                String email = emailField.getText();
                String password = passwordField.getText();
                if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please complete the fields before confirming.");
                } else {
                    AuthEventManager.loginUser(email, password, AuthAppManager.this);
                }
            }
        });
        add(loginButton);

        // Create Account button with ActionListener
        createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(130, 90, 150, 25);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action performed when the create account button is clicked
                String email = emailField.getText();
                String password = passwordField.getText();
                if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please complete the fields before confirming.");
                } else {
                    // Validate email using regex pattern
                    String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
                    Pattern pattern = Pattern.compile(emailPattern);
                    Matcher matcher = pattern.matcher(emailField.getText());

                    if (matcher.matches()) {
                        AuthEventManager.createAccount(email, password);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
                    }
                }
            }
        });
        add(createAccountButton);
    }

    // Static method to launch the authentication application UI
    public static void main() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AuthAppManager();
            }
        });
    }
}
