package org.hsd.cryptoeditor.encryption;

import org.hsd.cryptoeditor.encryption.exception.CryptographerException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import java.io.InputStream;

/**
 * Created by nils on 5/10/16.
 */
public class AESCryptographer implements Cryptographer {

    @Override
    public CipherInputStream getEncryptor(InputStream in, SecretKey key) throws CryptographerException {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }

    @Override
    public CipherInputStream getDecryptor(InputStream in, SecretKey key) throws CryptographerException {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }
}
