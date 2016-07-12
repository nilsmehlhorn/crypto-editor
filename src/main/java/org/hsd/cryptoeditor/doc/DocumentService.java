package org.hsd.cryptoeditor.doc;

import javafx.beans.property.*;
import org.hsd.cryptoeditor.CryptoEditorException;
import org.hsd.cryptoeditor.concurrency.DecryptionService;
import org.hsd.cryptoeditor.concurrency.EncryptionService;
import org.hsd.cryptoeditor.concurrency.LoadService;
import org.hsd.cryptoeditor.concurrency.SaveService;
import org.hsd.cryptoeditor.crypto.CryptoService;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.dialog.DialogService;

import java.io.*;
import java.util.Optional;

/**
 * Service handling main features which consist of document related operations.
 */
public class DocumentService {

    private ObjectProperty<Document> current = new SimpleObjectProperty<Document>();

    private DialogService dialogService = DialogService.getInstance();

    private CryptoService cryptoService = CryptoService.getInstance();

    /**
     * Persists a document to the passed file by serializing the contents and encrypting them based on the nested encryption definition.
     * This process uses {@link SaveService} and {@link EncryptionService} to perform expensive operations on seperate threads.
     *
     * @param file document file to save to
     * @throws CryptoEditorException when an error occurs during the saving
     */
    public void saveCurrent(final File file) {
        save(current.get(), file);
        current.get().setHasUnsavedChanges(false);
    }

    /**
     * Assembles a document from the passed file by reading the contents and decrypting them based on the wrapped encryption definition.
     * This process uses {@link LoadService} and {@link DecryptionService} to perform expensive operations on seperate threads.
     * Eventually the document is set as currently in editing for the application.
     *
     * @param file document file to load from
     * @throws CryptoEditorException when an error occurs during the loading
     */
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

    private void save(Document document, File file) {
        // map to dto
        PersistenceDTO dto = new PersistenceDTO();
        dto.setEncryptionType(document.getEncryption().getType());
        dto.setEncryptionMode(document.getEncryption().getMode());
        dto.setEncryptionPadding(document.getEncryption().getPadding());
        // encrypt content
        EncryptionService encryptionService = new EncryptionService();
        encryptionService.setEncryption(document.getEncryption());
        encryptionService.setContentInput(new ByteArrayInputStream(document.getContentBytes()));
        encryptionService.setOnSucceeded(encryptionEvent -> {
            // set content
            dto.setContent(encryptionService.getValue());
            // set hash
            dto.setHash(cryptoService.getHash(document.getContentBytes()));
            // set iv
            if (document.getEncryption().getMode() != null) {
                if (document.getEncryption().getMode().isVectorMode()) {
                    dto.setInitializationVector(document.getEncryption().getInitializationVector());
                }
            }
            if (document.getEncryption().getType().isPBEType()) {
                // set salt
                dto.setSalt(document.getEncryption().getSalt());
            } else if (document.getEncryption().getType().isAsymmetric()) {
                // set public key
                dto.setPublicKey(document.getEncryption().getPublicKey());
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
        if (optional.isPresent()) {
            encryptionService.setPassword(optional.get().toCharArray());
            encryptionService.start();
        }
    }

    private Document buildDocument(PersistenceDTO dto, File file) {
        Document document = new Document();
        Encryption encryption = cryptoService.getEncryption(dto.getEncryptionType());
        encryption.setMode(dto.getEncryptionMode());
        encryption.setPadding(dto.getEncryptionPadding());
        if (encryption.getMode() != null) {
            if (encryption.getMode().isVectorMode()) {
                encryption.setInitializationVector(dto.getInitializationVector());
            }
        }
        if (encryption.getType().isPBEType()) {
            encryption.setSalt(dto.getSalt());
        } else if (encryption.getType().isAsymmetric()) {
            encryption.setPublicKey(dto.getPublicKey());
        }
        document.setEncryption(encryption);
        document.setFile(file);
        DecryptionService decryptionService = new DecryptionService();
        decryptionService.setCipherInput(new ByteArrayInputStream(dto.getContent()));
        decryptionService.setEncryption(document.getEncryption());
        decryptionService.setOnSucceeded(event -> {
            byte[] decryptionResult = decryptionService.getValue();
            if (!cryptoService.hashMatch(decryptionResult, dto.getHash())) {
                dialogService.showErrorDialog("The decrypted content did not pass the hash test",
                        "The persisted hash did not match the hash value of the decrypted contents.\n" +
                                "The content might be tampered!");
            }
            document.setText(new String(decryptionResult));
        });
        decryptionService.setOnFailed(failEvent -> {
            dialogService.showErrorDialog("Could not decrypt contents", "An error occured during decryption");
            throw new CryptoEditorException(decryptionService.getException());
        });
        Optional<String> optional = dialogService.showPasswordDialog();
        if (optional.isPresent()) {
            decryptionService.setPassword(optional.get().toCharArray());
            decryptionService.start();
        }
        return document;
    }

    /**
     * Initializes the document currently in editing with a new document. If there are unsaved changes to the document the user will be prompted to a save-dialog.
     */
    public void initNewDocument() {
        // TODO check for unsaved
        current.set(new Document());
    }

    /**
     * @return property containting the document currently in editing
     */
    public ObjectProperty<Document> currentDocumentProperty() {
        return current;
    }

    /////

    private static final DocumentService instance = new DocumentService();

    private DocumentService() {
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        } else {
            initNewDocument();
        }
    }

    /**
     * When the service is instantiated a new document will be initialized as the document currently in editing.
     *
     * @return service instance
     */
    public static DocumentService getInstance() {
        return instance;
    }


}
