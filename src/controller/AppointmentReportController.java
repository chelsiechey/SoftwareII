package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import utils.DBAppointment;
import model.Report;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class creates the controller for viewing the number of appointments by each appointment type and month
 */
public class AppointmentReportController implements Initializable {
    @FXML
    private TableView<Report> appointmentTypeTable;
    @FXML
    private TableColumn<Report, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Report, String> countTypeColumn;
    @FXML
    private TableView<Report> appointmentMonthTable;
    @FXML
    private TableColumn<Report, String> appointmentMonthColumn;
    @FXML
    private TableColumn<Report, String> countMonthColumn;

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
     * This method initializes the appointment report controller
     * by setting the values in the appointment type table to display each appointment type and it's count
     * and setting the values in the appointment month table to display each month an appointment is scheduled
     * and how many are scheduled in that month
     * @param url Unused parameter for a URL
     * @param resourceBundle Unused parameter for a resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTypeTable.setItems(DBAppointment.getUniqueAppointmentTypesAndCount());
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        countTypeColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        appointmentMonthTable.setItems(DBAppointment.getUniqueAppointmentMonthsAndCount());
        appointmentMonthColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        countMonthColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
}
