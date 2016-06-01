package org.hsd.cryptoeditor;
/**
 * Created by nils on 18.04.2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hsd.cryptoeditor.dialog.DialogService;

import java.io.IOException;
import java.security.Security;

public class App extends Application {

    /**
     * Default starting point for the application.
     * It will initialize an appropriate environment and start the JavaFX application.
     *
     * @param args execution parameters
     */
    public static void main(String... args) {
        // register provider for bc
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        // launch jfx
        launch(App.class);
    }

    /**
     * Starting method to be called by JavaFX framework
     * @param primaryStage base stage for ui application
     * @throws IOException when resource for main view can not be loaded
     */
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main_view.fxml"));
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Crypto Editor");
        primaryStage.setScene(scene);
        primaryStage.show();

        DialogService.getInstance().setWindow(primaryStage);
    }
}
