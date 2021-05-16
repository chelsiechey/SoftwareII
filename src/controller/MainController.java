package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import model.User;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


public class MainController implements Initializable {
    private Stage stage;
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
    private PasswordField PasswordFieldLogin;

    @FXML
    private Button SubmitButtonLogin;

    @FXML
    void SubmitLogin(ActionEvent event) throws IOException {
        Parent root;
        Stage stage;
        String usernameInput = UsernameTextFieldLogin.getText();
        String passwordInput = PasswordFieldLogin.getText();
        int userId = getUserId(usernameInput);
        if (correctPassword(userId, passwordInput)) {
            User user = new User(userId, usernameInput, passwordInput);
            Stage userViewStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            scene.getStylesheets().add("./stylesheet.css");
            userViewStage.setTitle("User View");
            userViewStage.setScene(new Scene(scene));
            userViewStage.show();
        } else {
            System.out.println("Invalid password.");
        }
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
