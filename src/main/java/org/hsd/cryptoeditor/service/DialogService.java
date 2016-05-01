package org.hsd.cryptoeditor.service;

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
