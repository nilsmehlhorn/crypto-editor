package org.hsd.cryptoeditor.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.hsd.cryptoeditor.model.TextEntity;
import org.hsd.cryptoeditor.service.TextService;

/**
 * Created by nils on 5/1/16.
 */
public class EditorController {

    @FXML
    private TextArea editorField;

    private ObjectProperty<TextEntity> textEntityProperty;

    @FXML
    public void initialize() {
        TextService textService = TextService.getInstance();
        textEntityProperty = textService.currentTextEntityProperty();
        textEntityProperty.addListener(new ChangeListener<TextEntity>() {
            public void changed(ObservableValue<? extends TextEntity> observable, TextEntity oldValue, TextEntity newValue) {
                editorField.textProperty().bind(newValue.textProperty());
            }
        });
    }



}
