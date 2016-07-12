package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Enumeration of cryptographic encryption types
 */
public enum EncryptionType {
    NONE("NONE", false, false, false, null, null),
    AES("AES", false, false, false, EncryptionMode.values(), new EncryptionPadding[]{EncryptionPadding.PKCS7Padding, EncryptionPadding.NoPadding}),
    DES("DES", false, false, false, EncryptionMode.values(), new EncryptionPadding[]{EncryptionPadding.PKCS5Padding, EncryptionPadding.PKCS7Padding}),
    ARC4("ARC4", true, false, false, null, null),
    PBE_SHA_128BIT_AES_CBC_BC("PBEWITHSHAAND128BITAES-CBC-BC", false, true, false, null, null),
    PBE_SHA_256BIT_AES_CBC_BC("PBEWITHSHAAND256BITAES-CBC-BC", false, true, false, null, null),
    PBE_MD5_DES("PBEWithMD5AndDES", false, true, false, null, null),
    PBE_SHA_40BIT_RC4("PBEWithSHAAnd40BitRC4", true, true, false, null, null),
    RSA("RSA", true, false, true, null, null);

    private final String name;
    private final boolean isStreamType;
    private final boolean isPBEType;
    private final boolean isAsymmetric;
    private final EncryptionMode[] supportedModes;
    private final EncryptionPadding[] supportedPaddings;

    EncryptionType(String name, boolean isStreamType, boolean isPBEType, boolean isAsymmetric, EncryptionMode[] supportedModes, EncryptionPadding[] supportedPaddings) {
        this.name = name;
        this.isStreamType = isStreamType;
        this.isPBEType = isPBEType;
        this.isAsymmetric = isAsymmetric;

        if (supportedModes == null) {
            supportedModes = new EncryptionMode[0];
        }
        this.supportedModes = supportedModes;

        if (supportedPaddings == null) {
            supportedPaddings = new EncryptionPadding[0];
        }
        this.supportedPaddings = supportedPaddings;
    }

    /**
     * @return Bouncy Castle conform identifier
     */
    public String getName() {
        return name;
    }

    /**
     * @return boolean indicating whether the type is stream based and thus does not require a block mode or padding
     */
    public boolean isStreamType() {
        return isStreamType;
    }

    /**
     * @return boolean indicating whether the type is pbe based and thus does not require a block mode or padding
     */
    public boolean isPBEType() {
        return this.isPBEType;
    }

    /**
     * @return block modes supported by this type
     */
    public EncryptionMode[] getSupportedModes() {
        return supportedModes;
    }

    /**
     * @return padding methods supported by this type
     */
    public EncryptionPadding[] getSupportedPaddings() {
        return supportedPaddings;
    }

    /**
     * @return boolean indicating whether the type is asymmetric and thus does require a key or key-pair
     */
    public boolean isAsymmetric() {
        return isAsymmetric;
    }
}
