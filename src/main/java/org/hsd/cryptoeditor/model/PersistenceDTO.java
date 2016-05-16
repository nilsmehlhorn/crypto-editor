package org.hsd.cryptoeditor.model;

import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;

/**
 * Created by nils on 5/16/16.
 */
public class PersistenceDTO {

    private EncryptionType encryptionType;

    private EncryptionPadding encryptionPadding;

    private EncryptionMode encryptionMode;

    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public EncryptionType getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(EncryptionType encryptionType) {
        this.encryptionType = encryptionType;
    }

    public EncryptionPadding getEncryptionPadding() {
        return encryptionPadding;
    }

    public void setEncryptionPadding(EncryptionPadding encryptionPadding) {
        this.encryptionPadding = encryptionPadding;
    }

    public EncryptionMode getEncryptionMode() {
        return encryptionMode;
    }

    public void setEncryptionMode(EncryptionMode encryptionMode) {
        this.encryptionMode = encryptionMode;
    }
}
