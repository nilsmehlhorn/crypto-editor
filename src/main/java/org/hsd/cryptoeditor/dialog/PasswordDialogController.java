package org.hsd.cryptoeditor.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

/**
 * Controller for handling the password input dialog.
 */
public class PasswordDialogController {

    @FXML
    private PasswordField passwordInput;

    /**
     * @return the password entered in the dialog
     */
    public String getPassword() {
        return passwordInput.getText();
    }
}
