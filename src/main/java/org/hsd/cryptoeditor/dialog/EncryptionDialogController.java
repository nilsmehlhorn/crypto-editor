package org.hsd.cryptoeditor.dialog;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.hsd.cryptoeditor.crypto.CryptoService;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for handing the encryption selection dialog.
 */
public class EncryptionDialogController {
    @FXML
    private ComboBox<EncryptionType> asymmetricDropdown;
    @FXML
    private Tab symmetricTab;
    @FXML
    private Tab pbeTab;
    @FXML
    private Tab asymmetricTab;
    @FXML
    private ComboBox<EncryptionType> pbeDropdown;
    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<EncryptionType> symmetricTypeList;
    @FXML
    private ComboBox<EncryptionMode> blockModeDropdown;
    @FXML
    private ComboBox<EncryptionPadding> paddingDropdown;

    private Map<EncryptionType, Encryption> mapping = new HashMap<>();

    private Encryption selected;

    /**
     * Called by the FXML-Loader after the view is assembled. Will initialize the selectable modes and options and register listeners for selecting them.
     */
    @FXML
    public void initialize() {
        initSelectables();
        initListeners();
    }

    private void initListeners() {
        symmetricTypeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switchToType(newValue);
        });
        blockModeDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switchToMode(newValue);
        });
        paddingDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected.setPadding(newValue);
        });
        pbeDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = mapping.get(newValue);
        });
        asymmetricDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = mapping.get(newValue);
        });
    }

    private void initSelectables() {
        for (EncryptionType type : EncryptionType.values()) {
            mapping.put(type, CryptoService.getInstance().getEncryption(type));
        }
        List<EncryptionType> pbeTypes = Arrays.stream(EncryptionType.values())
                .filter(EncryptionType::isPBEType)
                .collect(Collectors.toList());
        List<EncryptionType> asymmetricTypes = Arrays.stream(EncryptionType.values())
                .filter(EncryptionType::isAsymmetric)
                .collect(Collectors.toList());
        List<EncryptionType> symmetricTypes = Arrays.stream(EncryptionType.values())
                .filter(type -> !pbeTypes.contains(type))
                .filter(type -> !asymmetricTypes.contains(type))
                .collect(Collectors.toList());
        symmetricTypeList.setItems(FXCollections.observableList(symmetricTypes));
        pbeDropdown.setItems(FXCollections.observableArrayList(pbeTypes));
        asymmetricDropdown.setItems(FXCollections.observableArrayList(asymmetricTypes));
    }

    /**
     * @return the encryption selected by the user
     */
    public Encryption getEncryption() {
        return this.selected;
    }

    /**
     * Initializes the dialog with an encryption (used for displaying the currently selected encryption)
     *
     * @param encryption encryption to be display by the dialog
     */
    public void setEncryption(Encryption encryption) {
        mapping.put(encryption.getType(), encryption);
        if (encryption.getType().isPBEType()) {
            tabPane.getSelectionModel().select(pbeTab);
            pbeDropdown.getSelectionModel().select(encryption.getType());
        } else if (encryption.getType().isAsymmetric()) {
            tabPane.getSelectionModel().select(asymmetricTab);
            asymmetricDropdown.getSelectionModel().select(encryption.getType());
        } else {
            tabPane.getSelectionModel().select(symmetricTab);
            symmetricTypeList.getSelectionModel().select(encryption.getType());
        }
    }

    private void switchToMode(EncryptionMode mode) {
        if (mode == null) return;
        selected.setMode(mode);
        if (mode.isStreamMode()) {
            paddingDropdown.setDisable(true);
        } else {
            paddingDropdown.setDisable(false);
            paddingDropdown.setItems(FXCollections.observableList(selected.getPossiblePaddings()));
        }
    }

    private void switchToType(EncryptionType type) {
        selected = mapping.get(type);
        if (type.isStreamType() || type.equals(EncryptionType.NONE)) {
            blockModeDropdown.setDisable(true);
            paddingDropdown.setDisable(true);
        } else {
            blockModeDropdown.setDisable(false);
            paddingDropdown.setDisable(false);
            // update possible modes&paddings
            blockModeDropdown.setItems(FXCollections.observableArrayList(selected.getType().getSupportedModes()));
            blockModeDropdown.getSelectionModel().select(selected.getMode());
            paddingDropdown.setItems(FXCollections.observableList(selected.getPossiblePaddings()));
            paddingDropdown.getSelectionModel().select(selected.getPadding());
        }
    }
}

