package org.hsd.cryptoeditor.doc;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;

/**
 * Persistence class for serialization of documents.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersistenceDTO {

    private EncryptionType encryptionType;

    private EncryptionPadding encryptionPadding;

    private EncryptionMode encryptionMode;

    private byte[] initializationVector;

    private byte[] publicKey;

    private byte[] content;

    private byte[] hash;

    private byte[] salt;

    ///

    public byte[] getInitializationVector() {
        return initializationVector;
    }

    public void setInitializationVector(byte[] initializationVector) {
        this.initializationVector = initializationVector;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

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

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getSalt() {
        return salt;
    }
}
