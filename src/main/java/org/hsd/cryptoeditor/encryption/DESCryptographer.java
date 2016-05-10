package org.hsd.cryptoeditor.encryption;

import org.hsd.cryptoeditor.encryption.exception.CryptographerException;

import javax.crypto.*;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by nils on 5/10/16.
 */
public class DESCryptographer implements Cryptographer {

    @Override
    public CipherInputStream getEncryptor(InputStream in, SecretKey key) throws CryptographerException {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }

    @Override
    public CipherInputStream getDecryptor(InputStream in, SecretKey key) throws CryptographerException {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }
}
