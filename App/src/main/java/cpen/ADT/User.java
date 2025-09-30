package cpen.adt;

/**
 * Represents a user with a username and password.
 */
public class User {
    private String username;
    private String password;

    /**
     * Constructs a User with the specified username and password.
     *
     * @param username the username for the user
     * @param password the password for the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     *
     * @param username the new username to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     *
     * @param password the new password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
