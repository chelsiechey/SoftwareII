package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import model.User;
import utils.DBConnection;

/**
 * This class creates the controller for adding customers
 */
public class AddCustomerController implements Initializable {
    private Stage stage;
    private Parent scene;
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

    // customer table variables
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private String state;

    // observable lists for state/province and country combo boxes
    private final ObservableList<String> countryOptionsList = FXCollections.observableArrayList();
    private final ObservableList<String> stateOptionsList = FXCollections.observableArrayList();

    /**
     * This method initializes the controller for adding customers
     * by populating the country options combo box with countries
     * @param url Unused parameter for a URL
     * @param resourceBundle Unused parameter for a resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryOptions();
    }

    /**
     * This method gets all of the countries stored in the database
     * and populates the country combo box with the results
     */
    public void countryOptions() {
        try {
            String sql = "SELECT country FROM countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String country = rs.getString("Country");
                countryOptionsList.add(country);
                countryComboBox.setItems(countryOptionsList);
                initializeStates(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * This method finds the states/provinces in the selected country
     * and populates the state combo box
     * @param country The country selected by the user
     */
    public void initializeStates(String country) {
        if (country.equals("U.S")) {
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

    /**
     * This method sets the state combo box based on the country selected
     * @param actionEvent The ActionEvent object generated when a country is selected in the country combo box
     */
    public void filterByCountry(ActionEvent actionEvent) {
        stateOptionsList.clear();
        String country = countryComboBox.getValue();
        initializeStates(country);
    }

    /**
     * This method confirms if the user would actually like to cancel adding a customer
     * and redirects them to the customer page if yes
     * or stays on the add customer page if no
     * @param actionEvent The ActionEvent object generated when the cancel button is pressed.
     * @throws IOException Throws an exception if the fxml file for the Customer page is not found
     */
    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setHeaderText("Discard all changes?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customer.fxml")));
            scene.getStylesheets().add("/stylesheet.css");
            stage.setTitle("Customer View");
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method first checks that all customer fields are present and then attempts to add a
     * customer into the database and return the user to the Customer page
     * @param actionEvent The ActionEvent object generated when the save button is pressed
     */
    public void createCustomer(ActionEvent actionEvent) {
        customerName = customerNameTextField.getText();
        address = addressTextField.getText();
        postalCode = postalCodeTextField.getText();
        phone = phoneTextField.getText();
        state = stateComboBox.getValue();
        String username = User.getUsername();
        if (valuesInitialized()) {
            try {
                String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + state + "'";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                System.out.println(rs);
                while (rs.next()) {
                    try {
                        int divisionId = (rs.getInt("Division_ID"));
                        String sqlToUpdate = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                                "VALUES ('" + customerName + "', '" + address + "', '" + postalCode + "', '" + phone + "', CURRENT_TIMESTAMP, '" + username + "', CURRENT_TIMESTAMP, '" + username + "', " + divisionId + ")";
                        PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlToUpdate);
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        System.out.println("Reached catch");
                        throwables.printStackTrace();
                    }
                }
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customer.fxml")));
                scene.getStylesheets().add("/stylesheet.css");
                stage.setTitle("Customer View");
                stage.setScene(new Scene(scene));
                stage.show();
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Please fill in all fields with the appropriate data type.");
            alert.showAndWait();
        }
    }

    /**
     * This method checks that the user entered data in each field
     * @return Returns true if all of the fields have values in them or false if any are missing
     */
    public boolean valuesInitialized() {
        return !customerName.equals("") && !address.equals("") && !postalCode.equals("") && !phone.equals("") && !state.equals("");
    }
}
