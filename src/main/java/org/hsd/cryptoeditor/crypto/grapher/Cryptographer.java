package org.hsd.cryptoeditor.crypto.grapher;

import org.hsd.cryptoeditor.crypto.encryption.Encryption;

import javax.crypto.CipherInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;

/**
 * Interface for cryptographers which support various assembled encryptions with their symmetric and asymmetric encryption-types and accordingly cryptographic operations for en- and decryption.
 *
 * @see Encryption
 * @see org.hsd.cryptoeditor.crypto.encryption.EncryptionType
 */
public interface Cryptographer {

    /**
     * Assembles an appropriate Cipher for encryption based on the encryption set via {@link #setEncryption}, which is then wrapped in a CipherInputStream.
     *
     * @param in cipher input for encryption
     * @return output stream of encryption
     * @throws CryptographerException
     */
    CipherInputStream getEncryptor(InputStream in) throws CryptographerException;

    /**
     * Assembles an appropriate Cipher for decryption based on the encryption set via {@link #setEncryption(Encryption)}, which is then wrapped in a CipherInputStream.
     *
     * @param in cipher input for decryption
     * @return output stream of decryption
     * @throws CryptographerException
     */
    CipherInputStream getDecryptor(InputStream in) throws CryptographerException;

    /**
     * Sets the {@link Encryption} which will be used for cryptographic operations with this Cryptographer.
     * <p>
     * Notice that you also have to initialize the Cryptographer with a valid key via {@link #setKey(Key)} (for symmetric encryption types) or key-pair via {@link #setKeyPair(KeyPair)} (for asymmetric encryption types).
     *
     * @param encryption encryption to be used for cryptographic operations
     * @see org.hsd.cryptoeditor.crypto.encryption.EncryptionType
     */
    void setEncryption(Encryption encryption);

    void setKey(Key key);

    void setKeyPair(KeyPair keyPair);
}
