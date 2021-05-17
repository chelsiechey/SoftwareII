package controller;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import model.Customer;
import utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;
import java.net.URL;

public class ModifyCustomerController implements Initializable {

    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private ComboBox<String> stateComboBox;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private TextField phoneTextField;

    ObservableList<String> countryOptionsList = FXCollections.observableArrayList();
    ObservableList<String> stateOptionsList = FXCollections.observableArrayList();


    public void getCustomerToModify(Customer customerToModify) {
        System.out.println("Reached getCustomerToModify");
        System.out.println(customerToModify.getCustomerId());
        customerIdTextField.setText(String.valueOf(customerToModify.getCustomerId()));
        customerNameTextField.setText(customerToModify.getCustomerName());
        addressTextField.setText(String.format(customerToModify.getAddress()));
        postalCodeTextField.setText(String.valueOf(customerToModify.getPostalCode()));
        phoneTextField.setText(String.valueOf(customerToModify.getPhone()));
        countryComboBox.setValue(String.valueOf(customerToModify.getCountry()));
    }
    public void countryOptions() {
        try {
            String sql = "SELECT country FROM countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCountry(rs.getString("country"));
                countryOptionsList.add(customer.getCountry());
                countryComboBox.setItems(countryOptionsList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryOptions();
    }

    public void filterByCountry(ActionEvent actionEvent) throws SQLException {
        stateOptionsList.clear();
        System.out.println(countryComboBox.getValue());
        String country = countryComboBox.getValue();
        if (country.equals("U.S")) {
            int countryId = 1;
            try {
                System.out.println("Reached 1");
                String sql = "SELECT Division FROM first_level_divisions WHERE COUNTRY_ID=1";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    stateOptionsList.add(rs.getString("Division"));
                    stateComboBox.setItems(stateOptionsList);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (country.equals("UK")) {
            try {
                String sql = "SELECT Division FROM first_level_divisions WHERE COUNTRY_ID=2";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    stateOptionsList.add(rs.getString("Division"));
                    stateComboBox.setItems(stateOptionsList);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (country.equals("Canada")) {
            try {
                String sql = "SELECT Division FROM first_level_divisions WHERE COUNTRY_ID=3";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    stateOptionsList.add(rs.getString("Division"));
                    stateComboBox.setItems(stateOptionsList);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
