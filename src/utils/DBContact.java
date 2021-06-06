package utils;
import model.Contact;
import javafx.collections.*;
import java.sql.*;

/**
 * This class is used to get contact information from the database
 */
public class DBContact {
    /**
     * This method gets all contacts from the DB
     * @return Returns an observable list of contacts
     */
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
}
