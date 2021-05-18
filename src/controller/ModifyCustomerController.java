package controller;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import model.Customer;
import utils.DBConnection;
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

    ObservableList<String> countryOptionsList = FXCollections.observableArrayList();
    ObservableList<String> stateOptionsList = FXCollections.observableArrayList();



    public void getCustomerToModify(Customer customerToModify) {
        customerIdTextField.setText(String.valueOf(customerToModify.getCustomerId()));
        customerNameTextField.setText(customerToModify.getCustomerName());
        addressTextField.setText(String.format(customerToModify.getAddress()));
        postalCodeTextField.setText(String.valueOf(customerToModify.getPostalCode()));
        phoneTextField.setText(String.valueOf(customerToModify.getPhone()));
        countryComboBox.getSelectionModel().select(String.valueOf(customerToModify.getCountry()));
        stateComboBox.getSelectionModel().select(String.valueOf(customerToModify.getState()));
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryOptions();
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

    public void saveCustomer(ActionEvent actionEvent) {
        int customerId = Integer.parseInt(customerIdTextField.getText());
        System.out.println(customerId);
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
        System.out.println(state);
        System.out.println("Reached 1");
        try {
            System.out.println("Reached 2");
            String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + state + "'";
            System.out.println("Reached 3");
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            System.out.println("Reached 4");
            ResultSet rs = ps.executeQuery();
            System.out.println("Reached 5");
            System.out.println(rs);
            while (rs.next()) {
                try {
                    System.out.println("Reached 6");
                    int divisionId = (rs.getInt("Division_ID"));
                    System.out.println("Reached 7");
                    String sqlToUpdate = "UPDATE customers SET Customer_Name='" + customerName + "', Address='" + address + "', Postal_Code='" + postalCode + "', Phone='" + phone + "', Division_ID=" + divisionId + ", Last_Update=CURRENT_TIMESTAMP, Last_Updated_By='" + username + "' WHERE Customer_ID=" + customerId;
                    System.out.println("Reached 8");
                    PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlToUpdate);
                    preparedStatement.executeUpdate();
                    System.out.println("Reached 9");
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
