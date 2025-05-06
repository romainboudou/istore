package org.store;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StoreEventManager {

    /**
     * Refreshes the store table based on the given user ID.
     *
     * @param model  The table model to be refreshed.
     * @param userId The ID of the user.
     */
    public static void refreshStoreTable(DefaultTableModel model, int userId) {
        model.setRowCount(0);

        int storeId = StoreDatabaseManager.getStoreIdForUser(userId);

        if (storeId != -1) {
            List<Object[]> users = StoreDatabaseManager.getUsersForStore(storeId);
            for (Object[] user : users) {
                model.addRow(user);
            }
        }
    }

    /**
     * Refreshes the store table based on the given store ID.
     *
     * @param model   The table model to be refreshed.
     * @param storeID The ID of the store.
     */
    public static void refreshStoreTableWithStoreID(DefaultTableModel model, int storeID) {
        model.setRowCount(0);

        if (storeID != -1) {
            List<Object[]> users = StoreDatabaseManager.getUsersForStore(storeID);
            for (Object[] user : users) {
                model.addRow(user);
            }
        }
    }
}
