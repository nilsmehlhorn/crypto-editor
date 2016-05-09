package org.hsd.cryptoeditor.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.hsd.cryptoeditor.model.Document;
import org.hsd.cryptoeditor.service.DocumentService;

/**
 * Created by nils on 5/1/16.
 */
public class EditorController {

    @FXML
    private TextArea editorField;

    private ObjectProperty<Document> documentProperty;

    @FXML
    public void initialize() {
        DocumentService documentService = DocumentService.getInstance();
        documentProperty = documentService.currentDocumentProperty();
        editorField.textProperty().bindBidirectional(documentProperty.getValue().textProperty());
        documentProperty.addListener((observable, oldValue, newValue) -> {
            editorField.textProperty().bindBidirectional(newValue.textProperty());
        });
    }



}
