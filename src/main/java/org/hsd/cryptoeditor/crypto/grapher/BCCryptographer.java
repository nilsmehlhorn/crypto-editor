package org.hsd.cryptoeditor.crypto.grapher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.exception.CryptographerException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
        Cipher c = null;
        c = Cipher.getInstance(String.format("%s/%s/%s", encryption.getType(), encryption.getMode(), encryption.getPadding()), "BC");
        c.init(cipherMode, key);
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
