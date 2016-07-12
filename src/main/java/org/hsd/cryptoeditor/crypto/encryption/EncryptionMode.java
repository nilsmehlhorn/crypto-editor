package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Enumeration of cryptographic encryption modes
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

    /**
     * @return boolean indicating whether the mode is vector based and thus requires an initialization-vector
     */
    public boolean isVectorMode() {
        return isVectorMode;
    }

    /**
     * @return boolean indicating whether the mode is stream based and thus does not require padding
     */
    public boolean isStreamMode() {
        return isStreamMode;
    }

    /**
     * @return Bouncy Castle conform identifier
     */
    public String getName() {
        return name;
    }

    /**
     * @return padding methods supported by this mode
     */
    public EncryptionPadding[] getSupportedPaddings() {
        return supportedPaddings;
    }
}