package org.hsd.cryptoeditor.encryption;

import org.hsd.cryptoeditor.encryption.exception.CryptographerException;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by nils on 5/10/16.
 */
public interface Cryptographer {

    CipherInputStream getEncryptor(InputStream in, SecretKey key) throws CryptographerException;
    CipherInputStream getDecryptor(InputStream in, SecretKey key) throws CryptographerException;
}
