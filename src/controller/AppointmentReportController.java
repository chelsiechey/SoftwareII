package controller;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.util.Callback;
import utils.DBAppointment;
import model.Appointment;
import model.Report;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;

import utils.DBAppointment;

public class AppointmentReportController implements Initializable {
    @FXML
    private TableView<Report> appointmentTypeTable;
    @FXML
    private TableColumn<ObservableList<Report>, String> appointmentTypeColumn;
    @FXML
    private TableColumn<ObservableList<Report>, String> numTypeColumn;
    @FXML
    private TableView<Appointment> appointmentMonthTable;
    @FXML
    private TableColumn<Appointment, String> appointmentMonthColumn;

    // stage and scene
    private Stage stage;
    private Parent scene;

    @FXML
    void goBack(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
        scene.getStylesheets().add("/stylesheet.css");
        stage.setTitle("Customer View");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTypeTable.setItems(DBAppointment.getUniqueAppointmentTypesAndCount());
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        numTypeColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
}
