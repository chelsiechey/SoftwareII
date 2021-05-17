package model;
import java.sql.Timestamp;

public class User {
    private static int userId;
    private static String username;
    private String password;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    // constructor
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    // getters
    public static int getUserId() {
        return userId;
    }
    public static String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    // setters
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
