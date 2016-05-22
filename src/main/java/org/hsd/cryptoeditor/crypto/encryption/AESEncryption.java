package org.hsd.cryptoeditor.crypto.encryption;


/**
 * Created by nils on 5/11/16.
 */
public class AESEncryption extends Encryption {

    public AESEncryption() {
        super(EncryptionType.AES, EncryptionPadding.PKCS7Padding);
    }
}
