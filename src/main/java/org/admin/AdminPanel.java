package org.admin;

import org.admin.inventory.InventoryManagement;
import org.admin.store.StoreManagement;
import org.admin.user.UserManagement;
import org.user.UserUpdateDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JFrame {
    private JButton userProfileButton, userManagementButton, inventoryManagementButton, storeManagementButton;
    private JPanel catalogPanel;
    /**
     * Creates a new instance of the AdminPanel.
     * @param userID  The ID of the user.
     */
    public AdminPanel(int userID) {
        setTitle("Admin Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents(userID);
        setVisible(true);
    }

    /**
     * Initializes the GUI components of the AdminPanel.
     * @param userID  The ID of the user.
     */
    private void initializeComponents(int userID) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(50, 5, 5, 5);

        userProfileButton = new JButton(("Your profile"));

        add(userProfileButton, gbc);
        userProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserUpdateDisplay.displayUpdate(userID, null);
            }
        });

        userManagementButton = new JButton("User Management");
        userManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUserManagement();
            }
        });
        add(userManagementButton, gbc);

        inventoryManagementButton = new JButton("Inventory Management");
        inventoryManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInventoryManagement();
            }
        });
        add(inventoryManagementButton, gbc);

        storeManagementButton = new JButton("Store Management");
        storeManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStoreManagement();
            }
        });
        add(storeManagementButton, gbc);

        catalogPanel = new JPanel();
        catalogPanel.setPreferredSize(new Dimension(250, 250));
        gbc.weighty = 1.0;
        add(catalogPanel, gbc);
    }

    /**
     * Opens the User Management window.
     */
    private void openUserManagement() {
        UserManagement userManagement = new UserManagement();
        userManagement.setVisible(true);
    }

    /**
     * Opens the Inventory Management window.
     */
    private void openInventoryManagement() {
        InventoryManagement inventoryManagement = new InventoryManagement();
        inventoryManagement.setVisible(true);
    }

    /**
     * Opens the Store Management window.
     */
    private void openStoreManagement() {
        StoreManagement storeManagement = new StoreManagement();
        storeManagement.setVisible(true);
    }

    /**
     * Main method to start the AdminPanel.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args, int userID) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminPanel(userID);
            }
        });
    }
}