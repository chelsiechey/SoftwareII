package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import javafx.scene.Parent;
import model.User;
import utils.DBConnection;
import utils.DBContact;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import utils.DBContact;

public class AddAppointmentController implements Initializable {
    @FXML
    private TextField appointmentIdTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private TextField typeTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Integer> startTimeComboBox;
    @FXML
    private ComboBox<Integer> endTimeComboBox;
    private Stage stage;
    private Parent scene;
    private int customerId;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        startTimeComboBox.setItems();
        contactComboBox.setItems(DBContact.getAllContactNames());
    }
    public void getSelectedCustomersId(Customer selectedCustomer) {
        customerId = selectedCustomer.getCustomerId();
    }

    public void createAppointment(ActionEvent actionEvent) {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String contact = contactComboBox.getValue();
        String type = typeTextField.getText();
        LocalDate appointmentDate = datePicker.getValue();
        int startTime = startTimeComboBox.getValue();
        int endTime = startTimeComboBox.getValue();

//        String customerName = customerNameTextField.getText();
//        String address = addressTextField.getText();
//        String postalCode = postalCodeTextField.getText();
//        String phone = phoneTextField.getText();
//        String state = stateComboBox.getValue();
//        String username = User.getUsername();
//        try {
//            String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + state + "'";
//            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            System.out.println(rs);
//            while (rs.next()) {
//                try {
//                    int divisionId = (rs.getInt("Division_ID"));
//                    String sqlToUpdate = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
//                            "VALUES ('" + customerName + "', '" + address + "', '" + postalCode + "', '" + phone + "', CURRENT_TIMESTAMP, '" + username + "', CURRENT_TIMESTAMP, '" + username + "', " + divisionId + ")" ;
//                    PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlToUpdate);
//                    preparedStatement.executeUpdate();
//                } catch (SQLException throwables) {
//                    System.out.println("Reached catch");
//                    throwables.printStackTrace();
//                }
//            }
//            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
//            scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
//            scene.getStylesheets().add("/stylesheet.css");
//            stage.setTitle("Customer View");
//            stage.setScene(new Scene(scene));
//            stage.show();
//        } catch (SQLException | IOException throwables) {
//            throwables.printStackTrace();
//        }
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
}
