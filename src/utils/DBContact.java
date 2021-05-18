package utils;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import utils.DBConnection;
import model.Appointment;
import model.Contact;
import javafx.collections.*;
import java.sql.*;

public class DBContact {
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact contact = new Contact(contactId, contactName, email);
                contactList.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactList;
    }
    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> contactNameList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String contactName = rs.getString("Contact_Name");
                contactNameList.add(contactName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactNameList;
    }
}
