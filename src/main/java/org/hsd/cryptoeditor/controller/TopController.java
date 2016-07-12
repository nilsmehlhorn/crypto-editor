package org.hsd.cryptoeditor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.doc.Document;
import org.hsd.cryptoeditor.dialog.DialogService;
import org.hsd.cryptoeditor.doc.DocumentService;

import java.util.Optional;

/**
 * Controller for handling the toolbar of the application.
 */
public class TopController {

    private DocumentService documentService;

    @FXML
    public void initialize() {
        documentService = DocumentService.getInstance();
    }

    /**
     * Action method for initializing the editor with a new document.
     *
     * @param actionEvent event
     */
    public void fileNew(ActionEvent actionEvent) {
        documentService.initNewDocument();
    }

    /**
     * Action method for saving the document currently in editing.
     * If the document has already been saved at some point it will be fast-saved to the same file.
     *
     * @param actionEvent event
     */
    public void save(ActionEvent actionEvent) {
        Document doc = documentService.currentDocumentProperty().get();
        if (doc.getFile() == null) {
            documentService.saveCurrent(DialogService.getInstance().showSaveDialog("Save"));
        } else {
            documentService.saveCurrent(doc.getFile());
        }
    }

    /**
     * Action method for explicitly saving a document to a user chosen file.
     *
     * @param actionEvent event
     */
    public void saveAs(ActionEvent actionEvent) {
        documentService.saveCurrent(DialogService.getInstance().showSaveDialog("Save as"));
    }

    /**
     * Action method for opening a document-file for editing.
     *
     * @param actionEvent event
     */
    public void open(ActionEvent actionEvent) {
        documentService.load(DialogService.getInstance().showOpenDialog("Open"));
    }

    /**
     * Action method for opening a dialog for encryption selection.
     *
     * @param actionEvent event
     */
    public void encryption(ActionEvent actionEvent) {
        Optional<Encryption> optional = DialogService.getInstance()
                .showEncryptionDialog(documentService.currentDocumentProperty().get().getEncryption());
        if (optional.isPresent()) {
            documentService.currentDocumentProperty().get().setEncryption(optional.get());
        }

    }
}
