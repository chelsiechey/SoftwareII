package controller;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.net.URL;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;


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
    private TableColumn<Customer, Integer> customerDivisionColumn;
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
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;
    @FXML
    private TableColumn<Appointment, String> startDateColumn;
    @FXML
    private TableColumn<Appointment, String> endDateColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerIdColumn;
    @FXML
    void addAppointment(ActionEvent actionEvent) {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/AddAppointment.fxml"));
                loader.load();
                AppointmentController appointmentController = loader.getController();
                appointmentController.getSelectedCustomersId(customerTable.getSelectionModel().getSelectedItem());
                Stage addAppointmentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                addAppointmentStage.setTitle("Add Appointment");
                addAppointmentStage.setScene(new Scene(scene));
                scene.getStylesheets().add("stylesheet.css");
                addAppointmentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Resource failed to initialize");
            }
        }
        else {
            Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = noCustomerSelectedAlert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            noCustomerSelectedAlert.setHeaderText("Please select a customer to schedule an appointment.");
            noCustomerSelectedAlert.showAndWait();
        }
    }
    @FXML
    public void deleteAppointment(ActionEvent actionEvent)  {
        if (appointmentTable.getSelectionModel().getSelectedItem() != null) {
            Appointment currentAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            int appointmentId = currentAppointment.getAppointmentId();
            int customerId = currentAppointment.getCustomerId();
            String type = currentAppointment.getType();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setHeaderText("Delete appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    String sql = "DELETE FROM appointments WHERE Appointment_ID=" + appointmentId;
                    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                    ps.executeUpdate();
                    setCustomerTableValues();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    DialogPane confirmDialogPane = successAlert.getDialogPane();
                    confirmDialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
                    confirmDialogPane.getStyleClass().add("myConfirmDialog");
                    String headerText = "Appointment " + appointmentId + " for " + type + " was deleted successfully";
                    successAlert.setHeaderText(headerText);
                    successAlert.showAndWait();
                    setAppointmentTableValues(customerId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        else {
            Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = noCustomerSelectedAlert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            noCustomerSelectedAlert.setHeaderText("Please select an appointment to delete.");
            noCustomerSelectedAlert.showAndWait();
        }
    }
    private Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCustomerTableValues();
    }

    public void setCustomerTableValues() {
        customerTable.setPlaceholder(new Label("No rows to display"));
        customerTable.setItems(DBCustomers.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
    }

    public void setAppointmentTableValues(int customerId) {
        appointmentTable.setItems(DBAppointment.getAllAppointments(customerId));
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    public void getAppointments(MouseEvent actionEvent) {
        int customerId = customerTable.getSelectionModel().getSelectedItem().getCustomerId();
        setAppointmentTableValues(customerId);
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
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            noCustomerSelectedAlert.setHeaderText("Please select a customer to modify.");
            noCustomerSelectedAlert.showAndWait();
        }
    }

    public void modifyAppointment(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyAppointment.fxml"));
            loader.load();
            // gets the AppointmentController so the selected customer can be passed
            AppointmentController appointmentController = loader.getController();
            appointmentController.getAppointmentToModify(appointmentTable.getSelectionModel().getSelectedItem());
            Stage modifyAppointmentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            modifyAppointmentStage.setTitle("Modify Appointment");
            modifyAppointmentStage.setScene(new Scene(scene));
            scene.getStylesheets().add("stylesheet.css");
            modifyAppointmentStage.show();
        } catch (NullPointerException e) {
            Alert noAppointmentSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = noAppointmentSelectedAlert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            noAppointmentSelectedAlert.setHeaderText("Please select an appointment to modify.");
            noAppointmentSelectedAlert.showAndWait();
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

    public void deleteCustomer(ActionEvent actionEvent) {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            int customerId = customerTable.getSelectionModel().getSelectedItem().getCustomerId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
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
                    confirmDialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
                    confirmDialogPane.getStyleClass().add("myConfirmDialog");
                    successAlert.setHeaderText("Customer deleted successfully");
                    successAlert.showAndWait();
                    setCustomerTableValues();
                } catch (SQLException throwables ) {
                    if (isConstraintViolation(throwables)) {
                        Alert hasAssociatedAppointments = new Alert(Alert.AlertType.INFORMATION);
                        DialogPane dp = hasAssociatedAppointments.getDialogPane();
                        dp.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
                        dp.getStyleClass().add("myDialog");
                        hasAssociatedAppointments.setHeaderText("All customer appointments must be deleted before continuing.");
                        hasAssociatedAppointments.showAndWait();
                    } else {
                        throwables.printStackTrace();
                    }
                }
            }
        }
        else {
            Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = noCustomerSelectedAlert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            noCustomerSelectedAlert.setHeaderText("Please select a customer to delete.");
            noCustomerSelectedAlert.showAndWait();
        }
    }

    public static boolean isConstraintViolation(SQLException e) {
        return e.getSQLState().startsWith("23");
    }

    public void viewCalendar(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Calendar.fxml"));
            loader.load();
            Stage addCustomerStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            addCustomerStage.setTitle("Calendar");
            addCustomerStage.setScene(new Scene(scene));
            scene.getStylesheets().add("stylesheet.css");
            addCustomerStage.show();
        } catch (IOException e) {
            System.out.println("Resource failed to initialize");
        }
    }

    public void viewCustomerAppointmentReport(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentReport.fxml"));
            loader.load();
            Stage addCustomerStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            addCustomerStage.setTitle("Appointment Report");
            addCustomerStage.setScene(new Scene(scene));
            scene.getStylesheets().add("stylesheet.css");
            addCustomerStage.show();
        } catch (IOException e) {
            System.out.println("Resource failed to initialize");
        }
    }

    public void viewContactSchedules(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ContactSchedules.fxml"));
            loader.load();
            Stage addCustomerStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            addCustomerStage.setTitle("Contact Schedules");
            addCustomerStage.setScene(new Scene(scene));
            scene.getStylesheets().add("stylesheet.css");
            addCustomerStage.show();
        } catch (IOException e) {
            System.out.println("Resource failed to initialize");
        }
    }

    public void viewAppointmentsPerLocation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentsPerLocation.fxml"));
            loader.load();
            Stage addCustomerStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            addCustomerStage.setTitle("Appointments Per Location");
            addCustomerStage.setScene(new Scene(scene));
            scene.getStylesheets().add("stylesheet.css");
            addCustomerStage.show();
        } catch (IOException e) {
            System.out.println("Resource failed to initialize");
        }
    }
}


