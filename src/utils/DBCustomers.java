package utils;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import utils.DBConnection;
import model.Customer;
import javafx.collections.*;
import java.sql.*;

public class DBCustomers {
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
//            String sql = "SELECT * from customers";
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
