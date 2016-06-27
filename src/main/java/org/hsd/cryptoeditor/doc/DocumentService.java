package org.hsd.cryptoeditor.doc;

import javafx.beans.property.*;
import javafx.concurrent.Service;
import org.apache.commons.io.IOUtils;
import org.hsd.cryptoeditor.CryptoEditorException;
import org.hsd.cryptoeditor.concurrency.DecryptionService;
import org.hsd.cryptoeditor.concurrency.EncryptionService;
import org.hsd.cryptoeditor.concurrency.LoadService;
import org.hsd.cryptoeditor.concurrency.SaveService;
import org.hsd.cryptoeditor.crypto.CryptoService;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.grapher.Cryptographer;
import org.hsd.cryptoeditor.dialog.DialogService;

import java.io.*;
import java.util.Optional;

/**
 * Created by nils on 5/1/16.
 */
public class DocumentService {

    private ObjectProperty<Document> current = new SimpleObjectProperty<Document>();

    private DialogService dialogService = DialogService.getInstance();

    public void saveCurrent(final File file) {
        save(current.get(), file);
    }

    public void save(Document document, File file) {
        // map to dto
        PersistenceDTO dto = new PersistenceDTO();
        dto.setEncryptionType(document.getEncryption().getType());
        dto.setEncryptionMode(document.getEncryption().getMode());
        dto.setEncryptionPadding(document.getEncryption().getPadding());
        dto.setPbeType(document.getEncryption().getPbeType());
        // encrypt content
        EncryptionService encryptionService = new EncryptionService();
        encryptionService.setEncryption(document.getEncryption());
        encryptionService.setContentInput(new ByteArrayInputStream(document.getContentBytes()));
        encryptionService.setOnSucceeded(encryptionEvent -> {
            // set remaining parts (content, iv)
            dto.setContent(encryptionService.getValue());
            if (document.getEncryption().getMode().isVectorMode()) {
                dto.setInitializationVector(document.getEncryption().getInitializationVector());
            }
            // save to file
            SaveService saveService = new SaveService();
            saveService.setUrl(file.getPath());
            saveService.setPersistenceDTO(dto);
            saveService.setOnSucceeded(saveEvent -> document.setFile(file));
            saveService.setOnFailed(failEvent -> {
                dialogService.showErrorDialog("Could not save file", "An error occured while saving the file");
                throw new CryptoEditorException(saveService.getException());
            });
            saveService.start();
        });
        encryptionService.setOnFailed(failEvent -> {
            dialogService.showErrorDialog("Could not encrypt contents", "An error occured during encryption");
            throw new CryptoEditorException(encryptionService.getException());
        });
        Optional<String> optional = dialogService.showPasswordDialog();
        if(optional.isPresent()) {
            encryptionService.setPassword(optional.get().toCharArray());
            encryptionService.start();
        }
    }

    public void load(File file) {
        final Document document = new Document();
        document.setFile(file);
        final LoadService loadService = new LoadService();
        loadService.setUrl(file.getPath());
        loadService.setOnSucceeded(event -> {
            current.set(buildDocument(loadService.getValue(), file));
        });
        loadService.setOnFailed(f -> {
            DialogService.getInstance().showErrorDialog("Could not load file", "An error occured while loading the file");
            throw new CryptoEditorException(loadService.getException());
        });
        loadService.start();
    }

    private Document buildDocument(PersistenceDTO dto, File file) {
        Document document = new Document();
        Encryption encryption = CryptoService.getInstance().getEncryption(dto.getEncryptionType());
        encryption.setMode(dto.getEncryptionMode());
        encryption.setPbeType(dto.getPbeType());
        encryption.setPadding(dto.getEncryptionPadding());
        if (encryption.getMode().isVectorMode()) {
            encryption.setInitializationVector(dto.getInitializationVector());
        }
        document.setEncryption(encryption);
        document.setFile(file);
        DecryptionService decryptionService = new DecryptionService();
        decryptionService.setCipherInput(new ByteArrayInputStream(dto.getContent()));
        decryptionService.setEncryption(document.getEncryption());
        decryptionService.setOnSucceeded(event -> document.setText(new String(decryptionService.getValue())));
        decryptionService.setOnFailed(failEvent -> {
            dialogService.showErrorDialog("Could not decrypt contents", "An error occured during decryption");
            throw new CryptoEditorException(decryptionService.getException());
        });
        Optional<String> optional = dialogService.showPasswordDialog();
        if(optional.isPresent()) {
            decryptionService.setPassword(optional.get().toCharArray());
            decryptionService.start();
        }
        return document;
    }

    public ObjectProperty<Document> currentDocumentProperty() {
        return current;
    }

    /////

    private static final DocumentService instance = new DocumentService();

    private DocumentService() {
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        } else {
            current.set(new Document());
        }
    }

    public static DocumentService getInstance() {
        return instance;
    }


}
