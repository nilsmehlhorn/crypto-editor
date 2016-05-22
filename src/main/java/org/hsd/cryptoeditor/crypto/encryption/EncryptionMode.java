package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Created by nils on 5/20/16.
 */

public enum EncryptionMode {

    ECB("ECB", false),
    CBC("CBC", true);

    private final String name;
    private final boolean isVectorMode;

    EncryptionMode(String name, boolean isVectorMode) {
        this.name = name;
        this.isVectorMode = isVectorMode;
    }

    public boolean isVectorMode() {
        return isVectorMode;
    }

    public String getName() {
        return name;
    }
}