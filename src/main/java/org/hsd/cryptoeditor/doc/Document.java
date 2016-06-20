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
 * Created by nils on 5/1/16.
 */
public class Document implements Serializable {

    private File file;

    private Encryption encryption;

    private StringProperty content = new SimpleStringProperty();

    public Document() {
        encryption = new Encryption(EncryptionType.NONE);
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

    public byte[] getContentBytes() {
        try {
            return this.content.get().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CryptoEditorException(e);
        }
    }
}
