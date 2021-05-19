package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Contact;
import model.Customer;
import javafx.scene.Parent;
import model.User;
import model.Contact;
import utils.DBConnection;
import utils.DBContact;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;

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
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    private Stage stage;
    private Parent scene;
    private int customerId;
    Vector<Integer> contactIds = new Vector<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        startTimeComboBox.setItems();
        ObservableList<String> contactNames = FXCollections.observableArrayList();

        DBContact.getAllContacts().forEach((contact) -> {

            String contactName = contact.getContactName();
            contactNames.add(contactName);
            System.out.println(contact.getContactId());
            int contactId = contact.getContactId();
            contactIds.addElement(contactId);

        });
        contactComboBox.setItems(contactNames);
        System.out.println(contactIds);
        populateTimeBoxes();
    }
    public void getSelectedCustomersId(Customer selectedCustomer) {
        customerId = selectedCustomer.getCustomerId();
    }

    public void populateTimeBoxes() {
        ObservableList<LocalTime> startTimeList = FXCollections.observableArrayList();
        for (int i = 8; i <= 21; ++ i) {
            LocalTime time = LocalTime.of(i,0);
            startTimeList.add(time);
        }
        ObservableList<LocalTime> endTimeList = FXCollections.observableArrayList();
        for (int i = 9; i <= 22; ++ i) {
            LocalTime time = LocalTime.of(i,0);
            endTimeList.add(time);
        }
//        String stringTime = "2000-01-01T09:00:00Z";
//        Instant timestamp = Instant.parse(stringTime);
//        ZonedDateTime estTime = timestamp.atZone(ZoneId.of("EST"));
//        System.out.println(estTime);

        startTimeComboBox.setItems(startTimeList);
        endTimeComboBox.setItems(endTimeList);
    }

    public void createAppointment(ActionEvent actionEvent) {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        int contactId = contactIds.get(contactComboBox.getSelectionModel().getSelectedIndex());
        String type = typeTextField.getText();
        LocalDate appointmentDate = datePicker.getValue();
        LocalTime startTime = startTimeComboBox.getValue();
        LocalDateTime appointmentStartDateTime = LocalDateTime.of(appointmentDate, startTime);
        LocalTime endTime = endTimeComboBox.getValue();
        LocalDateTime appointmentEndDateTime = LocalDateTime.of(appointmentDate, endTime);
        try {
            String sql = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES ('" + title + "', '" + description + "', '" + location + "', '" + type + "', '" + appointmentStartDateTime + "', '" + appointmentEndDateTime + "', CURRENT_TIMESTAMP, '" + User.getUsername() + "', CURRENT_TIMESTAMP, '" + User.getUsername() + "', " + this.customerId + ", " + User.getUserId() + ", " + contactId + ")" ;
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("Reached catch");
            throwables.printStackTrace();
        }

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
