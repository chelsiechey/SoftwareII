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
import model.Appointment;
import model.User;
import utils.DBConnection;
import utils.DBContact;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;

public class ModifyAppointmentController implements Initializable {
    ObservableList<String> sortedStartTimeList = FXCollections.observableArrayList();
    ObservableList<String> sortedEndTimeList = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneId = ZoneId.systemDefault();
    private final ZoneId utcZoneId = ZoneId.of("UTC");
    private final ZoneId estZoneId = ZoneId.of("America/New_York");
    private Stage stage;
    private Parent scene;
    Vector<Integer> contactIds = new Vector<>();
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
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> startTimeComboBox;
    @FXML
    private ComboBox<String> endTimeComboBox;
    @FXML
    void saveAppointment() {
    }
    @FXML
    void handleCancel(ActionEvent actionEvent) throws IOException {
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
    public void getAppointmentToModify(Appointment appointmentToModify) {
        appointmentIdTextField.setText(String.valueOf(appointmentToModify.getAppointmentId()));
        titleTextField.setText(String.valueOf(appointmentToModify.getTitle()));
        descriptionTextField.setText(String.valueOf(appointmentToModify.getDescription()));
        locationTextField.setText(String.valueOf(appointmentToModify.getLocation()));
        contactComboBox.getSelectionModel().select(String.valueOf(appointmentToModify.getContact()));
        typeTextField.setText(String.valueOf(appointmentToModify.getType()));
        LocalDateTime startDateTime = LocalDateTime.parse(appointmentToModify.getStart().toString(), dateTimeFormat);
        LocalDateTime endDateTime = LocalDateTime.parse(appointmentToModify.getEnd().toString(), dateTimeFormat);
        LocalDate appointmentStartDate = startDateTime.toLocalDate();
        LocalDate appointmentEndDate = endDateTime.toLocalDate();
        String startTime = startDateTime.toLocalTime().format(timeDTF);
        String endTime = endDateTime.toLocalTime().format(timeDTF);
        startTimeComboBox.getSelectionModel().select(startTime);
        endTimeComboBox.getSelectionModel().select(endTime);
        startDatePicker.setValue(appointmentStartDate);
        endDatePicker.setValue(appointmentEndDate);
    }
    public boolean isInBusinessHours() {
        LocalTime selectedStartTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime selectedEndTime = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem());
        LocalDate selectedStartDate = startDatePicker.getValue();
        LocalDate selectedEndDate = endDatePicker.getValue();
        LocalTime openTime = LocalTime.parse(sortedStartTimeList.get(0));
        LocalDateTime selectedStartDateTime = LocalDateTime.of(selectedStartDate, selectedStartTime);
        LocalDateTime selectedEndDateTime = LocalDateTime.of(selectedEndDate, selectedEndTime);
        LocalDateTime openDateTime = LocalDateTime.of(selectedStartDate, openTime);
        LocalDateTime closeDateTime = openDateTime.plusHours(14);
        if (
                (selectedStartDateTime.isAfter(openDateTime) || selectedStartDateTime.isEqual(openDateTime)) &&
                        (selectedEndDateTime.isAfter(openDateTime) || selectedEndDateTime.isEqual(openDateTime)) &&
                        (selectedStartDateTime.isBefore(closeDateTime) || selectedStartDateTime.isEqual(closeDateTime)) &&
                        (selectedEndDateTime.isBefore(closeDateTime) || selectedEndDateTime.isEqual(closeDateTime))
        )  { return true; }
        return false;
    }
    public void populateTimeBoxes() {
        ObservableList<String> startTimeList = FXCollections.observableArrayList();
        for (int i = 8; i <= 21; ++i) {
            LocalTime estTime = LocalTime.of(i, 0);
            LocalDate estDate = LocalDate.now();
            LocalDateTime estDateTime = LocalDateTime.of(estDate, estTime);
            ZonedDateTime estZonedDateTime = ZonedDateTime.of(estDateTime, estZoneId);
            ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(estZonedDateTime.toInstant(), localZoneId);
            startTimeList.add(localZonedDateTime.toLocalTime().format(timeDTF));
        }
        ObservableList<String> endTimeList = FXCollections.observableArrayList();
        for (int i = 9; i <= 22; ++i) {
            LocalTime estTime = LocalTime.of(i, 0);
            LocalDate estDate = LocalDate.now();
            LocalDateTime estDateTime = LocalDateTime.of(estDate, estTime);
            ZonedDateTime estZonedDateTime = ZonedDateTime.of(estDateTime, estZoneId);
            ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(estZonedDateTime.toInstant(), localZoneId);
            endTimeList.add(localZonedDateTime.toLocalTime().format(timeDTF));
        }
        sortedStartTimeList = startTimeList.sorted();
        sortedEndTimeList = endTimeList.sorted();
        startTimeComboBox.setItems(sortedStartTimeList);
        endTimeComboBox.setItems(endTimeList);
    }
    public void saveAppointment(ActionEvent actionEvent) throws IOException {
        int appointmentId = Integer.parseInt(appointmentIdTextField.getText());
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        LocalDate appointmentStartDate = startDatePicker.getValue();
        LocalDate appointmentEndDate = endDatePicker.getValue();
        LocalTime startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalDateTime startDateTime = LocalDateTime.of(appointmentStartDate, startTime);
        LocalTime endTime = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalDateTime endDateTime = LocalDateTime.of(appointmentEndDate, endTime);
        ZonedDateTime startUtc = startDateTime.atZone(localZoneId).withZoneSameInstant(utcZoneId);
        ZonedDateTime endUtc = endDateTime.atZone(localZoneId).withZoneSameInstant(utcZoneId);
        Timestamp startTimestamp = Timestamp.valueOf(startUtc.toLocalDateTime());
        Timestamp endTimestamp = Timestamp.valueOf(endUtc.toLocalDateTime());
        int contactId = contactIds.get(contactComboBox.getSelectionModel().getSelectedIndex());
        // Find if between open and close
        LocalTime openTime = LocalTime.parse(sortedStartTimeList.get(0));
        LocalDateTime openDateTime = LocalDateTime.of(appointmentStartDate, openTime);
        LocalDateTime closeDateTime = openDateTime.plusHours(14);
        LocalTime closeTime = closeDateTime.toLocalTime();

        if (isInBusinessHours()) {
        try {
            String sqlToUpdate = "UPDATE appointments SET Title='" + title + "', Description='" + description + "', Location='" + location + "', Start='" + startTimestamp + "', End='" + endTimestamp +  "', Type='" + type + "', User_ID=" + User.getUserId() + ", Contact_ID=" + contactId + ", Last_Update=CURRENT_TIMESTAMP, Last_Updated_By='" + User.getUsername() + "' WHERE Appointment_ID=" + appointmentId;
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlToUpdate);
            preparedStatement.executeUpdate();
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            scene.getStylesheets().add("/stylesheet.css");
            stage.setTitle("Customer View");
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Appointment is scheduled outside of business hours. Please schedule an appointment between " + openTime + " and " + closeTime);
            Optional<ButtonType> result = alert.showAndWait();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        DBContact.getAllContacts().forEach((contact) -> {
            String contactName = contact.getContactName();
            contactNames.add(contactName);
            int contactId = contact.getContactId();
            contactIds.addElement(contactId);
        });
        contactComboBox.setItems(contactNames);
        System.out.println(contactIds);
        populateTimeBoxes();
    }
}
