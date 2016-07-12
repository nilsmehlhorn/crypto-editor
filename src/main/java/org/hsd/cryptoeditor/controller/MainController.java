package org.hsd.cryptoeditor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane borderPaneLayout;

    /**
     * Called by the FXML-Loader after the view is assembled.
     * Will initialize view ares for toolbar and editor text-input.
     */
    @FXML
    public void initialize() throws IOException {
        borderPaneLayout.setTop((Node) FXMLLoader.load(getClass().getResource("/top.fxml")));
        borderPaneLayout.setCenter((Node) FXMLLoader.load(getClass().getResource("/editor.fxml")));
    }
}
