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
import org.hsd.cryptoeditor.service.DialogService;

import java.security.Security;

public class App extends Application {

    public static void main(String... args) {
        // register provider for bc
        if(Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        // launch jfx
        launch(App.class);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main_view.fxml"));
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Crypto Editor");
        primaryStage.setScene(scene);
        primaryStage.show();

        DialogService.getInstance().setWindow(primaryStage);
    }
}
