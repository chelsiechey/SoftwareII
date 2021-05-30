package controller;

// import statements
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import javafx.scene.Parent;
import model.User;
import utils.DBAppointment;
import utils.DBConnection;
import utils.DBContact;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.fxml.Initializable;

public class AppointmentController implements Initializable {
    // appointment table fields
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

    // Appointment variables
    int appointmentId;
    String title;
    String description;
    String location;
    int contactId;
    String type;
    LocalDate appointmentStartDate;
    LocalDate appointmentEndDate;
    LocalTime startTime;
    LocalDateTime startDateTime;
    LocalTime endTime;
    LocalDateTime endDateTime;
    ZonedDateTime startUtc;
    ZonedDateTime endUtc;
    Timestamp startTimestamp;
    Timestamp endTimestamp;

    // Open and close date/time variables
    LocalTime openTime;
    LocalDateTime openDateTime;
    LocalTime closeTime;
    LocalDateTime closeDateTime;

    // Flag to determine if an appointment is being added or modified
    private boolean modifying;

    // stage and scene
    private Stage stage;
    private Parent scene;

    // Zones, locales and formats
    private final ZoneId localZoneId = ZoneId.systemDefault();
    private final ZoneId utcZoneId = ZoneId.of("UTC");
    private final ZoneId estZoneId = ZoneId.of("America/New_York");
    Locale locale = Locale.getDefault();
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Stores contact IDs so that the selected contact can be saved to the appointment
    Vector<Integer> contactIds = new Vector<>();

    // Initialize the appointment controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifying = false;
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        DBContact.getAllContacts().forEach((contact) -> {
            String contactName = contact.getContactName();
            contactNames.add(contactName);
            int contactId = contact.getContactId();
            contactIds.addElement(contactId);
        });
        contactComboBox.setItems(contactNames);
        populateTimeBoxes();
    }

    // Creates observable lists to store the sorted appointment start and end times
    ObservableList<String> sortedStartTimeList = FXCollections.observableArrayList();
    ObservableList<String> sortedEndTimeList = FXCollections.observableArrayList();

    // Finds the hours the office is open in local time and populates the start and end time combo boxes
    public void populateTimeBoxes() {
        ObservableList<String> startTimeList = FXCollections.observableArrayList();
        for (int i = 8; i <= 21; ++i) {
            LocalTime estTime = LocalTime.of(i, 0);
            LocalDate estDate = LocalDate.now();
            LocalDateTime estDateTime = LocalDateTime.of(estDate, estTime);
            ZonedDateTime estZonedDateTime = ZonedDateTime.of(estDateTime, estZoneId);
            ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(estZonedDateTime.toInstant(), localZoneId);
            startTimeList.add(localZonedDateTime.toLocalTime().format(timeFormat));
        }
        ObservableList<String> endTimeList = FXCollections.observableArrayList();
        for (int i = 9; i <= 22; ++i) {
            LocalTime estTime = LocalTime.of(i, 0);
            LocalDate estDate = LocalDate.now();
            LocalDateTime estDateTime = LocalDateTime.of(estDate, estTime);
            ZonedDateTime estZonedDateTime = ZonedDateTime.of(estDateTime, estZoneId);
            ZonedDateTime localZonedDateTime = ZonedDateTime.ofInstant(estZonedDateTime.toInstant(), localZoneId);
            endTimeList.add(localZonedDateTime.toLocalTime().format(timeFormat));
        }
        sortedStartTimeList = startTimeList.sorted();
        sortedEndTimeList = endTimeList.sorted();
        startTimeComboBox.setItems(sortedStartTimeList);
        endTimeComboBox.setItems(sortedEndTimeList);
    }

    // Variables to hold the start and end date/time of the appointment to be modified
    LocalDateTime originalStartDateTime;
    LocalDateTime originalEndDateTime;

    // Gets the selected appointment to modify
    public void getAppointmentToModify(Appointment appointmentToModify) {
        appointmentIdTextField.setText(String.valueOf(appointmentToModify.getAppointmentId()));
        titleTextField.setText(String.valueOf(appointmentToModify.getTitle()));
        descriptionTextField.setText(String.valueOf(appointmentToModify.getDescription()));
        locationTextField.setText(String.valueOf(appointmentToModify.getLocation()));
        contactComboBox.getSelectionModel().select(String.valueOf(appointmentToModify.getContact()));
        typeTextField.setText(String.valueOf(appointmentToModify.getType()));
        originalStartDateTime = LocalDateTime.parse(appointmentToModify.getStart(), dateTimeFormat);
        originalEndDateTime = LocalDateTime.parse(appointmentToModify.getEnd(), dateTimeFormat);
        LocalDate appointmentStartDate = originalStartDateTime.toLocalDate();
        LocalDate appointmentEndDate = originalEndDateTime.toLocalDate();
        String startTime = originalStartDateTime.toLocalTime().format(timeFormat);
        String endTime = originalEndDateTime.toLocalTime().format(timeFormat);
        startTimeComboBox.getSelectionModel().select(startTime);
        endTimeComboBox.getSelectionModel().select(endTime);
        startDatePicker.setValue(appointmentStartDate);
        endDatePicker.setValue(appointmentEndDate);
    }

    // Variable for the customer ID
    private int customerId;

    // Gets the customer ID
    public void getSelectedCustomersId(Customer selectedCustomer) {
        customerId = selectedCustomer.getCustomerId();
    }

    // Confirms if the user would like to cancel adding/modifying their appointment and takes them to the customer page
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

    public void initializeValues() {
        if (modifying == true) {
            appointmentId = Integer.parseInt(appointmentIdTextField.getText());
        }
        title = titleTextField.getText();
        description = descriptionTextField.getText();
        location = locationTextField.getText();
        contactId = contactIds.get(contactComboBox.getSelectionModel().getSelectedIndex());
        type = typeTextField.getText();
        appointmentStartDate = startDatePicker.getValue();
        appointmentEndDate = endDatePicker.getValue();
        startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem(), timeFormat);
        startDateTime = LocalDateTime.of(appointmentStartDate, startTime);
        endTime = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem(), timeFormat);
        endDateTime = LocalDateTime.of(appointmentEndDate, endTime);
        startUtc = startDateTime.atZone(localZoneId).withZoneSameInstant(utcZoneId);
        endUtc = endDateTime.atZone(localZoneId).withZoneSameInstant(utcZoneId);
        startTimestamp = Timestamp.valueOf(startUtc.toLocalDateTime());
        endTimestamp = Timestamp.valueOf(endUtc.toLocalDateTime());
        openTime = LocalTime.parse(sortedStartTimeList.get(0));
        openDateTime = LocalDateTime.of(appointmentStartDate, openTime);
        closeDateTime = openDateTime.plusHours(14);
        closeTime = closeDateTime.toLocalTime();
    }

    // Creates an appointment or displays an error describing why the appointment cannot be scheduled
    public void createAppointment(ActionEvent actionEvent) {
        initializeValues();
        // Check to see if appointment has a conflict or is scheduled outside of business hours
        if (isInBusinessHours() && doesNotHaveConflict()) {
            // Attempts to insert appointment into database
            try {
                String sql = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                        "VALUES ('" + title + "', '" + description + "', '" + location + "', '" + type + "', '" + startTimestamp + "', '" + endTimestamp + "', CURRENT_TIMESTAMP, '" + User.getUsername() + "', CURRENT_TIMESTAMP, '" + User.getUsername() + "', " + this.customerId + ", " + User.getUserId() + ", " + contactId + ")" ;
                PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
                preparedStatement.executeUpdate();
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
                scene.getStylesheets().add("/stylesheet.css");
                stage.setTitle("Customer View");
                stage.setScene(new Scene(scene));
                stage.show();
            // catch any SQL exceptions or failure to load resources
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        // Displays an error that the appointment is outside of business hours
        } else {
            displayAppropriateError();
        }
    }

    public void saveAppointment(ActionEvent actionEvent) {
        // Flag to indicate that the appointment is being modified not created
        modifying = true;
        initializeValues();
        // Check to see if appointment has a conflict or is scheduled outside of business hours
        if (isInBusinessHours() && doesNotHaveConflict()) {
            // Attempts to insert appointment into database
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
            // Displays an error that the appointment is outside of business hours
        } else {
            displayAppropriateError();
        }
    }

    public void displayAppropriateError() {
        if (!isInBusinessHours() && doesNotHaveConflict()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Appointment is scheduled outside of business hours. Please schedule an appointment between " + openTime + " and " + closeTime);
            Optional<ButtonType> result = alert.showAndWait();
            // Displays an error that the appointment conflicts with an existing appointment
        } else if (isInBusinessHours() && !doesNotHaveConflict()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Appointment conflicts with an existing appointment.");
            Optional<ButtonType> result = alert.showAndWait();
            // Displays an error that the appointment is outside of business hours and conflicts with an existing appointment
        } else if (!isInBusinessHours() && !doesNotHaveConflict()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Appointment conflicts with an existing appointment and is scheduled outside of business hours.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    // Determine if selected appointment is in local business hours
    public boolean isInBusinessHours() {
        if (
            (startDateTime.isAfter(openDateTime) || startDateTime.isEqual(openDateTime)) &&
            (endDateTime.isAfter(openDateTime) || endDateTime.isEqual(openDateTime)) &&
            (startDateTime.isBefore(closeDateTime) || startDateTime.isEqual(closeDateTime)) &&
            (endDateTime.isBefore(closeDateTime) || endDateTime.isEqual(closeDateTime))
        )  { return true; }
        return false;
    }

    // Determine if selected appointment conflicts with an existing appointment
    public boolean doesNotHaveConflict() {
        ObservableList<Timestamp> userAppointmentStartTimes = DBAppointment.getAllUserAppointmentStartTimes(User.getUserId());
        ObservableList<Timestamp> userAppointmentEndTimes = DBAppointment.getAllUserAppointmentEndTimes(User.getUserId());
        ObservableList<LocalDateTime> userAppointmentStartDateTime = FXCollections.observableArrayList();
        ObservableList<LocalDateTime> userAppointmentEndDateTime = FXCollections.observableArrayList();

        userAppointmentStartTimes.forEach((appointment) -> {
            LocalDateTime dbAppointment = appointment.toLocalDateTime();
            if (modifying == true) {
                if (!dbAppointment.isEqual(originalStartDateTime)) {
                    userAppointmentStartDateTime.add(dbAppointment);
                }
            } else {
                userAppointmentStartDateTime.add(dbAppointment);
            }

        });
        userAppointmentEndTimes.forEach((appointment) -> {
            LocalDateTime dbAppointment = appointment.toLocalDateTime();
            if (modifying == true) {
                if (!dbAppointment.isEqual(originalEndDateTime)) {
                    userAppointmentEndDateTime.add(dbAppointment);
                }
            } else {
                userAppointmentEndDateTime.add(dbAppointment);
            }
        });
        for (int i = 0; i < userAppointmentStartDateTime.size(); ++i) {
            System.out.println("DB appointment start: " + userAppointmentStartDateTime.get(i) + " DB appointment end: " + userAppointmentEndDateTime.get(i));
            if (
                // if start time is before an existing appointment start time but the end time is after the existing appointments end time (surrounds)
                    (startDateTime.isBefore(userAppointmentStartDateTime.get(i)) && endDateTime.isAfter(userAppointmentEndDateTime.get(i))) ||
                            // if start time is after an exiting appointment start time but the end time is before the existing appointments end time (surrounded)
                            (startDateTime.isAfter(userAppointmentStartDateTime.get(i)) && endDateTime.isBefore(userAppointmentEndDateTime.get(i))) ||
                            // if start time is before an existing appointment start time but the end time is between the existing appointments start and end time (overlaps earlier)
                            (startDateTime.isBefore(userAppointmentStartDateTime.get(i)) && endDateTime.isAfter(userAppointmentStartDateTime.get(i)) && endDateTime.isBefore(userAppointmentEndDateTime.get(i))) ||
                            // if start time is between an existing appointments start and end time but the end time is after the existing appointments end time (overlaps after)
                            (startDateTime.isAfter(userAppointmentStartDateTime.get(i)) && startDateTime.isBefore(userAppointmentEndDateTime.get(i)) && endDateTime.isAfter(userAppointmentEndDateTime.get(i))) ||
                            // if start time is equal to an existing appointments start time
                            (startDateTime.isEqual(userAppointmentStartDateTime.get(i))) ||
                            // if end time is equal to an existing appointments end time
                            (endDateTime.isEqual(userAppointmentEndDateTime.get(i)))
            ) {
                return false;
            }
        }
        return true;
    };
}

