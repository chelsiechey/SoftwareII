package controller;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.Customer;
import model.Appointment;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.DBAppointment;
import utils.DBConnection;
import utils.DBCustomers;
import utils.DBCustomerDivision;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;
import java.sql.Timestamp;

public class CustomerController implements Initializable {
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    @FXML
    private TableColumn<Customer, String> customerPostalCodeColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    @FXML
    private TableColumn<Customer, Integer> customerDivisionIdColumn;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, Timestamp> startColumn;
    @FXML
    private TableColumn<Appointment, Timestamp> endColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentUserIdColumn;
    @FXML
    void addAppointment(ActionEvent actionEvent) {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/AddAppointment.fxml"));
                loader.load();
                AddAppointmentController addAppointmentController = loader.getController();
                addAppointmentController.getSelectedCustomersId(customerTable.getSelectionModel().getSelectedItem());
                Stage addAppointmentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                addAppointmentStage.setTitle("Add Appointment");
                addAppointmentStage.setScene(new Scene(scene));
                scene.getStylesheets().add("stylesheet.css");
                addAppointmentStage.show();
            } catch (IOException e) {
                System.out.println("Resource failed to initialize");
            }
        }
        else {
            Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = noCustomerSelectedAlert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            noCustomerSelectedAlert.setHeaderText("Please select a customer to schedule an appointment.");
            noCustomerSelectedAlert.showAndWait();
        }
    }
    @FXML
    void deleteAppointment(ActionEvent event) {

    }
    @FXML
    void modifyAppointment(ActionEvent event) {

    }
    private Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up customer table
        ObservableList<Integer> divisionIdList = FXCollections.observableArrayList();
        DBCustomers.getAllCustomers().forEach((customer) -> {
            int divisionId = customer.getDivisionId();
            divisionIdList.add(divisionId);
        });
        DBCustomerDivision.getAllCustomerDivisions(divisionIdList);
//        customerTable.setItems(DBCustomers.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
//        setCustomerTableValues();
    }

    public void setCustomerTableValues() {
//        customerTable.setItems(DBCustomers.getAllCustomers());
//        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
//        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
//        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
//        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
//        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
//        customerDivisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
    }

    public void setAppointmentTableValues() {
        int customerId = customerTable.getSelectionModel().getSelectedItem().getCustomerId();
        appointmentTable.setItems(DBAppointment.getAllAppointments(customerId));
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    public void getAppointments(MouseEvent actionEvent) throws IOException {
        setAppointmentTableValues();
    }


    public void modifyCustomer(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyCustomer.fxml"));
            loader.load();
            // gets the ModifyCustomerController so the selected customer can be passed
            ModifyCustomerController modifyCustomerController = loader.getController();
            modifyCustomerController.getCustomerToModify(customerTable.getSelectionModel().getSelectedItem());
            Stage modifyCustomerStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            modifyCustomerStage.setTitle("Modify Customer");
            modifyCustomerStage.setScene(new Scene(scene));
            scene.getStylesheets().add("stylesheet.css");
            modifyCustomerStage.show();
        } catch (NullPointerException e) {
            Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = noCustomerSelectedAlert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            noCustomerSelectedAlert.setHeaderText("Please select a customer to modify.");
            noCustomerSelectedAlert.showAndWait();
        }
    }

    public void addCustomer(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AddCustomer.fxml"));
            loader.load();
            Stage addCustomerStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            addCustomerStage.setTitle("Add Customer");
            addCustomerStage.setScene(new Scene(scene));
            scene.getStylesheets().add("stylesheet.css");
            addCustomerStage.show();
        } catch (IOException e) {
            System.out.println("Resource failed to initialize");
        }
    }

    public void deleteCustomer(ActionEvent actionEvent) throws IOException {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            int customerId = customerTable.getSelectionModel().getSelectedItem().getCustomerId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Delete customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    String sql = "DELETE FROM customers WHERE Customer_ID=" + customerId;
                    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                    ps.executeUpdate();
                    setCustomerTableValues();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    DialogPane confirmDialogPane = successAlert.getDialogPane();
                    confirmDialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
                    confirmDialogPane.getStyleClass().add("myConfirmDialog");
                    successAlert.setHeaderText("Customer deleted successfully");
                    successAlert.showAndWait();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        else {
            Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = noCustomerSelectedAlert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            noCustomerSelectedAlert.setHeaderText("Please select a customer to delete.");
            noCustomerSelectedAlert.showAndWait();
        }
    }
}


