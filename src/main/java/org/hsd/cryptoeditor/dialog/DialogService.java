package org.hsd.cryptoeditor.dialog;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.hsd.cryptoeditor.CryptoEditorException;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


/**
 * Service for handling dailog based interactions with the user.
 */
public class DialogService {

    private Window window;

    public void setWindow(Window window) {
        this.window = window;
    }

    /**
     * Displays a file chooser dialog for opening a file.
     *
     * @param title title of the dialog window
     * @return user chosen file
     */
    public File showOpenDialog(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(window);
    }

    /**
     * Displays a file chooser dialog for saving a file.
     *
     * @param title title of the dialog window
     * @return user chosen file
     */
    public File showSaveDialog(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("untitled.json");
        fileChooser.setTitle(title);
        return fileChooser.showSaveDialog(window);
    }

    /**
     * Displays an error dialog.
     *
     * @param header  title of the error message
     * @param content text of the error message
     */
    public void showErrorDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        Label label = new Label(content);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);
        alert.showAndWait();
    }

    /**
     * Displays a dialog for encryption selection.
     *
     * @param currentEncryption currently selected encryption to initialize the dialog with
     * @return optional resolving to the now selected encryption
     */
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

    /**
     * Displays a password input dialog.
     *
     * @return optional resolving to the entered password
     */
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
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static DialogService getInstance() {
        return instance;
    }
}
