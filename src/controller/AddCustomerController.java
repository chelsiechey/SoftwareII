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
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import model.User;
import utils.DBConnection;

public class AddCustomerController implements Initializable {
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

    ObservableList<String> countryOptionsList = FXCollections.observableArrayList();
    ObservableList<String> stateOptionsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryOptions();
    }

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

    public void filterByCountry(ActionEvent actionEvent) throws SQLException {
        stateOptionsList.clear();
        System.out.println(countryComboBox.getValue());
        String country = countryComboBox.getValue();
        initializeStates(country);
    }
    public void handleCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setHeaderText("Discard all changes?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            scene.getStylesheets().add("/stylesheet.css");
            stage.setTitle("Customer View");
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    public void createCustomer(ActionEvent actionEvent) {
        String customerName = customerNameTextField.getText();
        System.out.println(customerName);
        String address = addressTextField.getText();
        System.out.println(address);
        String postalCode = postalCodeTextField.getText();
        System.out.println(postalCode);
        String phone = phoneTextField.getText();
        System.out.println(phoneTextField);
        String state = stateComboBox.getValue();
        String username = User.getUsername();
        try {
            String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + state + "'";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                try {
                    int divisionId = (rs.getInt("Division_ID"));
                    String sqlToUpdate = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                            "VALUES ('" + customerName + "', '" + address + "', '" + postalCode + "', '" + phone + "', CURRENT_TIMESTAMP, '" + username + "', CURRENT_TIMESTAMP, '" + username + "', " + divisionId + ")" ;
                    PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlToUpdate);
                    preparedStatement.executeUpdate();
                } catch (SQLException throwables) {
                    System.out.println("Reached catch");
                    throwables.printStackTrace();
                }
            }
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            scene.getStylesheets().add("/stylesheet.css");
            stage.setTitle("Customer View");
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }


}
