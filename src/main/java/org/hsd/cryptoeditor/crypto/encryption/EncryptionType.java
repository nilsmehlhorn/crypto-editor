package org.hsd.cryptoeditor.crypto.encryption;

import org.bouncycastle.crypto.paddings.PKCS7Padding;

/**
 * Created by nils on 5/20/16.
 */

public enum EncryptionType {
    NONE("NONE", false, false, null, null),
    AES("AES", false, false, EncryptionMode.values(), new EncryptionPadding[]{EncryptionPadding.PKCS7Padding, EncryptionPadding.NoPadding}),
    DES("DES", false, false, EncryptionMode.values(), new EncryptionPadding[]{EncryptionPadding.PKCS5Padding, EncryptionPadding.PKCS7Padding}),
    ARC4("ARC4", true, false, null, null),
    SHA_128BIT_AES_CBC_BC("PBEWITHSHAAND128BITAES-CBC-BC", false, true, null, null),
    SHA_256BIT_AES_CBC_BC("PBEWITHSHAAND256BITAES-CBC-BC", false, true, null, null),
    MD5_DES("PBEWithMD5AndDES", false, true, null, null),
    SHA_40BIT_RC4("PBEWithSHAAnd40BitRC4", true, true, null, null);

    private final String name;
    private final boolean isStreamType;
    private final boolean isPBEType;
    private final EncryptionMode[] supportedModes;
    private final EncryptionPadding[] supportedPaddings;

    EncryptionType(String name, boolean isStreamType, boolean isPBEType, EncryptionMode[] supportedModes, EncryptionPadding[] supportedPaddings) {
        this.name = name;
        this.isStreamType = isStreamType;
        this.isPBEType = isPBEType;

        if(supportedModes == null) {
            supportedModes = new EncryptionMode[0];
        }
        this.supportedModes = supportedModes;

        if(supportedPaddings == null) {
            supportedPaddings = new EncryptionPadding[0];
        }
        this.supportedPaddings = supportedPaddings;
    }

    public String getName() {
        return name;
    }

    public boolean isStreamType() {
        return isStreamType;
    }

    public boolean isPBEType() {
        return isPBEType;
    }

    public EncryptionMode[] getSupportedModes() {
        return supportedModes;
    }

    public EncryptionPadding[] getSupportedPaddings() {
        return supportedPaddings;
    }
}
