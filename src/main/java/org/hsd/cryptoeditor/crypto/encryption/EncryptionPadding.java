package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Created by nils on 5/20/16.
 */


public enum EncryptionPadding {
    NoPadding("NoPadding"),
    PKCS5Padding("PKCS5Padding"),
    PKCS7Padding("PKCS7Padding");

    private final String name;

    EncryptionPadding(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}