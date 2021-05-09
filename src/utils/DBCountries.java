package utils;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import utils.DBConnection;
import model.Country;
import javafx.collections.*;
import java.sql.*;

public class DBCountries {
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country country = new Country(countryId, countryName);
                countryList.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countryList;
    }

    public static void checkDateConversion() {
        System.out.println("TEST: ");
        String sql = "SELECT Create_Date from countries";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Timestamp ts = rs.getTimestamp("Create_Date");
                System.out.println("Create Date: " + ts.toLocalDateTime().toString());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
