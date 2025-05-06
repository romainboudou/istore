package org.user;

import static org.user.UserDisplay.display;

public class UserMain {

    /**
     * Entry point for the user application. Displays the user panel based on the provided user ID.
     *
     * @param args    Command-line arguments.
     * @param userID  The ID of the user.
     */
    public static void main(String[] args, int userID) {
        display(userID);
    }
}
