package org.hsd.cryptoeditor.crypto.grapher;

import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.PBEType;
import org.hsd.cryptoeditor.crypto.exception.CryptographerException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Created by nils on 5/16/16.
 */
public class BCCryptographer implements Cryptographer {

    private final char[] password;
    private Encryption encryption;

    public CipherInputStream getEncryptor(InputStream in) throws CryptographerException {
        try {
            return new CipherInputStream(in, buildCipher(Cipher.ENCRYPT_MODE));
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }

    public CipherInputStream getDecryptor(InputStream in) throws CryptographerException {
        try {
            return new CipherInputStream(in, buildCipher(Cipher.DECRYPT_MODE));
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }

    private Cipher buildCipher(int cipherMode) throws Exception {
        if (encryption == null) {
            throw new IllegalStateException("BCCryptographer needs to be initialized with a valid encryption");
        }
        Cipher c = Cipher.getInstance(parseInstanceCall(encryption), "BC");

        byte[] salt = new byte[]{
                0x7d, 0x60, 0x43, 0x5f,
                0x02, (byte) 0xe9, (byte) 0xe0, (byte) 0xae};

        SecretKeyFactory factory = SecretKeyFactory.getInstance(encryption.getPbeType().getName());
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey key = new SecretKeySpec(tmp.getEncoded(), encryption.getType().getName());

        if (encryption.getMode().isVectorMode()) {
            if (encryption.getInitializationVector() != null) {
                c.init(cipherMode, key, new IvParameterSpec(encryption.getInitializationVector()));
            } else {
                c.init(cipherMode, key);
                encryption.setInitializationVector(c.getIV());
            }
        } else {
            c.init(cipherMode, key);
        }
        return c;
    }

    private String parseInstanceCall(Encryption encryption) {
        String instanceCall = encryption.getType().getName();
        if (encryption.getType().isStreamType()) {
            return instanceCall;
        } else {
            instanceCall += "/" + encryption.getMode().getName();
            if (encryption.getMode().isStreamMode()) {
                encryption.setPadding(EncryptionPadding.NoPadding);
            }
            instanceCall += "/" + encryption.getPadding();
            return instanceCall;
        }
    }

    public BCCryptographer(Encryption encryption, char[] password) {
        this.encryption = encryption;
        this.password = password;
    }

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }
}
