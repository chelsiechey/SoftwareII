package controller;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import model.Customer;
import utils.DBConnection;

import java.util.Objects;
import java.util.Optional;
import java.io.IOException;
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
import model.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class ModifyCustomerController implements Initializable {
    private Stage stage;
    private Parent scene;
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
     * This method populates the customer data in the fields
     * @param customerToModify The Customer the user selected to modify
     */
    public void getCustomerToModify(Customer customerToModify) {
        customerIdTextField.setText(String.valueOf(customerToModify.getCustomerId()));
        customerNameTextField.setText(customerToModify.getCustomerName());
        addressTextField.setText(customerToModify.getAddress());
        postalCodeTextField.setText(String.valueOf(customerToModify.getPostalCode()));
        phoneTextField.setText(String.valueOf(customerToModify.getPhone()));
        String country = customerToModify.getCountry();
        countryComboBox.getSelectionModel().select(String.valueOf(country));
        stateComboBox.getSelectionModel().select(String.valueOf(customerToModify.getState()));
        initializeStates(country);
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
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * This method initializes the controller for modifying customers
     * by populating the country options combo box with countries
     * @param url Unused parameter for a URL
     * @param resourceBundle Unused parameter for a resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryOptions();
    }

    /**
     * This method finds the states/provinces in the selected country
     * and populates the state combo box
     * @param country The country selected by the user or the initial country of the customer
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
     * This method confirms if the user would actually like to cancel modifying a customer
     * and redirects them to the customer page if yes or stays on the modify customer page if no
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
     * This method checks that the user entered data in each field
     * @return Returns true if all of the fields have values in them or false if any are missing
     */
    public boolean valuesInitialized() {
        return !customerName.equals("") && !address.equals("") && !postalCode.equals("") && !phone.equals("") && !state.equals("");
    }

    /**
     * This method first checks that all customer fields are present and then attempts to update the
     * customer in the database and return the user to the Customer page
     * @param actionEvent The ActionEvent object generated when the save button is pressed
     */
    public void saveCustomer(ActionEvent actionEvent) {
        int customerId = Integer.parseInt(customerIdTextField.getText());
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
                while (rs.next()) {
                    try {
                        int divisionId = (rs.getInt("Division_ID"));
                        String sqlToUpdate = "UPDATE customers SET Customer_Name='" + customerName + "', Address='" + address + "', Postal_Code='" + postalCode + "', Phone='" + phone + "', Division_ID=" + divisionId + ", Last_Update=CURRENT_TIMESTAMP, Last_Updated_By='" + username + "' WHERE Customer_ID=" + customerId;
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
}
