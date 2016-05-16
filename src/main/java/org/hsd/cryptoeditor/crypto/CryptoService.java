package org.hsd.cryptoeditor.crypto;

import org.hsd.cryptoeditor.crypto.encryption.AESEncryption;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.encryption.NoEncryption;
import org.hsd.cryptoeditor.crypto.exception.CryptographerException;
import org.hsd.cryptoeditor.crypto.grapher.BCCryptographer;
import org.hsd.cryptoeditor.crypto.grapher.Cryptographer;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

/**
 * Created by nils on 5/11/16.
 */
public class CryptoService {

    private KeyStore keyStore;

    public Encryption getEncryption(EncryptionType type) {
        Encryption encryption = null;
        switch (type) {
            case NONE:
                encryption = new NoEncryption();
            case AES:
                encryption = new AESEncryption();
        }
        return encryption;
    }

    public Cryptographer getCryptographer(Encryption encryption) {
        Cryptographer c = null;
        KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection("1234".toCharArray());
        try {
            SecretKey key = null;
            KeyStore.Entry keyEntry = keyStore.getEntry(encryption.getType().toString(), keyPassword);
            if (keyEntry != null) {
                key = ((KeyStore.SecretKeyEntry) keyEntry).getSecretKey();
            } else {
                key = KeyGenerator.getInstance(encryption.getType().toString()).generateKey();
                KeyStore.SecretKeyEntry keyStoreEntry = new KeyStore.SecretKeyEntry(key);
                keyStore.setEntry(encryption.getType().toString(), keyStoreEntry, keyPassword);
            }
            c = new BCCryptographer(encryption, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    private void initKeyStore() {
        try {
            File keyStoreFile = new File(".keystore");
            keyStore = KeyStore.getInstance("JCEKS");
            if (keyStoreFile.exists()) {
                keyStore.load(new FileInputStream(keyStoreFile), "1234".toCharArray());
            } else {
                keyStore.load(null, null);
                keyStore.store(new FileOutputStream(keyStoreFile), "1234".toCharArray());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////

    private static final CryptoService instance = new CryptoService();

    private CryptoService() {
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        } else {
            initKeyStore();
        }
    }

    public static CryptoService getInstance() {
        return instance;
    }
}
