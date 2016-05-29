package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Created by nils on 5/11/16.
 */
public class DESEncryption extends Encryption {

    public DESEncryption() {
        super(EncryptionType.DES);
        this.setMode(EncryptionMode.ECB);
    }
}
