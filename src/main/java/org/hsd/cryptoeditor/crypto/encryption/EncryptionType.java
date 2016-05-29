package org.hsd.cryptoeditor.crypto.encryption;

import org.bouncycastle.crypto.paddings.PKCS7Padding;

/**
 * Created by nils on 5/20/16.
 */

public enum EncryptionType {
    NONE("NONE", false, null, null),
    AES("AES", false, EncryptionMode.values(), new EncryptionPadding[]{EncryptionPadding.PKCS7Padding}),
    DES("DES", false, EncryptionMode.values(), new EncryptionPadding[]{EncryptionPadding.PKCS5Padding, EncryptionPadding.PKCS7Padding}),
    ARC4("ARC4", true, null, null);

    private final String name;
    private final boolean isStreamType;
    private final EncryptionMode[] supportedModes;
    private final EncryptionPadding[] supportedPaddings;

    EncryptionType(String name, boolean isStreamType, EncryptionMode[] supportedModes, EncryptionPadding[] supportedPaddings) {
        this.name = name;
        this.isStreamType = isStreamType;

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

    public EncryptionMode[] getSupportedModes() {
        return supportedModes;
    }

    public EncryptionPadding[] getSupportedPaddings() {
        return supportedPaddings;
    }
}
