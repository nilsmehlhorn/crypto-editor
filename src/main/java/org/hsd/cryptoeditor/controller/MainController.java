package org.hsd.cryptoeditor.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane borderPaneLayout;

    @FXML
    public void initialize() throws IOException {
        borderPaneLayout.setTop((Node) FXMLLoader.load(getClass().getResource("/top_view.fxml")));
        borderPaneLayout.setCenter((Node) FXMLLoader.load(getClass().getResource("/editor_view.fxml")));
    }
}
