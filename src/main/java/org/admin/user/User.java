package org.admin.user;

/**
 * Represents a user with associated attributes.
 */
class User {
    private int id;
    private String email;
    private String pseudo;
    private int role;
    private int whitelisted;

    /**
     * Initializes a new instance of the User class.
     * @param id The user ID.
     * @param email The email of the user.
     * @param pseudo The pseudo of the user.
     * @param role The role of the user.
     * @param whitelisted The whitelisted status of the user.
     */
    public User(int id, String email, String pseudo, int role, int whitelisted) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.role = role;
        this.whitelisted = whitelisted;
    }

    /**
     * Gets the ID of the user.
     * @return The user ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the email of the user.
     * @return The user email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the pseudo of the user.
     * @return The user pseudo.
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Gets the role of the user.
     * @return The user role.
     */
    public int getRole() {
        return role;
    }

    /**
     * Gets the whitelisted status of the user.
     * @return The whitelisted status (1 if whitelisted, 0 otherwise).
     */
    public int getWhitelisted() {
        return whitelisted;
    }
}
