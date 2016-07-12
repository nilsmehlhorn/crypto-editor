package org.hsd.cryptoeditor.crypto;

import org.hsd.cryptoeditor.CryptoEditorException;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Service for handling key storage in a locally saved key store.
 *
 * @see KeyStore
 */
class StoreService {

    private KeyStore keyStore;

    private File keyStoreFile = new File(".keystore");

    private char[] storePassword = "1234".toCharArray();

    /**
     * Stores the passed secret-key in the key store, indexed with the passed alias and secured by the passed password.
     *
     * @param alias    index for key storage
     * @param key      key to store
     * @param password secures key entry
     */
    void storeSecretKey(String alias, SecretKey key, char[] password) {
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(key);
        try {
            keyStore.setEntry(alias, entry, new KeyStore.PasswordProtection(password));
            keyStore.store(new FileOutputStream(keyStoreFile), storePassword);
        } catch (Exception e) {
            throw new CryptoEditorException("Could not store secret-key in keystore", e);
        }
    }

    /**
     * Loads a secret-key from the key store which was saved under the passed alias.
     *
     * @param alias    index for the key-entry
     * @param password password for the key-entry
     * @return loaded secret-key
     * @throws CryptoEditorException when the key could not be retrieved
     */
    SecretKey loadSecretKey(String alias, char[] password) {
        try {
            KeyStore.Entry entry = keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
            return entry != null ? ((KeyStore.SecretKeyEntry) entry).getSecretKey() : null;
        } catch (Exception e) {
            throw new CryptoEditorException("Could not retrieve secret-key from keystore", e);
        }
    }

    /**
     * Stores the passed private-key in the key store, indexed with the passed alias and secured by the passed password.
     *
     * @param alias       index for key storage
     * @param key         key to store
     * @param password    secures key entry
     * @param certificate arbitrary self-signed certificate to satisfy key store API
     */
    void storePrivateKey(String alias, PrivateKey key, char[] password, Certificate certificate) {
        KeyStore.Entry entry = new KeyStore.PrivateKeyEntry(key, new Certificate[]{certificate});
        try {
            keyStore.setEntry(alias, entry, new KeyStore.PasswordProtection(password));
            keyStore.store(new FileOutputStream(keyStoreFile), storePassword);
        } catch (Exception e) {
            throw new CryptoEditorException("Could not store private-key in keystore", e);
        }
    }

    /**
     * Loads a private-key from the key store which was saved under the passed alias.
     *
     * @param alias    index for the key-entry
     * @param password password for the key-entry
     * @return loaded private-key
     * @throws CryptoEditorException when the key could not be retrieved
     */
    PrivateKey loadPrivateKey(String alias, char[] password) {
        try {
            KeyStore.Entry entry = keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
            return entry != null ? ((KeyStore.PrivateKeyEntry) entry).getPrivateKey() : null;
        } catch (Exception e) {
            throw new CryptoEditorException("Could not retrieve private-key from keystore", e);
        }
    }

    private void initKeyStore() {
        try {
            keyStore = KeyStore.getInstance("JCEKS");
            if (keyStoreFile.exists()) {
                keyStore.load(new FileInputStream(keyStoreFile), storePassword);
            } else {
                keyStore.load(null, null);
                keyStore.store(new FileOutputStream(keyStoreFile), storePassword);
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

    /**
     * When the service is instantiated the key store will be initialized by either loading it from a local file or creating it if not found.
     *
     * @return service instance
     */
    public static StoreService getInstance() {
        return instance;
    }
}
