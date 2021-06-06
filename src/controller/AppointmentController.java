package controller;

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

/**
 * This class creates the controller for adding and modifying appointments
 */
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
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private Integer contactId;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;

    // Open and close date/time variables
    private LocalTime openTime;
    private LocalDateTime openDateTime;
    private LocalTime closeTime;
    private LocalDateTime closeDateTime;

    // Flag to determine if an appointment is being added or modified
    private boolean modifying;

    // stage and scene
    private Stage stage;
    private Parent scene;

    // Zones, locales and formats
    private final ZoneId localZoneId = ZoneId.systemDefault();
    private final ZoneId utcZoneId = ZoneId.of("UTC");
    private final ZoneId estZoneId = ZoneId.of("America/New_York");
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Stores contact IDs so that the selected contact can be saved to the appointment
    private final Vector<Integer> contactIds = new Vector<>();

    /**
     * This method initializes the appointment controller
     * by populating the contact combo box with contacts
     * and start and end time combo boxes with available appointment times.
     * It uses a lambda expression as a parameter for the forEach operation.
     * Lambda expressions allow you to customize how aggregate operations process elements from a stream.
     * @param url Unused parameter for a URL
     * @param resourceBundle Unused parameter for a resource bundle
     */
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
    private ObservableList<String> sortedStartTimeList = FXCollections.observableArrayList();


    /**
     * This method finds the hours the office is open in local time and populates the start and end time combo boxes
     */
    public void populateTimeBoxes() {
        ObservableList<String> startTimeList = FXCollections.observableArrayList();
        for (int i = 8; i <= 21; ++i) {
            LocalTime estTime = LocalTime.of(i, 0);
            LocalDate estDate = LocalDate.now();
            LocalDateTime estDateTime = LocalDateTime.of(estDate, estTime);
            var estZonedDateTime = ZonedDateTime.of(estDateTime, estZoneId);
            var localZonedDateTime = ZonedDateTime.ofInstant(estZonedDateTime.toInstant(), localZoneId);
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
        ObservableList<String> sortedEndTimeList = endTimeList.sorted();
        startTimeComboBox.setItems(sortedStartTimeList);
        endTimeComboBox.setItems(sortedEndTimeList);
    }

    // Variables to hold the start and end date/time of the appointment to be modified
    private LocalDateTime originalStartDateTime;
    private LocalDateTime originalEndDateTime;

    // Gets the selected appointment to modify

    /**
     * This method populates the appointment data in the fields
     * @param appointmentToModify The Appointment the user selected to modify
     */
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

    /**
     * This method is called from the Customer Controller and gets the customer ID for the selected Customer
     * @param selectedCustomer The Customer selected in the Customer table
     */
    public void getSelectedCustomersId(Customer selectedCustomer) {
        customerId = selectedCustomer.getCustomerId();
    }

    /**
     * This method confirms if the user would actually like to cancel adding/modifying an appointment
     * and redirects them to the customer page if yes
     * or stays on the add/modify appointment page if no
     * @param actionEvent The ActionEvent object generated when the cancel button is pressed
     * @throws IOException Throws an exception if the fxml file for the Customer page is not found
     */
    @FXML
    void handleCancel(ActionEvent actionEvent) throws IOException {
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
     * This method gets the information entered into the add/modify appointment fields
     */
    public boolean valuesInitialized() {
            if (modifying) {
                appointmentId = Integer.parseInt(appointmentIdTextField.getText());
            }
            title = titleTextField.getText();
            if (title.equals("")) { return false; }
            description = descriptionTextField.getText();
            if (description.equals("")) { return false; }
            location = locationTextField.getText();
            if (location.equals("")) { return false; }
            String contact = contactComboBox.getSelectionModel().getSelectedItem();
            if (contact.equals("")) {
                return false;
            }
            System.out.println("Contact name is not empty");
            contactId = contactIds.get(contactComboBox.getSelectionModel().getSelectedIndex());
            System.out.println("Contact ID assigned");
            type = typeTextField.getText();
            if (type.equals("")) { return false; }
            LocalDate appointmentStartDate = startDatePicker.getValue();
            if (appointmentStartDate == null) { return false; }
            LocalDate appointmentEndDate = endDatePicker.getValue();
            if (appointmentEndDate == null) { return false; }
            LocalTime startTime = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem(), timeFormat);
            if (startTime == null) { return false; }
            startDateTime = LocalDateTime.of(appointmentStartDate, startTime);
            LocalTime endTime = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem(), timeFormat);
            endDateTime = LocalDateTime.of(appointmentEndDate, endTime);
            ZonedDateTime startUtc = startDateTime.atZone(localZoneId).withZoneSameInstant(utcZoneId);
            ZonedDateTime endUtc = endDateTime.atZone(localZoneId).withZoneSameInstant(utcZoneId);
            startTimestamp = Timestamp.valueOf(startUtc.toLocalDateTime());
            endTimestamp = Timestamp.valueOf(endUtc.toLocalDateTime());
            openTime = LocalTime.parse(sortedStartTimeList.get(0));
            openDateTime = LocalDateTime.of(appointmentStartDate, openTime);
            closeDateTime = openDateTime.plusHours(14);
            closeTime = closeDateTime.toLocalTime();
            return true;
    }



    /**
     * This method creates an appointment or displays an error describing why the appointment cannot be scheduled
     * @param actionEvent The ActionEvent object generated when the save button is pressed on the add appointment page
     */
    public void createAppointment(ActionEvent actionEvent) {
        // Check to see if appointment has a conflict or is scheduled outside of business hours
        if (valuesInitialized()) {
            if (isInBusinessHours() && doesNotHaveConflict()) {
                // Attempts to insert appointment into database
                try {
                    System.out.println("User ID: " + User.getUserId());
                    String sql = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                            "VALUES ('" + title + "', '" + description + "', '" + location + "', '" + type + "', '" + startTimestamp + "', '" + endTimestamp + "', CURRENT_TIMESTAMP, '" + User.getUsername() + "', CURRENT_TIMESTAMP, '" + User.getUsername() + "', " + this.customerId + ", " + User.getUserId() + ", " + contactId + ")";
                    PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
                    preparedStatement.executeUpdate();
                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customer.fxml")));
                    scene.getStylesheets().add("/stylesheet.css");
                    stage.setTitle("Customer View");
                    stage.setScene(new Scene(scene));
                    stage.show();
                    // catch any SQL exceptions or failure to load resources
                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                displayAppropriateError();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            System.out.println("Attempts to get stylesheet here.");
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            System.out.println("Should have stylesheet by here.");
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Please fill in all fields with the appropriate data type.");
            alert.showAndWait();
        }
    }

    /**
     * This method updates an appointment or displays an error describing why the appointment cannot be scheduled
     * @param actionEvent The ActionEvent object generated when the save button is pressed on the modify appointment page
     */
    public void saveAppointment(ActionEvent actionEvent) {
        // Flag to indicate that the appointment is being modified not created
        System.out.println("Reached save appointment");
        modifying = true;
        // Check to see if appointment has a conflict or is scheduled outside of business hours
        if (valuesInitialized()) {
            System.out.println("Values were initialized.");
            if (isInBusinessHours() && doesNotHaveConflict()) {
                // Attempts to insert appointment into database
                try {
                    String sqlToUpdate = "UPDATE appointments SET Title='" + title + "', Description='" + description + "', Location='" + location + "', Start='" + startTimestamp + "', End='" + endTimestamp + "', Type='" + type + "', User_ID=" + User.getUserId() + ", Contact_ID=" + contactId + ", Last_Update=CURRENT_TIMESTAMP, Last_Updated_By='" + User.getUsername() + "' WHERE Appointment_ID=" + appointmentId;
                    PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlToUpdate);
                    preparedStatement.executeUpdate();
                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customer.fxml")));
                    scene.getStylesheets().add("/stylesheet.css");
                    stage.setTitle("Customer View");
                    stage.setScene(new Scene(scene));
                    stage.show();
                } catch (SQLException | IOException throwables) {
                    System.out.println("SQL/IO Exception catch reached.");
                    throwables.printStackTrace();
                }
                // Displays an error that the appointment is outside of business hours
            } else {
                System.out.println("Reached else to display appropriate error.");
                displayAppropriateError();
            }
        } else {
            System.out.println("Reached else.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            System.out.println("Attempts to get stylesheet here.");
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            System.out.println("Should have stylesheet by here.");
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Please fill in all fields with the appropriate data type.");
            alert.showAndWait();
        }
    }

    /**
     * This method displays an error if the appointment cannot be scheduled
     */
    public void displayAppropriateError() {
        if (!isInBusinessHours() && doesNotHaveConflict()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Appointment is scheduled outside of business hours. Please schedule an appointment between " + openTime + " and " + closeTime);
            alert.showAndWait();
            // Displays an error that the appointment conflicts with an existing appointment
        } else if (isInBusinessHours() && !doesNotHaveConflict()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Appointment conflicts with an existing appointment.");
            alert.showAndWait();
            // Displays an error that the appointment is outside of business hours and conflicts with an existing appointment
        } else if (!isInBusinessHours() && !doesNotHaveConflict()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Appointment conflicts with an existing appointment and is scheduled outside of business hours.");
            alert.showAndWait();
        } else {
            System.out.println("Unknown error");
        }
    }

    /**
     * This method determines if the selected appointment is in local business hours
     * @return Returns true if the appointment is in business hours or false if outside of business hours
     */
    public boolean isInBusinessHours() {
        return (startDateTime.isAfter(openDateTime) || startDateTime.isEqual(openDateTime)) &&
                (endDateTime.isAfter(openDateTime) || endDateTime.isEqual(openDateTime)) &&
                (startDateTime.isBefore(closeDateTime) || startDateTime.isEqual(closeDateTime)) &&
                (endDateTime.isBefore(closeDateTime) || endDateTime.isEqual(closeDateTime));
    }

    /**
     * This method determines if the selected appointment conflicts with another of the user's appointments
     * @return Returns true if the appointment does not have a conflict or false if it does
     */
    public boolean doesNotHaveConflict() {
        ObservableList<Timestamp> userAppointmentStartTimes = DBAppointment.getAllUserAppointmentStartTimes(User.getUserId());
        ObservableList<Timestamp> userAppointmentEndTimes = DBAppointment.getAllUserAppointmentEndTimes(User.getUserId());
        ObservableList<LocalDateTime> userAppointmentStartDateTime = FXCollections.observableArrayList();
        ObservableList<LocalDateTime> userAppointmentEndDateTime = FXCollections.observableArrayList();

        userAppointmentStartTimes.forEach((appointment) -> {
            LocalDateTime dbAppointment = appointment.toLocalDateTime();
            if (modifying) {
                if (!dbAppointment.isEqual(originalStartDateTime)) {
                    userAppointmentStartDateTime.add(dbAppointment);
                }
            } else {
                userAppointmentStartDateTime.add(dbAppointment);
            }

        });

        userAppointmentEndTimes.forEach((appointment) -> {
            LocalDateTime dbAppointment = appointment.toLocalDateTime();
            if (modifying) {
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
            )
            {
                return false;
            }
        }
        return true;
    }
}

