package org.hsd.cryptoeditor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.model.Document;
import org.hsd.cryptoeditor.dialog.DialogService;
import org.hsd.cryptoeditor.crypto.encryption.NoEncryption;
import org.hsd.cryptoeditor.logic.DocumentService;

import java.util.Optional;

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
        if (doc.getFile() == null) {
            DocumentService.getInstance().saveCurrent(DialogService.getInstance().showSaveDialog("Save"));
        } else {
            DocumentService.getInstance().saveCurrent(doc.getFile());
        }
    }

    public void open(ActionEvent event) {
        DocumentService.getInstance().load(DialogService.getInstance().showOpenDialog("Open"));
    }

    public void encryption(ActionEvent event) {
        Optional<Encryption> optional = DialogService.getInstance()
                .showEncryptionDialog(documentService.currentDocumentProperty().get().getEncryption());
        if (optional.isPresent()) {
            documentService.currentDocumentProperty().get().setEncryption(optional.get());
        }

    }
}
