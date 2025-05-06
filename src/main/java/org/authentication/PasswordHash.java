package org.authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {

    /**
     * Hashes a password using the SHA-256 algorithm.
     *
     * @param password The password to be hashed.
     * @return The hashed password.
     */
    public static String hashPassword(String password) {
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

    /**
     * Verifies whether the input password matches the hashed password.
     *
     * @param inputPassword The password provided by the user for verification.
     * @param hashedPassword The hashed password stored in the database.
     * @return True if the passwords match, false otherwise.
     */
    public static boolean verifyPassword(String inputPassword, String hashedPassword) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput.equals(hashedPassword);
    }
}
