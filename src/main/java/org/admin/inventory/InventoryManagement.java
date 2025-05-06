package org.admin.inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the main frame for Inventory Management.
 */
public class InventoryManagement extends JFrame {
    private JButton addItemButton, deleteItemsButton, linkInventoryButton, deleteInventoryButton;

    /**
     * Constructs a new instance of InventoryManagement.
     */
    public InventoryManagement() {
        setTitle("Inventory Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes the components of the InventoryManagement frame.
     */
    private void initializeComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(25, 5, 5, 5);

        addItemButton = new JButton("Add New Item");
        addItemButton.setPreferredSize(new Dimension(150, 30));
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddItem(InventoryManagement.this).setVisible(true);
            }
        });
        add(addItemButton, gbc);

        deleteItemsButton = new JButton("Delete Items");
        deleteItemsButton.setPreferredSize(new Dimension(150, 30));
        deleteItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteItem(InventoryManagement.this);
            }
        });
        add(deleteItemsButton, gbc);

        linkInventoryButton = new JButton("Link Inventory to Store");
        linkInventoryButton.setPreferredSize(new Dimension(200, 30));
        linkInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LinkInventoryToStore(InventoryManagement.this);
            }
        });
        add(linkInventoryButton, gbc);

        deleteInventoryButton = new JButton("Delete Inventory");
        deleteInventoryButton.setPreferredSize(new Dimension(150, 30));
        deleteInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteInventory(InventoryManagement.this);
            }
        });
        add(deleteInventoryButton, gbc);
    }
}
