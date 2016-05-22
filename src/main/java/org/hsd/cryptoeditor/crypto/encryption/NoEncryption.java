package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Created by nils on 5/16/16.
 */
public class NoEncryption extends Encryption {
    public NoEncryption() {
        super(EncryptionType.NONE, EncryptionPadding.values());
    }

}
