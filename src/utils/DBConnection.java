package utils;
import java.sql.*;

/**
 * This class creates a connection to the database
 */
public class DBConnection {
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ07cKg";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    // Driver and Connection Interface reference
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn;

    // Variables for the database
    private static final String username = "U07cKg";
    private static final String password = "53688989591";

    /**
     * This method establishes a connection to the database or catches an exception
     * @return Returns the connection to the database
     */
    public static Connection establishConnection() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * This method gets the previously established connection to the database
     * @return Returns the connection to the database
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * This method closes the connection to the database or catches an exception
     */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e){
            // Do nothing -- plans for race conditions
        }
    }
}
