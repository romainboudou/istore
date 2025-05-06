package org.user;

import org.store.StoreAppforuserManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserDisplay {

    /**
     * Displays the user panel with options for managing the user's profile and viewing the store.
     *
     * @param userID The ID of the user.
     */
    public static void display(int userID) {
        JFrame userFrame = new JFrame("User Panel");
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userFrame.setSize(600, 400);
        userFrame.setLocationRelativeTo(null);
        userFrame.setResizable(false);

        JPanel userPanel = new JPanel();
        JPanel userSearch = new JPanel();

        JButton userProfile = new JButton("Your Profile");
        userProfile.addActionListener(e -> {
            UserUpdateDisplay.displayUpdate(userID, userFrame);
        });

        JButton userStore = new JButton("View Store");
        userStore.addActionListener(e ->{
            StoreAppforuserManagement.main(null, userID);
        });

        JTable table = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        table.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        JTextField searchField = new JTextField(10);

        JButton search = new JButton("Search");
        search.addActionListener(e -> {
            String searchText = searchField.getText();
            UserController.searchUser(searchText, tableModel);
        });

        userPanel.add(userProfile);
        userPanel.add(userStore);

        userSearch.add(search);
        userSearch.add(searchField);
        userSearch.add(scrollPane, BorderLayout.SOUTH);

        userFrame.add(userPanel, BorderLayout.NORTH);
        userFrame.add(userSearch, BorderLayout.CENTER);
        userFrame.setVisible(true);
    }
}
