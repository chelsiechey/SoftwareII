package utils;
import model.Customer;
import javafx.collections.*;
import java.sql.*;

/**
 * This class is used to get customer information from the database
 */
public class DBCustomers {
    /**
     * This method gets all customers and the their first level division from the DB
     * @return Returns an observable list of customers
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT customers.*, first_level_divisions.Division FROM customers, first_level_divisions WHERE customers.Division_ID=first_level_divisions.Division_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                String division= rs.getString("Division");
                Customer customer = new Customer(customerId, customerName, address, postalCode, phone, divisionId, division);
                customerList.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }
}
