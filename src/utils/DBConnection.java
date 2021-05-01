package utils;
import java.sql.*;

public class DBConnection {
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ07cKg";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    // Driver and Connection Interface reference
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn;

    // Variables for the database
    private static final String username = "U07cKg";
    private static final String password = "53688989591";

    // Establishes a connection to the database
    public static Connection establishConnection() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection established.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed.");
        } catch (SQLException e){

        }
    }
}
