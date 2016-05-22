package org.hsd.cryptoeditor.crypto.encryption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nils on 5/11/16.
 */
public abstract class Encryption {

    private EncryptionType type;

    private List<EncryptionPadding> possiblePaddings = new ArrayList<>();

    private EncryptionMode mode;

    private EncryptionPadding padding;

    private byte[] initializationVector;

    public Encryption(EncryptionType type, EncryptionPadding... paddings) {
        this.type = type;
        for (EncryptionPadding p : paddings) {
            possiblePaddings.add(p);
        }
        if (!type.equals(EncryptionType.NONE)) {
            this.mode = EncryptionMode.ECB;
            if (possiblePaddings.isEmpty()) {
                this.padding = EncryptionPadding.NoPadding;
            } else {
                this.padding = possiblePaddings.get(0);
            }
        }
    }

    public EncryptionPadding getPadding() {
        return padding;
    }

    public void setPadding(EncryptionPadding padding) {
        if (padding == null)
            return;
        if (possiblePaddings.contains(padding)) {
            this.padding = padding;
        } else {
            throw new IllegalArgumentException("Padding is not listed as possible for the encryption type");
        }
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

    public List<EncryptionPadding> getPossiblePaddings() {
        return possiblePaddings;
    }

    public byte[] getInitializationVector() {
        return initializationVector;
    }

    public void setInitializationVector(byte[] initializationVector) {
        this.initializationVector = initializationVector;
    }
}
