package org.hsd.cryptoeditor.crypto.grapher;

import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.exception.CryptographerException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import java.io.InputStream;
import java.security.Key;

/**
 * Created by nils on 5/16/16.
 */
public class BCCryptographer implements Cryptographer {

    private Encryption encryption;

    private Key key;

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
        Cipher c = Cipher.getInstance(parseInstanceCall(encryption), "BC");

        if (encryption.getMode().isVectorMode()) {
            if (encryption.getInitializationVector() != null) {
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

    private String parseInstanceCall(Encryption encryption) {
        String instanceCall = encryption.getType().getName();
        if (encryption.getType().isPBEType() || encryption.getType().isStreamType()) {
            return instanceCall;
        } else {
            instanceCall += "/" + encryption.getMode().getName();
            if (encryption.getMode().isStreamMode()) {
                encryption.setPadding(EncryptionPadding.NoPadding);
            }
            instanceCall += "/" + encryption.getPadding();
            return instanceCall;
        }
    }

    public BCCryptographer(Encryption encryption, Key key) {
        this.encryption = encryption;
        this.key = key;
    }

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
