package utils;
import javafx.collections.*;
import java.sql.*;

public class DBCustomerDivision {
    public static ObservableList<String> getAllCustomerDivisions(ObservableList<Integer> divisionId) {
        String divisionIdString = divisionId.toString();
        String searchString = divisionIdString.substring(1, divisionIdString.length() - 1).replace(", ", ",");
        ObservableList<String> customerDivisionList = FXCollections.observableArrayList();
        try {
            System.out.println("Reached DB Customer Division");
            String sql = "SELECT Division from first_level_divisions WHERE Division_ID IN (" + searchString + ")";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String division = rs.getString("Division");
                customerDivisionList.add(division);
            }
        } catch (SQLException throwables) {
            System.out.println("Reached catch in DB Customer Division");
            throwables.printStackTrace();
        }
        System.out.println(customerDivisionList);
        return customerDivisionList;
    }
}
