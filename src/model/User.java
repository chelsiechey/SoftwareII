package model;

/**
 * Represents a user
 * @author Chelsie Conrad
 */
public class User {
    private static int userId;
    private static String username;
    private String password;

    // constructor
    public User(int userId, String username, String password) {
        setUserId(userId);
        setUsername(username);
        setPassword(password);
    }

    // getters
    /**
     * Gets the user ID
     * @return Returns the user's ID
     */
    public static int getUserId() {
        return userId;
    }

    /**
     * Gets the username
     * @return Returns the username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Gets the user password
     * @return Returns the user's password
     */
    public String getPassword() {
        return password;
    }

    // setters

    /**
     * Sets the user ID
     * @param userId The value set to the user ID
     */
    public void setUserId(int userId) {
        User.userId = userId;
    }

    /**
     * Sets the username
     * @param username The value set to the username
     */
    public void setUsername(String username) {
        User.username = username;
    }

    /**
     * Sets the user password
     * @param password The value set to the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
