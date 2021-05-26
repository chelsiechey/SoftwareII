package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import utils.DBAppointment;
import utils.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import model.User;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


public class MainController implements Initializable {
    private Parent scene;
    private ZoneId zoneId = ZoneId.systemDefault();
    @FXML
    private Label LogInLabel;
    @FXML
    private Label ZoneIdLabelLogin;
    @FXML
    private Label UsernameLabelLogin;
    @FXML
    private TextField UsernameTextFieldLogin;
    @FXML
    private Label PasswordLabelLogin;
    @FXML
    private Label InvalidLabelLogin;
    @FXML
    private PasswordField PasswordFieldLogin;
    @FXML
    private Button SubmitButtonLogin;

    @FXML
    void SubmitLogin(ActionEvent event) throws IOException {
        String usernameInput = UsernameTextFieldLogin.getText();
        String passwordInput = PasswordFieldLogin.getText();
        int userId = getUserId(usernameInput);
        if (correctPassword(userId, passwordInput)) {
            User user = new User(userId, usernameInput, passwordInput);
            Stage customerViewStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            scene.getStylesheets().add("./stylesheet.css");
            customerViewStage.setTitle("Customer View");
            customerViewStage.setScene(new Scene(scene));
            customerViewStage.show();
            upcomingAppointmentAlert();
        } else {
            try {
                ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.login", Locale.getDefault());
                InvalidLabelLogin.setText(resourceBundle.getString("incorrect"));
            } catch (MissingResourceException e) {
                System.out.println("Resource not found.");
            }
        }
    }

    public void upcomingAppointmentAlert() {
        ObservableList<Timestamp> userAppointmentStartTimes = DBAppointment.getAllUserAppointmentStartTimes(User.getUserId());
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime alertTimePeriod = currentTime.plusMinutes(15);
        userAppointmentStartTimes.forEach((time) -> {
            LocalDateTime appointmentStartTime = time.toLocalDateTime();
            long timeDifference = ChronoUnit.MINUTES.between(currentTime, appointmentStartTime);
            System.out.println("Time difference: " + timeDifference);
            if (currentTime.isBefore(appointmentStartTime) && alertTimePeriod.isAfter(appointmentStartTime)) {
                Alert upcomingAppointmentAlert = new Alert(Alert.AlertType.INFORMATION);
                DialogPane dialogPane = upcomingAppointmentAlert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialog");
                upcomingAppointmentAlert.setHeaderText("You have an upcoming appointment in approximately " + timeDifference + " minute(s)");
                upcomingAppointmentAlert.showAndWait();
            }
        });
    }

    private int getUserId(String username) {
        int userId = -1;
        String sql = "SELECT User_ID FROM users WHERE User_Name ='" + username + "'";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                userId = rs.getInt("User_ID");
                return userId;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userId;
    }

    private boolean correctPassword(int userId, String password) {
        String sql = "SELECT password FROM users WHERE User_ID ='" + userId + "'";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if (rs.getString("password").equals(password)) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /** Initializes the MainController */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            resourceBundle = ResourceBundle.getBundle("languages.login", Locale.getDefault());
            UsernameLabelLogin.setText(resourceBundle.getString("username"));
            PasswordLabelLogin.setText(resourceBundle.getString("password"));
            LogInLabel.setText(resourceBundle.getString("logIn"));
            SubmitButtonLogin.setText(resourceBundle.getString("submit"));
            ZoneIdLabelLogin.setText(zoneId.toString());
            System.out.println("Resources initialized.");
        } catch (MissingResourceException e) {
            System.out.println("Resource not found.");
        }
    }
}
