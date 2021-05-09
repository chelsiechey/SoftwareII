import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import model.Country;
import utils.DBCountries;
import javafx.collections.*;

/** This class creates an App for scheduling appointments */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/Main.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /** The main method for the application. This is the first method to be called when the Java program is run. */
    public static void main(String[] args) {
        DBConnection.establishConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
