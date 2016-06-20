package org.hsd.cryptoeditor.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.hsd.cryptoeditor.CryptoEditorException;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by nils on 5/1/16.
 */
public class DialogService {

    private Window window;

    public void setWindow(Window window) {
        this.window = window;
    }

    public File showOpenDialog(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(window);
    }

    public File showSaveDialog(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showSaveDialog(window);
    }

    public void showErrorDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public Optional<Encryption> showEncryptionDialog(Encryption currentEncryption) {
        Dialog<Encryption> encryptionDialog = new Dialog<>();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog/encryption_dialog.fxml"));
            encryptionDialog.setTitle("Encryption");
            encryptionDialog.getDialogPane().setContent(loader.load());
            encryptionDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            EncryptionDialogController controller = loader.getController();
            controller.setEncryption(currentEncryption);
            encryptionDialog.setResultConverter(buttonType -> {
                ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
                return data == ButtonBar.ButtonData.OK_DONE ? controller.getEncryption() : null;
            });
            return encryptionDialog.showAndWait();
        } catch (IOException e) {
            throw new CryptoEditorException("Unable to load view-resource", e);
        }
    }

    public Optional<String> showPasswordDialog() {
        Dialog<String> passwordDialog = new Dialog<>();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog/password_dialog.fxml"));
            passwordDialog.setTitle("Password");
            passwordDialog.getDialogPane().setContent(loader.load());
            passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            PasswordDialogController controller = loader.getController();
            passwordDialog.setResultConverter(buttonType -> {
                ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
                return data == ButtonBar.ButtonData.OK_DONE ? controller.getPassword() : null;
            });
            return passwordDialog.showAndWait();
        } catch (IOException e) {
            throw new CryptoEditorException("Unable to load view-resource", e);
        }
    }

    /////

    private static final DialogService instance = new DialogService();

    private DialogService() {
        if(instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static DialogService getInstance() {
        return instance;
    }
}
