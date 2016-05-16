package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Created by nils on 5/11/16.
 */
public abstract class Encryption {

    private EncryptionType type;

    private EncryptionMode mode;

    private EncryptionPadding padding;

    public Encryption(EncryptionType type) {
        this.type = type;
        if(!type.equals(EncryptionType.NONE)) {
            this.mode = EncryptionMode.ECB;
            this.padding = EncryptionPadding.PKCS5Padding;
        }
    }

    public Encryption(EncryptionType type, EncryptionMode mode, EncryptionPadding padding) {
        this.type = type;
        this.mode = mode;
        this.padding = padding;
    }

    public EncryptionPadding getPadding() {
        return padding;
    }

    public void setPadding(EncryptionPadding padding) {
        this.padding = padding;
    }

    public EncryptionMode getMode() {
        return mode;
    }

    public void setMode(EncryptionMode mode) {
        this.mode = mode;
    }

    public EncryptionType getType() {
        return type;
    }

    public void setType(EncryptionType type) {
        this.type = type;
    }
}
