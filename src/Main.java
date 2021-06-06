import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import java.time.ZoneId;
import java.util.Objects;

/** This class creates an App for scheduling appointments */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view/Main.fxml")));
        primaryStage.setTitle("Log In");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** The main method for the application. This is the first method to be called when the Java program is run. */
    public static void main(String[] args) {
        System.out.println(ZoneId.systemDefault());
        DBConnection.establishConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
