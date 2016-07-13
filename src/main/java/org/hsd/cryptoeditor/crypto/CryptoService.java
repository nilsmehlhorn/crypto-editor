package org.hsd.cryptoeditor.crypto;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.hsd.cryptoeditor.CryptoEditorException;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.grapher.CryptographerException;
import org.hsd.cryptoeditor.crypto.grapher.BCCryptographer;
import org.hsd.cryptoeditor.crypto.grapher.Cryptographer;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Service handling all cryptography-related operations
 */
public class CryptoService {

    private static final int ITERATIONS = 1024;
    private static final int SEED_BYTES = 32;

    /**
     * Resolves the passed encryption-type to an appropriate encryption object.
     *
     * @param type encryption-type to resolve
     * @return appropriate encryption object
     */
    public Encryption getEncryption(EncryptionType type) {
        return new Encryption(type);
    }

    /**
     * Resolves keys or key-pairs by either generating or retrieving them from storage and assembles a cryptographer initialized with the passed encryption and the key material.
     * <p>
     * <strong>For Encryption with a PBE type:</strong>
     * For encryptions where {@link EncryptionType#isPBEType()} is <i>true</i>, the key will be generated from the passed password using the method specified by the encryption type.
     * <p>
     * <strong>For Encryption with a symmetric type:</strong>
     * For encryptions where {@link EncryptionType#isAsymmetric()} is <i>false</i>, a key will either be retrieved from local storage or generated.
     * <p>
     * <strong>For Encryption with an asymmetric type:</strong>
     * For encryptions where {@link EncryptionType#isAsymmetric()} is <i>true</i>, the public get will be read from the passed encryption via {@link Encryption#getPublicKey()}.
     * If the public key is present, the private key will be retrieved from local storage. Otherwise a new key-pair will be generated.
     *
     * @param encryption encryption object for cryptographic operations
     * @param password   either for PBE key derivation or key retrieval from storage
     * @return assemble cryptographer
     */
    public Cryptographer getCryptographer(Encryption encryption, char[] password) {
        if (password == null) {
            throw new IllegalArgumentException("A password is required");
        }
        Cryptographer cryptographer = new BCCryptographer();
        if (encryption.getType().isAsymmetric()) {
            KeyPair keyPair = getKeyPair(encryption, password);
            cryptographer.setEncryption(encryption);
            cryptographer.setKeyPair(keyPair);
        } else {
            Key key = null;
            if (encryption.getType().isPBEType()) {
                key = getPBEKey(encryption, password);
            } else {
                key = getSecretKey(encryption, password);
            }
            cryptographer.setEncryption(encryption);
            cryptographer.setKey(key);
        }
        return cryptographer;
    }

    /**
     * Hashes the input with SHA-256 using the Bouncy Castle Provider
     *
     * @param input content to hash
     * @return resulting hash
     */
    public byte[] getHash(byte[] input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256", "BC");
            messageDigest.update(input);
            return messageDigest.digest();
        } catch (Exception e) {
            throw new CryptographerException(e);
        }
    }

    /**
     * Hashe the input with SHA-256 using the Bouncy Castle Provider and matches the result with the passed testHash
     *
     * @param input    content to hash
     * @param testHash hash to test against
     * @return result of the match-test
     */
    public boolean hashMatch(byte[] input, byte[] testHash) {
        return Arrays.equals(getHash(input), testHash);
    }

    private Key getSecretKey(Encryption encryption, char[] password) {
        assert !encryption.getType().isAsymmetric() : encryption.getType();

        SecretKey key = StoreService.getInstance().loadSecretKey(encryption.getType().getName(), password);
        if (key == null) {
            try {
                key = KeyGenerator.getInstance(encryption.getType().getName(), "BC").generateKey();
                StoreService.getInstance().storeSecretKey(encryption.getType().getName(), key, password);
            } catch (Exception e) {
                throw new CryptoEditorException(
                        String.format("Could not generate secret-key for algorithm %s", encryption.getType().getName()), e);
            }
        }
        return key;
    }

    private KeyPair getKeyPair(Encryption encryption, char[] password) {
        assert encryption.getType().isAsymmetric() : encryption.getType();

        byte[] publicKeyBytes = encryption.getPublicKey();
        KeyPair keyPair = null;
        if (publicKeyBytes != null) {
            // get private key
            PrivateKey privateKey = StoreService.getInstance().loadPrivateKey(encryption.getType().getName(), password);
            try {
                PublicKey publicKey = KeyFactory.getInstance(encryption.getType().getName(), "BC")
                        .generatePublic(new X509EncodedKeySpec(publicKeyBytes));
                keyPair = new KeyPair(publicKey, privateKey);
            } catch (Exception e) {
                throw new CryptoEditorException("Could not recover public key from serialization", e);
            }
        } else {
            // generate key pair
            try {
                keyPair = KeyPairGenerator.getInstance(encryption.getType().getName(), "BC").generateKeyPair();
                // store private key in key store
                StoreService.getInstance()
                        .storePrivateKey(encryption.getType().getName(), keyPair.getPrivate(), password, generateCertificate(keyPair));
                // expose public key
                encryption.setPublicKey(keyPair.getPublic().getEncoded());
            } catch (Exception e) {
                throw new CryptoEditorException(
                        String.format("Could not generate key-pair for algorithm %s", encryption.getType().getName()), e);
            }
        }
        return keyPair;
    }

    private Key getPBEKey(Encryption encryption, char[] password) {
        assert encryption.getType().isPBEType() : encryption.getType();

        Key key = null;
        byte[] salt = encryption.getSalt();
        if (salt == null) {
            SecureRandom secureRandom = new SecureRandom();
            salt = secureRandom.generateSeed(SEED_BYTES);
            encryption.setSalt(salt);
        }

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, ITERATIONS);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryption.getType().getName(), "BC");
            key = keyFactory.generateSecret(pbeKeySpec);
        } catch (Exception e) {
            throw new CryptoEditorException(
                    String.format("Could not generate PBE-key for scheme %s", encryption.getType().getName()), e);
        }
        return key;
    }

    /**
     * Utility method to generate a random certificate for private-key storage in java keystore
     *
     * @param keyPair RSA keypair to generate certificate for
     * @return self-signed utility certificate
     * @see java.security.KeyStore.PrivateKeyEntry
     */
    private X509Certificate generateCertificate(KeyPair keyPair) {
        X509V3CertificateGenerator cert = new X509V3CertificateGenerator();
        cert.setSerialNumber(BigInteger.valueOf(1));
        cert.setSubjectDN(new X509Principal("CN=localhost"));
        cert.setIssuerDN(new X509Principal("CN=localhost"));
        cert.setPublicKey(keyPair.getPublic());
        Calendar notBefore = Calendar.getInstance();
        cert.setNotBefore(notBefore.getTime()); // valid from now on
        Calendar notAfter = Calendar.getInstance();
        notAfter.add(Calendar.YEAR, 1);// valid for 1 year
        cert.setNotAfter(notAfter.getTime());
        cert.setSignatureAlgorithm("SHA1WithRSAEncryption");
        PrivateKey signingKey = keyPair.getPrivate();
        try {
            return cert.generate(signingKey, "BC");
        } catch (Exception e) {
            throw new CryptoEditorException("Could not generate RSA certificate", e);
        }
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
