package org.hsd.cryptoeditor.dialog;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
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
 * Created by nils on 5/11/16.
 */
public class EncryptionDialogController {
    @FXML
    private ComboBox<EncryptionType> pbeDropdown;
    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<EncryptionType> encryptionTypeList;
    @FXML
    private ComboBox<EncryptionMode> blockModeDropdown;
    @FXML
    private ComboBox<EncryptionPadding> paddingDropdown;

    private Map<EncryptionType, Encryption> mapping = new HashMap<>();

    private Encryption selected;

    @FXML
    public void initialize() {
        initSelectables();
        initListeners();
    }

    private void initListeners() {
        encryptionTypeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
    }

    private void initSelectables() {
        for (EncryptionType type : EncryptionType.values()) {
            mapping.put(type, CryptoService.getInstance().getEncryption(type));
        }
        List<EncryptionType> pbeTypes = Arrays.stream(EncryptionType.values())
                .filter(EncryptionType::isPBEType)
                .collect(Collectors.toList());
        List<EncryptionType> plainTypes = Arrays.stream(EncryptionType.values())
                .filter(type -> !pbeTypes.contains(type))
                .collect(Collectors.toList());
        encryptionTypeList.setItems(FXCollections.observableList(plainTypes));
        blockModeDropdown.setItems(FXCollections.observableList(Arrays.asList(EncryptionMode.values())));
        pbeDropdown.setItems(FXCollections.observableArrayList(pbeTypes));
    }

    public Encryption getEncryption() {
        return this.selected;
    }

    public void setEncryption(Encryption encryption) {
        mapping.put(encryption.getType(), encryption);
        if (encryption.getType().isPBEType()) {
            tabPane.getSelectionModel().selectLast();
            pbeDropdown.getSelectionModel().select(encryption.getType());
        } else {
            tabPane.getSelectionModel().selectFirst();
            encryptionTypeList.getSelectionModel().select(encryption.getType());
        }
    }

    private void switchToMode(EncryptionMode mode) {
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
            blockModeDropdown.getSelectionModel().select(selected.getMode());
            paddingDropdown.setItems(FXCollections.observableList(selected.getPossiblePaddings()));
            paddingDropdown.getSelectionModel().select(selected.getPadding());
        }
    }
}

