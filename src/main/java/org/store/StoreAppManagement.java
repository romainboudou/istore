package org.store;

import org.inventory.InventoryAppManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoreAppManagement extends JFrame {
    private JTable userTable;

    /**
     * Constructs a StoreAppManagement instance for a specific store.
     *
     * @param storeID The ID of the store.
     */
    private StoreAppManagement(int storeID) {
        setTitle(StoreDatabaseManager.getStoreNameById(storeID));
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultTableModel userModel = new DefaultTableModel();
        userModel.addColumn("ID");
        userModel.addColumn("Email");
        userModel.addColumn("Pseudo");
        userModel.addColumn("Role");

        userTable = new JTable(userModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        JButton inventory = new JButton("Inventory");

        inventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InventoryAppManager.main(null, storeID);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(inventory);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel);

        StoreEventManager.refreshStoreTableWithStoreID(userModel, storeID);
    }

    /**
     * Main method to create and display the StoreAppManagement frame.
     *
     * @param args   Command-line arguments (unused).
     * @param storeID The ID of the store.
     */
    public static void main(String[] args, int storeID) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StoreAppManagement(storeID).setVisible(true);
            }
        });
    }
}
