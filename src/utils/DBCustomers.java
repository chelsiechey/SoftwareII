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
            String sql = "SELECT * from customers";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                try {
                    String sqlDivision = "SELECT Division from first_level_divisions WHERE Division_ID=" + divisionId;
                    PreparedStatement psDivision = DBConnection.getConnection().prepareStatement(sqlDivision);
                    ResultSet rsDivision = psDivision.executeQuery();
                    while (rsDivision.next()) {
                        String division = rsDivision.getString("Division");
                        Customer customer = new Customer(customerId, customerName, address, postalCode, phone, divisionId, division);
                        customerList.add(customer);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }
}
