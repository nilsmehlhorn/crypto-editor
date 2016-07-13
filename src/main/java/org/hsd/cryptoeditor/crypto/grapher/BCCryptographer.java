package org.hsd.cryptoeditor.crypto.grapher;

import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;

/**
 * Cryptographer implementation based on the Bouncy Castle Provider.
 * <p>
 * Note that you have to pass keys and key-pairs which work with the Bouncy Castle Provider (preferably created using it).
 *
 * @see org.bouncycastle.jce.provider.BouncyCastleProvider
 */
public class BCCryptographer implements Cryptographer {

    private final static String PROVIDER = "BC";

    private Encryption encryption;

    private Key key;

    private KeyPair keyPair;

    /**
     * @copydoc Cryptographer::getEncryptor()
     */
    @Override
    public CipherInputStream getEncryptor(InputStream in) throws CryptographerException {
        return new CipherInputStream(in, buildCipher(Cipher.ENCRYPT_MODE));
    }

    /**
     * @copydoc Cryptographer::getDecryptor()
     */
    @Override
    public CipherInputStream getDecryptor(InputStream in) throws CryptographerException {
        return new CipherInputStream(in, buildCipher(Cipher.DECRYPT_MODE));
    }

    private Cipher buildCipher(int cipherMode) {
        assert cipherMode == Cipher.ENCRYPT_MODE || cipherMode == Cipher.DECRYPT_MODE : cipherMode;

        if (encryption == null) {
            throw new IllegalStateException("BCCryptographer needs to be initialized with a valid encryption");
        }

        try {
            return encryption.getType().isAsymmetric() ? buildAsymmetricCipher(cipherMode) : buildSymmetricCipher(cipherMode);
        } catch (Exception e) {
            throw new CryptographerException("Could not build cipher", e);
        }
    }

    private Cipher buildSymmetricCipher(int cipherMode) throws Exception {
        assert cipherMode == Cipher.ENCRYPT_MODE || cipherMode == Cipher.DECRYPT_MODE : cipherMode;

        if (key == null) {
            throw new IllegalStateException("A valid secret-key is required for a symmetric encryption type");
        }
        Cipher c = Cipher.getInstance(parseInstanceCall(encryption), PROVIDER);

        if (!encryption.getType().isStreamType() && !encryption.getType().isPBEType()) {
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
        } else {
            c.init(cipherMode, key);
        }
        return c;
    }

    private Cipher buildAsymmetricCipher(int cipherMode) throws Exception {
        assert cipherMode == Cipher.ENCRYPT_MODE || cipherMode == Cipher.DECRYPT_MODE : cipherMode;

        if (keyPair == null) {
            throw new IllegalStateException("A valid key-pair is required for an asymmetric encryption type");
        }
        Cipher c = Cipher.getInstance(encryption.getType() + "/None/NoPadding", PROVIDER);
        if (cipherMode == Cipher.ENCRYPT_MODE) {
            c.init(cipherMode, keyPair.getPublic());
        } else {
            c.init(cipherMode, keyPair.getPrivate());
        }
        return c;
    }

    private String parseInstanceCall(Encryption encryption) {
        assert encryption != null;

        String instanceCall = encryption.getType().getName();
        if (encryption.getType().isPBEType() || encryption.getType().isStreamType()) {
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

    /**
     * @copydoc Cryptographer::setEncryption()
     */
    @Override
    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }

    /**
     * @copydoc Cryptographer::setKey()
     */
    @Override
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * @copydoc Cryptographer::setKeyPair()
     */
    @Override
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public BCCryptographer() {
    }
}
