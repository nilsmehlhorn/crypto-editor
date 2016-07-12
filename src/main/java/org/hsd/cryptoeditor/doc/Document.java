package org.hsd.cryptoeditor.doc;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hsd.cryptoeditor.CryptoEditorException;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Model class for a document, which contains ...
 * <ul>
 * <li>a file associated with the document</li>
 * <li>the encryption in use for the document</li>
 * <li>a string property containing the document contents</li>
 * <li>a boolean indicating if the document has unsaved changes</li>
 * </ul>
 *
 * @see Encryption
 */
public class Document {

    private File file;

    private Encryption encryption;

    private StringProperty content = new SimpleStringProperty();

    private boolean hasUnsavedChanges = false;

    public Document() {
        encryption = new Encryption(EncryptionType.NONE);
    }

    /**
     * @return byte representation of the document contents (serialized with charset UTF-8)
     */
    public byte[] getContentBytes() {
        try {
            return this.content.get().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CryptoEditorException(e);
        }
    }

    public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
        this.hasUnsavedChanges = hasUnsavedChanges;
    }

    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public String getText() {
        return content.get();
    }

    public void setText(String s) {
        this.content.set(s);
    }

    public StringProperty textProperty() {
        return content;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Encryption getEncryption() {
        return encryption;
    }

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

}
