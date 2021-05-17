package controller;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.Customer;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.DBCustomers;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.fxml.Initializable;

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
    private Label NoSelectionErrorLabel;
    private Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up customer table
        customerTable.setItems(DBCustomers.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
    }

    public void modifyCustomer(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyCustomer.fxml"));
            loader.load();
            // gets the ModifyCustomerController so the selected customer can be passed
            ModifyCustomerController modifyCustomerController = loader.getController();
            System.out.println(modifyCustomerController);
            System.out.println(customerTable.getSelectionModel().getSelectedItem());
            modifyCustomerController.getCustomerToModify(customerTable.getSelectionModel().getSelectedItem());
            Stage modifyCustomerStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            modifyCustomerStage.setTitle("Modify Customer");
            modifyCustomerStage.setScene(new Scene(scene));
            scene.getStylesheets().add("stylesheet.css");
            modifyCustomerStage.show();
        } catch (NullPointerException e) {
            NoSelectionErrorLabel.setText("Please select a customer to modify.");
        }
    }
}


