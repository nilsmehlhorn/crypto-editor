package org.hsd.cryptoeditor.crypto;

import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.grapher.BCCryptographer;
import org.hsd.cryptoeditor.crypto.grapher.Cryptographer;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by nils on 5/11/16.
 */
public class CryptoService {

    private static final int ITERATIONS = 1024;

    public Encryption getEncryption(EncryptionType type) {
        return new Encryption(type);
    }

    public Cryptographer getCryptographer(Encryption encryption, char[] password) {
        Key key = null;
        if (encryption.getType().isPBEType()) {
            if (password == null) {
                throw new IllegalArgumentException("A password is required for PBE types");
            }
            key = getPBEKey(password, encryption);
        } else {
            key = getStorageKey(encryption);
        }
        return new BCCryptographer(encryption, key);
    }

    private Key getStorageKey(Encryption encryption) {
        Key key = loadKey(encryption.getType().getName());
        return key != null ? key : generateKey(encryption.getType().getName());
    }

    private Key getPBEKey(char[] password, Encryption encryption) {
        if (!encryption.getType().isPBEType()) {
            throw new IllegalArgumentException("Can only generate PBE-keys for PBE encryption types");
        }
        Key key = null;
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[] {
                0x7d, 0x60, 0x43, 0x5f,
                0x02, (byte)0xe9, (byte)0xe0, (byte)0xae };
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, ITERATIONS);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryption.getType().getName(), "BC");
            key = keyFactory.generateSecret(pbeKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    private Key loadKey(String algorithm) {
        return StoreService.getInstance().loadKey(algorithm);
    }

    private Key generateKey(String algorithm) {
        SecretKey key = null;
        try {
            key = KeyGenerator.getInstance(algorithm).generateKey();
            StoreService.getInstance().storeKey(algorithm, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key;
    }


    /////

    private static final CryptoService instance = new CryptoService();

    private CryptoService() {
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static CryptoService getInstance() {
        return instance;
    }
}
