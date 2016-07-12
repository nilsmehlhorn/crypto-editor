package org.hsd.cryptoeditor.controller;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.hsd.cryptoeditor.doc.Document;
import org.hsd.cryptoeditor.doc.DocumentService;


/**
 * Controller for handling the editor text-input.
 */
public class EditorController {

    @FXML
    private TextArea editorField;

    private ObjectProperty<Document> documentProperty;


    /**
     * Called by the FXML-Loader after the view is assembled.
     * Will bind the text-field to the document-property exposed by the document-service.
     *
     * @see DocumentService#currentDocumentProperty()
     */
    @FXML
    public void initialize() {
        DocumentService documentService = DocumentService.getInstance();
        documentProperty = documentService.currentDocumentProperty();
        editorField.textProperty().bindBidirectional(documentProperty.getValue().textProperty());
        documentProperty.addListener((observable, oldValue, newValue) -> {
            editorField.textProperty().bindBidirectional(newValue.textProperty());
        });
        editorField.setOnKeyPressed(event -> documentProperty.get().setHasUnsavedChanges(true));
    }


}
