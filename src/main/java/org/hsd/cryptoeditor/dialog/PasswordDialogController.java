package org.hsd.cryptoeditor.dialog;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

/**
 * Created by nils on 6/20/16.
 */
public class PasswordDialogController {

    @FXML
    private PasswordField passwordInput;

    public String getPassword() {
        return passwordInput.getText();
    }
}
