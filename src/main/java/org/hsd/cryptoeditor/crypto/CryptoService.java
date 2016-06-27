package org.hsd.cryptoeditor.crypto;

import org.hsd.cryptoeditor.CryptoEditorException;
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

    public Encryption getEncryption(EncryptionType type) {
        return new Encryption(type);
    }

    public Cryptographer getCryptographer(Encryption encryption, char[] password) {
        return new BCCryptographer(encryption, password);
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
