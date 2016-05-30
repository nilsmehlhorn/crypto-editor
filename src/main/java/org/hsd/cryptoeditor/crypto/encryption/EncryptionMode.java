package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Created by nils on 5/20/16.
 */

public enum EncryptionMode {

    ECB("ECB", false, false, EncryptionPadding.values()),
    CBC("CBC", true, false, EncryptionPadding.values()),
    CTR("CTR", true, true, EncryptionPadding.NoPadding),
    OFB("OFB", true, true, EncryptionPadding.NoPadding),
    CFB("CFB", true, true, EncryptionPadding.NoPadding),
    GCM("GCM", true, true, EncryptionPadding.NoPadding);

    private final String name;
    private final boolean isVectorMode;
    private final boolean isStreamMode;
    private final EncryptionPadding[] supportedPaddings;

    EncryptionMode(String name, boolean isVectorMode, boolean isStreamMode, EncryptionPadding... supportedPaddings) {
        this.name = name;
        this.isVectorMode = isVectorMode;
        this.supportedPaddings = supportedPaddings;
        this.isStreamMode = isStreamMode;
    }

    public boolean isVectorMode() {
        return isVectorMode;
    }

    public boolean isStreamMode() {
        return isStreamMode;
    }

    public String getName() {
        return name;
    }

    public EncryptionPadding[] getSupportedPaddings() {
        return supportedPaddings;
    }
}