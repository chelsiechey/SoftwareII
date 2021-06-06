package utils;
import java.sql.*;

/**
 * This class is used to get first level division information from the database
 */
public class DBDivision {
    /**
     * This method gets the division from the DB with the matching division ID
     * @param divisionId The division ID used to search for the state/province name
     * @return Returns the name of the state/province with the matching division ID
     */
    public static String getStateOfDivisionId(int divisionId) {
        String state = "";
        try {
            String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID=" + divisionId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                state = rs.getString("Division");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return state;
    }

    /**
     * This method gets the country ID from the DB with the matching division ID
     * @param divisionId The division ID used to search for the country ID
     * @return Returns the name of the country with the matching division ID
     */
    public static String getCountryOfDivisionId(int divisionId) {
        String country = "";
        try {
            String sql = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division_ID=" + divisionId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if (rs.getInt("COUNTRY_ID") == 1) {
                    country = "U.S";
                }
                else if (rs.getInt("COUNTRY_ID") == 2) {
                    country = "UK";
                }
                else if (rs.getInt("COUNTRY_ID") == 3) {
                    country = "Canada";
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return country;
    }
}
