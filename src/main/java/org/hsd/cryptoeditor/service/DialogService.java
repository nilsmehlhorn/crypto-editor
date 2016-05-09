package org.hsd.cryptoeditor.service;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * Created by nils on 5/1/16.
 */
public class DialogService {

    private Window window;

    public void setWindow(Window window) {
        this.window = window;
    }

    public File showOpenDialog(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(window);
    }

    public File showSaveDialog(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showSaveDialog(window);
    }

    public void showErrorDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /////

    private static final DialogService instance = new DialogService();

    private DialogService() {
        if(instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static DialogService getInstance() {
        return instance;
    }
}
