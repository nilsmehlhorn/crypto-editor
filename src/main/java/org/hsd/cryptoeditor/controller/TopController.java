package org.hsd.cryptoeditor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.hsd.cryptoeditor.model.Document;
import org.hsd.cryptoeditor.service.DialogService;
import org.hsd.cryptoeditor.service.DocumentService;

import java.io.File;

public class TopController {
    DocumentService documentService;

    @FXML
    public void initialize() {
        documentService = DocumentService.getInstance();
    }

    public void fileNew(ActionEvent actionEvent) {

    }

    public void save(ActionEvent actionEvent) {
        Document doc = DocumentService.getInstance().currentDocumentProperty().get();
        if(doc.getFile() == null) {
            DocumentService.getInstance().saveCurrent(DialogService.getInstance().showSaveDialog("Save"));
        } else {
            DocumentService.getInstance().saveCurrent(doc.getFile());
        }
    }

    public void open(ActionEvent event) {
        DocumentService.getInstance().load(DialogService.getInstance().showOpenDialog("Open"));
    }
}
