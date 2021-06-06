package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import utils.DBAppointment;
import utils.DBContact;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class creates the controller for viewing the schedule of each contact in the organization
 */
public class ContactSchedulesController implements Initializable {
    @FXML
    private TableView<Contact> contactTable;
    @FXML
    private TableColumn<Contact, Integer> contactIdColumn;
    @FXML
    private TableColumn<Contact, String> contactNameColumn;
    @FXML
    private TableColumn<Contact, String> contactEmailColumn;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> startDateColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endDateColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerIdColumn;

    /**
     * This method gets the ID of the selected contact
     * @param mouseEvent The MouseEvent object generated when a contact is selected
     */
    @FXML
    void getAppointments(MouseEvent mouseEvent) {
        int contactId = contactTable.getSelectionModel().getSelectedItem().getContactId();
        setAppointmentTableValues(contactId);
    }

    /**
     * This method populates the appointment table with the selected contact's schedule
     * @param contactId The ID of the selected contact
     */
    public void setAppointmentTableValues(int contactId) {
        appointmentTable.setItems(DBAppointment.getAllAppointmentsForContact(contactId));
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    /**
     * This method redirects the user to the customer page
     * @param actionEvent The ActionEvent object generated when the button 'Go Back' is pressed
     * @throws IOException Throws an exception if the fxml file for the Customer page is not found
     */
    @FXML
    void goBack(ActionEvent actionEvent) throws IOException {
        // stage and scene
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customer.fxml")));
        scene.getStylesheets().add("/stylesheet.css");
        stage.setTitle("Customer View");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method initializes the contact schedule controller by populating the contact table
     * with all of the contacts in the database
     * @param url Unused parameter for a URL
     * @param resourceBundle Unused parameter for a resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setContactTableValues();
    }

    /**
     * This method populates the contact table with all of the contacts in the database
     */
    public void setContactTableValues() {
        contactTable.setPlaceholder(new Label("No rows to display"));
        contactTable.setItems(DBContact.getAllContacts());
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        contactNameColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        contactEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

}
