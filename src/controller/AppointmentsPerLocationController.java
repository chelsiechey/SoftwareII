package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.DBAppointment;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import model.Report;

/**
 * This class creates the controller for viewing the number of appointments at each location
 */
public class AppointmentsPerLocationController implements Initializable {
    @FXML
    private TableView<Report> appointmentsPerLocationTable;
    @FXML
    private TableColumn<Report, String> locationColumn;
    @FXML
    private TableColumn<Report, Integer> numAppointmentsColumn;

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
     * This method initializes the appointments per location controller
     * by setting the values in the appointment table to display
     * the number of appointments at each location
     * @param url Unused parameter for a URL
     * @param resourceBundle Unused parameter for a resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentsPerLocationTable.setItems(DBAppointment.getAppointmentLocationAndCount());
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        numAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
}
