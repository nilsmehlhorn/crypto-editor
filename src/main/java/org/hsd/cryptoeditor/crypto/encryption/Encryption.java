package org.hsd.cryptoeditor.crypto.encryption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nils on 5/11/16.
 */
public class Encryption {

    private EncryptionType type;

    private EncryptionMode mode;

    private EncryptionPadding padding;

    private byte[] initializationVector;

    public Encryption(EncryptionType type) {
        this.type = type;
        if(type.equals(EncryptionType.SHA_128BIT_AES_CBC_BC) || type.equals(EncryptionType.SHA_256BIT_AES_CBC_BC)) {
            this.mode = EncryptionMode.CBC;
        } else {
            this.mode = EncryptionMode.ECB;
        }
    }

    public List<EncryptionPadding> getPossiblePaddings() {
        List<EncryptionPadding> possiblePaddings = new ArrayList<>();
        if (type.isStreamType()) {
            possiblePaddings.add(EncryptionPadding.NoPadding);
        } else {
            possiblePaddings.addAll(Arrays.asList(type.getSupportedPaddings()));
            possiblePaddings.retainAll(Arrays.asList(mode.getSupportedPaddings()));
        }
        return possiblePaddings;
    }

    public void setPadding(EncryptionPadding padding) {
        if (padding == null)
            return;
        if (getPossiblePaddings().contains(padding)) {
            this.padding = padding;
        } else {
            throw new IllegalArgumentException("Padding is not listed as possible for the encryption type");
        }
    }

    public EncryptionPadding getPadding() {
        return padding;
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

    public byte[] getInitializationVector() {
        return initializationVector;
    }

    public void setInitializationVector(byte[] initializationVector) {
        this.initializationVector = initializationVector;
    }
}
