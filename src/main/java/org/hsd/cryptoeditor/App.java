package org.hsd.cryptoeditor; /**
 * Created by nils on 18.04.2016.
 */
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{

    public static void main(String... args) {
        launch(App.class);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main_view.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Crypto Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
