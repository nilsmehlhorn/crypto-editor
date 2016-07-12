package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Enumeration of cryptographic padding methods.
 */
public enum EncryptionPadding {
    NoPadding("NoPadding"),
    PKCS5Padding("PKCS5Padding"),
    PKCS7Padding("PKCS7Padding");

    private final String name;

    EncryptionPadding(String name) {
        this.name = name;
    }

    /**
     * @return Bouncy Castle conform identifier
     */
    public String getName() {
        return name;
    }
}