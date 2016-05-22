package org.hsd.cryptoeditor.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.hsd.cryptoeditor.crypto.CryptoService;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nils on 5/11/16.
 */
public class EncryptionDialogController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private ListView<EncryptionType> encryptionTypeList;
    private Map<EncryptionType, Encryption> mapping;

    @FXML
    public void initialize() {
        // load encryption types
        mapping = new HashMap<>();
        for(EncryptionType type : EncryptionType.values()) {
            mapping.put(type, CryptoService.getInstance().getEncryption(type));
        }
        ObservableList<EncryptionType> types = FXCollections.observableArrayList(mapping.keySet());
        encryptionTypeList.setItems(types);

        encryptionTypeList.getSelectionModel().selectedItemProperty().addListener(observable -> {
            setEncryptionView(encryptionTypeList.getSelectionModel().getSelectedItem());
        });
    }

    private void setEncryptionView(EncryptionType type) {
        Node n = null;
        if(type == EncryptionType.NONE) {
            n = new Text("Select an encryption from the list");
        } else {
            Encryption encryption = mapping.get(type);
            GridPane settingsView = new GridPane();
            settingsView.setPadding(new Insets(25, 10, 0, 10));
            settingsView.setHgap(20);
            settingsView.add(new Label("Block Cipher Mode"), 0, 0);
            ObservableList<EncryptionMode> modes = FXCollections.observableArrayList(EncryptionMode.values());
            ComboBox<EncryptionMode> modeBox = new ComboBox<>(modes);
            modeBox.setValue(encryption.getMode());
            modeBox.setOnAction(event -> encryption.setMode(modeBox.getValue()));
            settingsView.add(modeBox, 1, 0);

            settingsView.add(new Label("Padding Method"), 0, 1);
            ObservableList<EncryptionPadding> paddings = FXCollections.observableArrayList(encryption.getPossiblePaddings());
            ComboBox<EncryptionPadding> padBox = new ComboBox<>(paddings);
            padBox.setValue(encryption.getPadding());
            padBox.setOnAction(event -> encryption.setPadding(padBox.getValue()));
            settingsView.add(padBox, 1, 1);
            n = settingsView;
        }
        borderPane.setCenter(n);
    }

    public void setEncryption(Encryption encryption) {
        mapping.put(encryption.getType(), encryption);
        encryptionTypeList.getSelectionModel().select(encryption.getType());
    }

    public Encryption getEncryption() {
        return mapping.get(encryptionTypeList.getSelectionModel().getSelectedItem());
    }
}

