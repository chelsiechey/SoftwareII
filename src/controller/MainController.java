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
import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import model.User;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * This class creates the main controller
 */
public class MainController implements Initializable {
    private final ZoneId zoneId = ZoneId.systemDefault();
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

    /**
     * This method determines if the user put in a valid login and either gives an error message
     * or brings the user to the contact page.
     * @param event The ActionEvent object generated when the submit button is pressed
     * @throws IOException Throws an exception if the fxml file for the Customer page is not found
     */
    @FXML
    public void SubmitLogin(ActionEvent event) throws IOException {
        String usernameInput = UsernameTextFieldLogin.getText();
        String passwordInput = PasswordFieldLogin.getText();
        int userId = getUserId(usernameInput);
        if (correctPassword(userId, passwordInput)) {
            successLog();
            Stage customerViewStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customer.fxml")));
            scene.getStylesheets().add("stylesheet.css");
            customerViewStage.setTitle("Customer View");
            customerViewStage.setScene(new Scene(scene));
            customerViewStage.show();
            upcomingAppointmentAlert();
        } else {
            failureLog();
            try {
                ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.login", Locale.getDefault());
                InvalidLabelLogin.setText(resourceBundle.getString("incorrect"));
            } catch (MissingResourceException e) {
                System.out.println("Resource not found.");
            }
        }
    }

    /**
     * This method alerts the user if they have an appointment within 15 minutes of logging in.
     */
    public void upcomingAppointmentAlert() {
        ObservableList<Timestamp> userAppointmentStartTimes = DBAppointment.getAllUserAppointmentStartTimes(User.getUserId());
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime alertTimePeriod = currentTime.plusMinutes(15);
        // lambda expression
        userAppointmentStartTimes.forEach((time) -> {
            LocalDateTime appointmentStartTime = time.toLocalDateTime();
            long timeDifference = ChronoUnit.MINUTES.between(currentTime, appointmentStartTime);
            System.out.println("Time difference: " + timeDifference);
            if (currentTime.isBefore(appointmentStartTime) && alertTimePeriod.isAfter(appointmentStartTime)) {
                Alert upcomingAppointmentAlert = new Alert(Alert.AlertType.INFORMATION);
                DialogPane dialogPane = upcomingAppointmentAlert.getDialogPane();
                dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheet.css")).toExternalForm());
                dialogPane.getStyleClass().add("myDialog");
                upcomingAppointmentAlert.setHeaderText("You have an upcoming appointment in approximately " + timeDifference + " minute(s)");
                upcomingAppointmentAlert.showAndWait();
            }
        });
    }

    /**
     * This method attempts to find the user ID associated with the username
     * @param username The username entered by the user
     * @return Returns the user ID associated with the entered username or -1 if the username is not in the database
     */
    private int getUserId(String username) {
        int userId = -1;
        String sql = "SELECT User_ID FROM users WHERE User_Name ='" + username + "'";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userId = rs.getInt("User_ID");
            }
            return userId;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userId;
    }

    /**
     * This method determines if the user entered the correct password
     * @param userId The user ID
     * @param password The user's password
     * @return Returns true if the password is correct or false if incorrect
     */
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

    /**
     * This method logs if the user logged in successfully
     */
    public void successLog() {
        try {
            String logFile = "login_activity.txt";
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            LocalDate currentDate = java.time.LocalDate.now();
            FileWriter fileWriter = new FileWriter(logFile, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("User login attempt: ");
            printWriter.println("Timestamp: " + currentTimestamp);
            printWriter.println("Date: " + currentDate);
            printWriter.println("Login successful.");
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method logs if the user entered incorrect login information
     */
    public void failureLog() {
        try {
            String logFile = "login_activity.txt";
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            LocalDate currentDate = java.time.LocalDate.now();
            FileWriter fileWriter = new FileWriter(logFile, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("User login attempt: ");
            printWriter.println("Timestamp: " + currentTimestamp);
            printWriter.println("Date: " + currentDate);
            printWriter.println("Login failed.");
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method initializes the main controller
     * @param url Unused parameter for a url
     * @param resourceBundle The ResourceBundle for translating the login page based on the user's locale
     */
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
