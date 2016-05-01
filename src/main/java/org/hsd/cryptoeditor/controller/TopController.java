package org.hsd.cryptoeditor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.hsd.cryptoeditor.service.DialogService;
import org.hsd.cryptoeditor.service.TextService;

public class TopController {
    TextService textService;

    @FXML
    public void initialize() {
        textService = TextService.getInstance();
    }

    public void fileNew(ActionEvent actionEvent) {

    }

    public void save(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
    }

    public void open(ActionEvent event) {
        TextService.getInstance().load(DialogService.getInstance().showOpenDialog("Open"));
    }
}
