package org.user;

import org.h2config.Configuration;
import org.authentication.PasswordHash;
import org.main.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController {

    /**
     * Searches for users based on the given search text and populates the table model.
     *
     * @param searchText The text to search for.
     * @param tableModel The table model to be populated.
     */
    public static void searchUser(String searchText, DefaultTableModel tableModel) {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        tableModel.addColumn("ID");
        tableModel.addColumn("Email");
        tableModel.addColumn("Pseudo");
        tableModel.addColumn("Role");
        try (Connection conn = Configuration.getConnection()) {
            String sql = "SELECT * FROM \"user\" WHERE \"email\" LIKE ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "%" + searchText + "%");

            ResultSet resultSet = statement.executeQuery();

            boolean found = false;

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String pseudo = resultSet.getString("pseudo");
                int role = resultSet.getInt("role");
                tableModel.addRow(new Object[]{id, email, pseudo, role});
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(null, "No user found.");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves user information for the specified column and user ID.
     *
     * @param column The column to retrieve information from.
     * @param userID The ID of the user.
     * @return The information for the specified column.
     */
    public static String getInfo(String column, int userID) {
        try (Connection conn = Configuration.getConnection()) {
            String sql = "SELECT * FROM \"user\" WHERE \"id\" = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userID);
            ResultSet result = (statement.executeQuery());
            while (result.next()) {
                return result.getString(column);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes the user with the specified ID.
     *
     * @param id The ID of the user to be deleted.
     */
    public static void delete(int id) {
        try (Connection conn = Configuration.getConnection()) {
            String sql = "DELETE FROM \"user\" WHERE \"id\" = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            Main.main(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates user information with the specified ID, new email, new pseudo, and new password.
     *
     * @param id          The ID of the user to be updated.
     * @param newEmail    The new email for the user.
     * @param newPseudo   The new pseudo for the user.
     * @param newPassword The new password for the user.
     */
    public static void update(int id, String newEmail, String newPseudo, String newPassword) {
        try (Connection conn = Configuration.getConnection()) {
            if (newPassword.length() < 8) {
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long!");
            } else {
                String sql = "UPDATE \"user\" SET \"email\" = ?, \"password\" = ?, \"pseudo\" = ? WHERE \"id\" = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, newEmail);
                statement.setString(2, PasswordHash.hashPassword(newPassword));
                statement.setString(3, newPseudo);
                statement.setInt(4, id);
                String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
                Pattern pattern = Pattern.compile(emailPattern);
                Matcher matcher = pattern.matcher(newEmail);
                if (matcher.matches()) {
                    statement.executeUpdate();
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
