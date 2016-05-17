package org.hsd.cryptoeditor.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * Created by nils on 5/17/16.
 */
public class StoreService {

    private KeyStore keyStore;

    private File keyStoreFile = new File(".keystore");

    private char[] password = "1234".toCharArray();

    public SecretKey loadKey(String alias) {
        try {
            KeyStore.Entry entry = keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
            return entry != null ? ((KeyStore.SecretKeyEntry) entry).getSecretKey() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void storeKey(String alias, SecretKey key) {
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(key);
        try {
            keyStore.setEntry(alias, entry, new KeyStore.PasswordProtection(password));
            keyStore.store(new FileOutputStream(keyStoreFile), password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initKeyStore() {
        try {
            keyStore = KeyStore.getInstance("JCEKS");
            if (keyStoreFile.exists()) {
                keyStore.load(new FileInputStream(keyStoreFile), password);
            } else {
                keyStore.load(null, null);
                keyStore.store(new FileOutputStream(keyStoreFile), password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /////

    private static final StoreService instance = new StoreService();

    private StoreService() {
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        } else {
            initKeyStore();
        }
    }

    public static StoreService getInstance() {
        return instance;
    }
}
