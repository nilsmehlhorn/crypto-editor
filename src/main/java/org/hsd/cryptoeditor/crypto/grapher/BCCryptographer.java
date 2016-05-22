package org.hsd.cryptoeditor.crypto.grapher;

import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.exception.CryptographerException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.InputStream;

/**
 * Created by nils on 5/16/16.
 */
public class BCCryptographer implements Cryptographer {

    private Encryption encryption;

    private SecretKey key;

    public CipherInputStream getEncryptor(InputStream in) throws CryptographerException {
        try {
            return new CipherInputStream(in, buildCipher(Cipher.ENCRYPT_MODE));
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }

    public CipherInputStream getDecryptor(InputStream in) throws CryptographerException {
        try {
            return new CipherInputStream(in, buildCipher(Cipher.DECRYPT_MODE));
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }

    private Cipher buildCipher(int cipherMode) throws Exception {
        if (encryption == null) {
            throw new IllegalStateException("BCCryptographer needs to be initialized with a valid encryption");
        }
        Cipher c = Cipher.getInstance(String.format("%s/%s/%s", encryption.getType(), encryption.getMode(), encryption.getPadding()), "BC");
        if(encryption.getMode().isVectorMode()) {
            if(encryption.getInitializationVector() != null) {
                c.init(cipherMode, key, new IvParameterSpec(encryption.getInitializationVector()));
            } else {
                c.init(cipherMode, key);
                encryption.setInitializationVector(c.getIV());
            }
        } else {
            c.init(cipherMode, key);
        }
        return c;
    }

    public BCCryptographer(Encryption encryption, SecretKey key) {
        this.encryption = encryption;
        this.key = key;
    }

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }
}
