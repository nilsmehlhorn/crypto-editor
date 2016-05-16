package org.hsd.cryptoeditor.crypto.grapher;

import org.hsd.cryptoeditor.crypto.exception.CryptographerException;

import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import java.io.InputStream;

/**
 * Created by nils on 5/10/16.
 */
public interface Cryptographer {

    CipherInputStream getEncryptor(InputStream in) throws CryptographerException;
    CipherInputStream getDecryptor(InputStream in) throws CryptographerException;
}
