package org.hsd.cryptoeditor.crypto.encryption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model class representing an encryption algorithm with all its public parameters.
 */
public class Encryption {

    private EncryptionType type;

    private EncryptionMode mode;

    private EncryptionPadding padding;

    private byte[] salt;

    private byte[] initializationVector;

    private byte[] publicKey;

    /**
     * Default constructor for initializing an algorithm based on an encryption type.
     *
     * @param type encryption type of algorithm
     */
    public Encryption(EncryptionType type) {
        this.type = type;
        if (!type.isPBEType() && !type.isStreamType() && type != EncryptionType.NONE) {
            this.mode = type.getSupportedModes()[0];
        }
    }

    /**
     * Calculates the possible paddings for this encryption by intersecting the supported paddings of the set encryption type and blockmode.
     *
     * @return possible paddings for the algorithm
     */
    public List<EncryptionPadding> getPossiblePaddings() {
        List<EncryptionPadding> possiblePaddings = new ArrayList<>();
        if (type.isStreamType()) {
            possiblePaddings.add(EncryptionPadding.NoPadding);
        } else {
            possiblePaddings.addAll(Arrays.asList(mode.getSupportedPaddings()));
            possiblePaddings.retainAll(Arrays.asList(type.getSupportedPaddings()));
        }
        return possiblePaddings;
    }

    /**
     * Sets the padding method of the algorithm.
     *
     * @param padding padding method to set
     * @throws IllegalArgumentException if the passed padding method is not possible for the alogorithm
     */
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

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
