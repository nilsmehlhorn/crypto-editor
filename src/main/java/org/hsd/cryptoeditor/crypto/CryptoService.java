package org.hsd.cryptoeditor.crypto;

import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.grapher.BCCryptographer;
import org.hsd.cryptoeditor.crypto.grapher.Cryptographer;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nils on 5/11/16.
 */
public class CryptoService {

    public Encryption getEncryption(EncryptionType type) {
        Encryption encryption = null;
        switch (type) {
            case NONE:
                encryption = new Encryption(EncryptionType.NONE);
                break;
            case DES:
                encryption = new Encryption(EncryptionType.DES);
                break;
            case AES:
                encryption = new Encryption(EncryptionType.AES);
                break;
            case ARC4:
                encryption = new Encryption(EncryptionType.ARC4);
                break;
        }
        encryption.setMode(EncryptionMode.ECB);
        return encryption;
    }

    public Cryptographer getCryptographer(Encryption encryption) {
        SecretKey key = StoreService.getInstance().loadKey(encryption.getType().toString());
        if(key == null) {
            try {
                key = KeyGenerator.getInstance(String.valueOf(encryption.getType())).generateKey();
                StoreService.getInstance().storeKey(encryption.getType().toString(), key);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return new BCCryptographer(encryption, key);
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
