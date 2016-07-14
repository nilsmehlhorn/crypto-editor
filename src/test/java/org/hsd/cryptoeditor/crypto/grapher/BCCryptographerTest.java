package org.hsd.cryptoeditor.crypto.grapher;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.*;

@PowerMockIgnore({"javax", "org.bouncycastle"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(Cipher.class)
public class BCCryptographerTest {

    @BeforeClass
    public static void initialize() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testEncryptionCallsForArc4() throws Exception {
        Encryption encryption = new Encryption(EncryptionType.ARC4);
        String expectedInstanceCall = "ARC4";

        byte[] input = new byte[]{
                0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b,
                0x0c, 0x0d, 0x0e, 0x0f
        };

        // setup spy
        Cipher spyCipher = PowerMockito.spy(Cipher.getInstance(expectedInstanceCall, "BC"));
        PowerMockito.spy(Cipher.class);
        PowerMockito.doReturn(spyCipher).when(Cipher.class, "getInstance", anyString(), anyString());

        Cryptographer cryptographer = new BCCryptographer();
        Key key = KeyGenerator.getInstance(encryption.getType().getName(), "BC").generateKey();
        cryptographer.setEncryption(encryption);
        cryptographer.setKey(key);

        // encrypt
        InputStream encryptor = cryptographer.getEncryptor(new ByteArrayInputStream(input));
        byte[] encryptionResult = IOUtils.toByteArray(encryptor);

        // check for correct instance call
        PowerMockito.verifyStatic(Mockito.times(1));
        Cipher.getInstance(expectedInstanceCall, "BC");

        // check for correct calls
        Mockito.verify(spyCipher, Mockito.times(1)).init(Cipher.ENCRYPT_MODE, key); // should init cipher for encryption
        Mockito.verify(spyCipher).update(any(byte[].class), anyInt(), anyInt());    // should update
        Mockito.verify(spyCipher, Mockito.times(1)).doFinal();                      // should doFinal
    }

    @Test
    public void testDecryptionCallsForArc4() throws Exception {
        Encryption encryption = new Encryption(EncryptionType.ARC4);
        String expectedInstanceCall = "ARC4";
        byte[] input = new byte[]{
                0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b,
                0x0c, 0x0d, 0x0e, 0x0f
        };

        // setup spy
        Cipher spyCipher = PowerMockito.spy(Cipher.getInstance(expectedInstanceCall, "BC"));
        PowerMockito.spy(Cipher.class);
        PowerMockito.doReturn(spyCipher).when(Cipher.class, "getInstance", anyString(), anyString());

        Cryptographer cryptographer = new BCCryptographer();
        Key key = KeyGenerator.getInstance(encryption.getType().getName(), "BC").generateKey();
        cryptographer.setEncryption(encryption);
        cryptographer.setKey(key);

        // decrypt
        InputStream decryptor = cryptographer.getDecryptor(new ByteArrayInputStream(input));
        byte[] decryptionResult = IOUtils.toByteArray(decryptor);

        // check for correct instance call
        PowerMockito.verifyStatic(Mockito.times(1));
        Cipher.getInstance(expectedInstanceCall, "BC");

        // check for correct calls
        Mockito.verify(spyCipher, Mockito.times(1)).init(Cipher.DECRYPT_MODE, key); // should init cipher for decryption
        Mockito.verify(spyCipher).update(any(byte[].class), anyInt(), anyInt());    // should update
        Mockito.verify(spyCipher, Mockito.times(1)).doFinal();                      // should doFinal
    }

    @Test
    public void testEncryptionCallsForAESWithCBCAndIvPresent() throws Exception {
        byte[] input = new byte[]{
                0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b,
                0x0c, 0x0d, 0x0e, 0x0f
        };

        byte[] iv = new byte[]{
                0x1f, (byte) 0xa7, 0x2b, 0x0b,
                (byte) 0xaf, 0x3b, 0x3d, 0x06,
                0x64, (byte) 0x98, 0x24, 0x04,
                (byte) 0x8a, (byte) 0xbb, (byte) 0xcc, 0x00
        };

        Encryption encryption = new Encryption(EncryptionType.AES);
        encryption.setMode(EncryptionMode.CBC);
        encryption.setPadding(EncryptionPadding.PKCS7Padding);
        encryption.setInitializationVector(iv);
        String expectedInstanceCall = "AES/CBC/PKCS7Padding";

        // setup spy
        Cipher spyCipher = PowerMockito.spy(Cipher.getInstance(expectedInstanceCall, "BC"));
        PowerMockito.spy(Cipher.class);
        PowerMockito.doReturn(spyCipher).when(Cipher.class, "getInstance", anyString(), anyString());

        Cryptographer cryptographer = new BCCryptographer();
        Key key = KeyGenerator.getInstance(encryption.getType().getName(), "BC").generateKey();
        cryptographer.setEncryption(encryption);
        cryptographer.setKey(key);

        // encrypt
        InputStream encryptor = cryptographer.getEncryptor(new ByteArrayInputStream(input));
        byte[] encryptionResult = IOUtils.toByteArray(encryptor);

        // check for correct instance call
        PowerMockito.verifyStatic(Mockito.times(1));
        Cipher.getInstance(expectedInstanceCall, "BC");

        // check for correct calls
        Mockito.verify(spyCipher, Mockito.times(1)).init(anyInt(), any(), any(IvParameterSpec.class));  // should use already present IV
        Mockito.verify(spyCipher).update(any(byte[].class), anyInt(), anyInt());
        Mockito.verify(spyCipher, Mockito.times(1)).doFinal();
    }

    @Test
    public void testEncryptionCallsForAESWithCBCAndIvNotPresent() throws Exception {
        byte[] input = new byte[]{
                0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b,
                0x0c, 0x0d, 0x0e, 0x0f
        };
        Encryption encryption = new Encryption(EncryptionType.AES);
        encryption.setMode(EncryptionMode.CBC);
        encryption.setPadding(EncryptionPadding.PKCS7Padding);
        String expectedInstanceCall = "AES/CBC/PKCS7Padding";

        // setup spy
        Cipher spyCipher = PowerMockito.spy(Cipher.getInstance(expectedInstanceCall, "BC"));
        PowerMockito.spy(Cipher.class);
        PowerMockito.doReturn(spyCipher).when(Cipher.class, "getInstance", anyString(), anyString());

        Cryptographer cryptographer = new BCCryptographer();
        Key key = KeyGenerator.getInstance(encryption.getType().getName(), "BC").generateKey();
        cryptographer.setEncryption(encryption);
        cryptographer.setKey(key);

        // encrypt
        InputStream encryptor = cryptographer.getEncryptor(new ByteArrayInputStream(input));
        byte[] encryptionResult = IOUtils.toByteArray(encryptor);

        // check for correct instance call
        PowerMockito.verifyStatic(Mockito.times(1));
        Cipher.getInstance(expectedInstanceCall, "BC");

        // check for correct calls
        Mockito.verify(spyCipher, Mockito.times(1)).init(anyInt(), any(Key.class)); // should let BC generate IV
        Mockito.verify(spyCipher).update(any(byte[].class), anyInt(), anyInt());
        Mockito.verify(spyCipher, Mockito.times(1)).doFinal();
    }

    @Test
    public void testAESEncryptionInCBCMode() throws Exception {
        byte[] input = new byte[]{
                0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b,
                0x0c, 0x0d, 0x0e, 0x0f
        };

        byte[] expectedResult = new byte[]{
                (byte) 0xe8, (byte) 0xeb, 0x6f, (byte) 0x29,
                0x7b, (byte) 0xda, (byte) 0xc1, 0x39,
                (byte) 0xde, (byte) 0xc6, (byte) 0x8d, (byte) 0x72,
                0x7e, (byte) 0x99, (byte) 0xce, 0x0b
        };

        Encryption encryption = new Encryption(EncryptionType.AES);
        encryption.setMode(EncryptionMode.CBC);
        encryption.setPadding(EncryptionPadding.NoPadding);
        byte[] keyBytes = new byte[]{
                45, 9, 89, 93,
                39, -5, 2, 38,
                52, -111, -91, -118,
                0, 121, 110, 35
        };

        byte[] iv = new byte[]{
                0x1f, (byte) 0xa7, 0x2b, 0x0b,
                (byte) 0xaf, 0x3b, 0x3d, 0x06,
                0x64, (byte) 0x98, 0x24, 0x04,
                (byte) 0x8a, (byte) 0xbb, (byte) 0xcc, 0x00
        };
        encryption.setInitializationVector(iv);

        // set encryption
        Cryptographer cryptographer = new BCCryptographer();
        cryptographer.setEncryption(encryption);

        // set key
        Key key = new SecretKeySpec(keyBytes, "BC");
        cryptographer.setKey(key);

        // encrypt
        InputStream encryptor = cryptographer.getEncryptor(new ByteArrayInputStream(input));
        byte[] result = IOUtils.toByteArray(encryptor);

        Assert.assertThat(result, is(equalTo(expectedResult)));
    }

    @Test
    public void testAESDecryptionInCBCMode() throws Exception {
        byte[] input = new byte[]{
                (byte) 0xe8, (byte) 0xeb, 0x6f, (byte) 0x29,
                0x7b, (byte) 0xda, (byte) 0xc1, 0x39,
                (byte) 0xde, (byte) 0xc6, (byte) 0x8d, (byte) 0x72,
                0x7e, (byte) 0x99, (byte) 0xce, 0x0b
        };

        byte[] expectedResult = new byte[]{
                0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b,
                0x0c, 0x0d, 0x0e, 0x0f
        };
        Encryption encryption = new Encryption(EncryptionType.AES);
        encryption.setMode(EncryptionMode.CBC);
        encryption.setPadding(EncryptionPadding.NoPadding);
        byte[] keyBytes = new byte[]{
                45, 9, 89, 93,
                39, -5, 2, 38,
                52, -111, -91, -118,
                0, 121, 110, 35
        };

        byte[] iv = new byte[]{
                0x1f, (byte) 0xa7, 0x2b, 0x0b,
                (byte) 0xaf, 0x3b, 0x3d, 0x06,
                0x64, (byte) 0x98, 0x24, 0x04,
                (byte) 0x8a, (byte) 0xbb, (byte) 0xcc, 0x00
        };
        encryption.setInitializationVector(iv);

        // set encryption
        Cryptographer cryptographer = new BCCryptographer();
        cryptographer.setEncryption(encryption);

        // set key
        Key key = new SecretKeySpec(keyBytes, "BC");
        cryptographer.setKey(key);

        // decrypt
        InputStream decryptor = cryptographer.getDecryptor(new ByteArrayInputStream(input));
        byte[] result = IOUtils.toByteArray(decryptor);

        Assert.assertThat(result, is(equalTo(expectedResult)));
    }

    @Test
    public void testRSAEncryptionCalls() throws Exception {

        Encryption encryption = new Encryption(EncryptionType.RSA);
        String expectedInstanceCall = "RSA/None/NoPadding";

        byte[] input = new byte[]{
                0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b,
                0x0c, 0x0d, 0x0e, 0x0f
        };

        // setup spy
        Cipher spyCipher = PowerMockito.spy(Cipher.getInstance(expectedInstanceCall, "BC"));
        PowerMockito.spy(Cipher.class);
        PowerMockito.doReturn(spyCipher).when(Cipher.class, "getInstance", anyString(), anyString());

        Cryptographer cryptographer = new BCCryptographer();
        KeyPair keyPair = KeyPairGenerator.getInstance(encryption.getType().getName(), "BC").generateKeyPair();
        cryptographer.setEncryption(encryption);
        cryptographer.setKeyPair(keyPair);

        // encrypt
        InputStream encryptor = cryptographer.getEncryptor(new ByteArrayInputStream(input));
        byte[] encryptionResult = IOUtils.toByteArray(encryptor);

        // check for correct instance call
        PowerMockito.verifyStatic(Mockito.times(1));
        Cipher.getInstance(expectedInstanceCall, "BC");

        // check for correct calls
        Mockito.verify(spyCipher, Mockito.times(1)).init(Cipher.ENCRYPT_MODE, keyPair.getPublic()); // should init cipher with public key
        Mockito.verify(spyCipher).update(any(byte[].class), anyInt(), anyInt());    // should update
        Mockito.verify(spyCipher, Mockito.times(1)).doFinal();                      // should doFinal
    }

    @Test
    public void testRSADecryptionCalls() throws Exception {

        Encryption encryption = new Encryption(EncryptionType.RSA);
        String expectedInstanceCall = "RSA/None/NoPadding";

        int[] inputBase = new int[]{
                0x3f, 0x6a, 0xf8, 0x59, 0x28, 0x0c, 0x57, 0x78, 0xef, 0x29, 0xe2, 0x36, 0xaf, 0xb2, 0x6a, 0xad,
                0xb7, 0x52, 0x62, 0x51, 0xd4, 0xc6, 0xcc, 0x9a, 0xe7, 0x51, 0x36, 0xdf, 0x31, 0x24, 0xc2, 0x0a,
                0xb3, 0xd8, 0x2e, 0x43, 0xb4, 0x97, 0xb8, 0x9c, 0x44, 0x2c, 0x40, 0xc9, 0x4c, 0x14, 0x7c, 0x48,
                0xa7, 0xce, 0xfa, 0xc3, 0x04, 0x4f, 0x8b, 0x09, 0xea, 0x04, 0x52, 0x51, 0x0d, 0x02, 0x9c, 0x64,
                0xc7, 0x40, 0x7c, 0xd8, 0xd1, 0xd1, 0x47, 0x1f, 0x5d, 0xfd, 0x58, 0x25, 0x4c, 0x4a, 0x9d, 0x1e,
                0x5d, 0x20, 0x8b, 0x88, 0xa8, 0x01, 0xeb, 0xc0, 0x0a, 0xf0, 0x9f, 0xb4, 0x0f, 0x01, 0x87, 0xc1,
                0x71, 0x21, 0xa6, 0x6a, 0x34, 0x94, 0x60, 0xdb, 0x1f, 0x09, 0x34, 0xe8, 0xf4, 0x2e, 0x81, 0x4d,
                0x55, 0x04, 0x17, 0xf4, 0x0b, 0xde, 0x39, 0xc8, 0x84, 0x8b, 0x7c, 0x38, 0x76, 0xc4, 0xa6, 0x0e,
                0x0b, 0x25, 0x41, 0x3f, 0xa1, 0x4a, 0xfd, 0x26, 0x5d, 0xa3, 0xe6, 0xcd, 0x59, 0x1f, 0x3b, 0x27,
                0x2c, 0x3f, 0x41, 0x6a, 0x85, 0xde, 0x0f, 0xff, 0xe0, 0xff, 0xe8, 0xd9, 0x38, 0x3c, 0x6e, 0x09,
                0xe3, 0x7d, 0xea, 0xc1, 0x82, 0xcb, 0x63, 0xad, 0x03, 0xf8, 0x3e, 0x6b, 0x71, 0x71, 0x73, 0x02,
                0x80, 0xb7, 0xbc, 0xb7, 0x3b, 0x68, 0x0c, 0xcb, 0xf6, 0x0c, 0xb6, 0x19, 0x44, 0xf3, 0xe1, 0x92,
                0x76, 0x61, 0x94, 0xbb, 0xca, 0x7c, 0x5a, 0xcb, 0x62, 0x64, 0xd4, 0x8f, 0x3d, 0x24, 0x6e, 0x6c,
                0x1e, 0xcb, 0xf1, 0x03, 0xa6, 0x04, 0xea, 0x12, 0x82, 0xa9, 0x4d, 0x26, 0x65, 0x4b, 0x85, 0xb1,
                0xd2, 0x80, 0xc6, 0x76, 0x35, 0xb5, 0x48, 0x79, 0xbb, 0x15, 0x3f, 0x80, 0x25, 0x09, 0x7a, 0xd7,
                0x4e, 0x72, 0xd0, 0xde, 0x30, 0x3d, 0xce, 0xd3, 0xf1, 0xef, 0x15, 0xda, 0xef, 0x1d, 0xa3, 0x51
        };

        // setup spy
        Cipher spyCipher = PowerMockito.spy(Cipher.getInstance(expectedInstanceCall, "BC"));
        PowerMockito.spy(Cipher.class);
        PowerMockito.doReturn(spyCipher).when(Cipher.class, "getInstance", anyString(), anyString());

        Cryptographer cryptographer = new BCCryptographer();

        KeyPair keyPair = getRSAKeyPair();
        cryptographer.setEncryption(encryption);
        cryptographer.setKeyPair(keyPair);

        // encrypt
        InputStream encryptor = cryptographer.getDecryptor(new ByteArrayInputStream(toByteArray(inputBase)));
        byte[] decryptionResult = IOUtils.toByteArray(encryptor);

        // check for correct instance call
        PowerMockito.verifyStatic(Mockito.times(1));
        Cipher.getInstance(expectedInstanceCall, "BC");

        // check for correct calls
        Mockito.verify(spyCipher, Mockito.times(1)).init(Cipher.DECRYPT_MODE, keyPair.getPrivate()); // should init cipher with private key
        Mockito.verify(spyCipher).update(any(byte[].class), anyInt(), anyInt());    // should update
        Mockito.verify(spyCipher, Mockito.times(1)).doFinal();                      // should doFinal
    }

    /**
     * Return length many bytes of the passed in byte array as a hex string.
     *
     * @param data   the bytes to be converted.
     * @param length the number of bytes in the data block to be converted.
     * @return a hex representation of length bytes of data.
     */
    public static String toHex(byte[] data, int length) {
        String digits = "0123456789abcdef";
        StringBuffer
                buf = new StringBuffer();
        for (int i = 0; i != length; i++) {
            int
                    v = data[i] & 0xff;
            buf.append(digits.charAt(v >> 4));
            buf.append(digits.charAt(v & 0xf));
        }
        return buf.toString();
    }

    /**
     * Return the passed in byte array as a hex string.
     *
     * @param data the bytes to be converted.
     * @return a hex representation of data.
     */
    public static String toHex(byte[] data) {
        return toHex(data, data.length);
    }

    /**
     * Utility method to make static array initialization based on hex strings easier
     */
    private static String hexToByteArray(String hex) {
        String out = "";
        for (int i = 1; i < hex.length() - 1; i += 2) {
            out += "0x";
            out += hex.charAt(i - 1);
            out += hex.charAt(i);
            out += ", ";
        }
        return out;
    }

    /**
     * Utility method to be able to define byte arrays without casting some values
     */
    private static byte[] toByteArray(int[] data) {
        byte[] output = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = (byte) data[i];
        }
        return output;
    }

    private static KeyPair getRSAKeyPair() throws Exception {
        int[] keyBase = new int[]{
                0x30, 0x82, 0x04, 0xbd, 0x02, 0x01, 0x00, 0x30, 0x0d, 0x06, 0x09, 0x2a, 0x86, 0x48, 0x86, 0xf7,
                0x0d, 0x01, 0x01, 0x01, 0x05, 0x00, 0x04, 0x82, 0x04, 0xa7, 0x30, 0x82, 0x04, 0xa3, 0x02, 0x01,
                0x00, 0x02, 0x82, 0x01, 0x01, 0x00, 0xd3, 0xf4, 0x77, 0x5a, 0xb9, 0x09, 0x39, 0x8e, 0xdb, 0x27,
                0x01, 0x3c, 0x3f, 0xff, 0xe4, 0x34, 0x51, 0x71, 0x3a, 0xe8, 0xb9, 0x2a, 0xb3, 0x0c, 0x95, 0x65,
                0x6f, 0x55, 0x9c, 0xf2, 0x3d, 0x1b, 0xf4, 0x71, 0x94, 0x27, 0xfa, 0x84, 0x9b, 0x9c, 0xd6, 0x67,
                0x6a, 0xc6, 0x0e, 0xde, 0x03, 0x61, 0xe0, 0x63, 0x0d, 0xde, 0x25, 0x4c, 0xdb, 0x68, 0x22, 0x55,
                0x95, 0xe6, 0x5a, 0x05, 0x15, 0x77, 0x33, 0x12, 0x38, 0x08, 0xca, 0x14, 0xf1, 0xe7, 0xb0, 0x2e,
                0x5b, 0xf9, 0x64, 0x3c, 0x59, 0x7a, 0xa2, 0x4d, 0x98, 0xea, 0x62, 0xd4, 0x54, 0x38, 0x2e, 0xee,
                0xe2, 0x67, 0xaf, 0xf6, 0x6e, 0x74, 0xe2, 0xfc, 0xaf, 0x6c, 0x01, 0xc3, 0x97, 0x88, 0x28, 0xbf,
                0x03, 0xda, 0x2c, 0xc9, 0xff, 0xea, 0x51, 0x8f, 0xe8, 0x26, 0x6b, 0xcb, 0x1a, 0x5c, 0xcc, 0x01,
                0xf1, 0xe0, 0x56, 0x8c, 0x24, 0xd9, 0x6a, 0xd2, 0x2f, 0x92, 0x9f, 0xd1, 0x97, 0xae, 0xb6, 0x58,
                0x60, 0x31, 0xbb, 0x60, 0x6e, 0xe2, 0x58, 0x79, 0x63, 0xcc, 0x29, 0xa2, 0xc4, 0x31, 0xb5, 0x6b,
                0x2f, 0x18, 0x47, 0x77, 0x3b, 0x02, 0xea, 0x44, 0xe1, 0x71, 0x29, 0x6a, 0xdf, 0x8c, 0xf4, 0x9d,
                0x5f, 0xb0, 0x8c, 0x47, 0xfd, 0x58, 0xf3, 0xcc, 0x98, 0x95, 0xb7, 0xe7, 0x28, 0x32, 0x48, 0xec,
                0x63, 0xe7, 0x60, 0xab, 0xa0, 0x37, 0x1b, 0xe2, 0x28, 0x05, 0x68, 0xc7, 0x5d, 0xea, 0x5d, 0x4e,
                0x66, 0x15, 0xf6, 0x64, 0x05, 0x03, 0x5f, 0xda, 0x8e, 0x63, 0x6a, 0xe3, 0x44, 0xf2, 0xd2, 0xd8,
                0x94, 0x54, 0xbc, 0xb9, 0x94, 0x4b, 0x08, 0x13, 0x76, 0xb6, 0xd8, 0xfa, 0x68, 0xe3, 0xc3, 0x9a,
                0x57, 0xc3, 0xde, 0xb1, 0xd8, 0xb7, 0x52, 0x85, 0x8a, 0xe9, 0xd6, 0x1c, 0x8e, 0x78, 0xfb, 0x23,
                0xd3, 0xb2, 0xc3, 0x03, 0xc8, 0x09, 0x02, 0x03, 0x01, 0x00, 0x01, 0x02, 0x82, 0x01, 0x00, 0x1c,
                0xfb, 0x58, 0x47, 0xa4, 0x36, 0x05, 0xf4, 0x09, 0xf4, 0xdd, 0x27, 0x21, 0x24, 0x2b, 0x0e, 0xd9,
                0x55, 0x07, 0x86, 0x53, 0x6d, 0x6a, 0x82, 0xad, 0xb9, 0xeb, 0x3d, 0x94, 0x62, 0x8e, 0x27, 0x10,
                0xa7, 0x68, 0x28, 0x08, 0x4a, 0x12, 0x22, 0x21, 0xb2, 0x0d, 0xe8, 0x4b, 0x97, 0xa1, 0x74, 0x44,
                0x73, 0x9e, 0xed, 0x91, 0x81, 0x9f, 0x1d, 0xaf, 0xd6, 0x2e, 0x93, 0x4b, 0x1b, 0x57, 0x5a, 0x4f,
                0x76, 0x2e, 0x09, 0xcb, 0xa2, 0x0e, 0x0a, 0xcd, 0x6d, 0x25, 0x5f, 0xe6, 0x4c, 0x17, 0x3b, 0x1c,
                0xce, 0xc9, 0xfa, 0x40, 0xad, 0xc2, 0x5f, 0x55, 0x51, 0x8e, 0x4d, 0x39, 0xc3, 0x13, 0x4a, 0xf1,
                0xcf, 0xb5, 0xe7, 0xe9, 0x9c, 0xd6, 0x4f, 0x44, 0xad, 0x0b, 0xc8, 0x8d, 0x27, 0xc8, 0xd1, 0xc0,
                0x39, 0xd2, 0x77, 0x50, 0x75, 0x7a, 0xe3, 0x15, 0xf5, 0x2d, 0x1e, 0xed, 0x0e, 0xa8, 0x29, 0xff,
                0xb5, 0x6c, 0xf4, 0xf3, 0x8e, 0xa9, 0x8a, 0xa6, 0x1d, 0xf0, 0x85, 0x55, 0xbe, 0x8f, 0x2a, 0x50,
                0x5d, 0x69, 0x7f, 0xe0, 0x1f, 0xfb, 0xf1, 0x46, 0xc2, 0x44, 0x9f, 0xa8, 0xaa, 0x78, 0xbb, 0x38,
                0xa6, 0x8c, 0xac, 0x8f, 0xc5, 0x3e, 0x30, 0xbd, 0x48, 0x3f, 0xba, 0x80, 0x4a, 0xe7, 0xbc, 0x08,
                0x7c, 0x72, 0xdc, 0x24, 0x78, 0xb3, 0xcc, 0x04, 0xf3, 0xa2, 0x63, 0x49, 0x49, 0xbd, 0x48, 0x03,
                0x9b, 0x26, 0x8e, 0xe4, 0x83, 0x4e, 0x47, 0x59, 0x29, 0x86, 0xf7, 0x15, 0x45, 0xd9, 0x2b, 0x3b,
                0x5c, 0xf2, 0x45, 0xba, 0xb7, 0xaf, 0x8d, 0x1f, 0x0c, 0xc0, 0x63, 0x93, 0x47, 0xd9, 0x8a, 0xe6,
                0x87, 0xf9, 0x3e, 0x3a, 0x03, 0x92, 0x98, 0xb4, 0xf2, 0xf4, 0xef, 0xf9, 0xf5, 0xc1, 0xca, 0x90,
                0x6a, 0xd3, 0xc2, 0x54, 0x0f, 0xe7, 0xbb, 0x88, 0x85, 0xaa, 0xa7, 0xa4, 0xce, 0x46, 0x85, 0x02,
                0x81, 0x81, 0x00, 0xf5, 0xab, 0x8c, 0xef, 0x7f, 0x19, 0xcd, 0x22, 0xc6, 0x09, 0xce, 0x27, 0x62,
                0x42, 0xf0, 0x10, 0x95, 0x2e, 0x82, 0x74, 0x36, 0xa7, 0x27, 0xdc, 0x5e, 0x8f, 0x8e, 0x6b, 0x5b,
                0x4a, 0x45, 0x9f, 0x0f, 0xe7, 0x7f, 0x66, 0x6d, 0x3b, 0x0c, 0x7c, 0x2a, 0x0b, 0x9e, 0xc7, 0xec,
                0xac, 0xec, 0xe2, 0x56, 0xba, 0x38, 0x26, 0xbe, 0x14, 0x2a, 0x52, 0x38, 0x17, 0x5f, 0x67, 0x50,
                0x0a, 0x33, 0xbb, 0x0e, 0x8e, 0x67, 0xd5, 0x3b, 0x4d, 0xfc, 0xad, 0x3e, 0xb0, 0xbd, 0x11, 0x4f,
                0xdf, 0x03, 0xc1, 0xd2, 0x26, 0x2f, 0x1f, 0xbb, 0xcd, 0xc8, 0x19, 0xa7, 0xe0, 0x19, 0x10, 0x71,
                0x7f, 0xde, 0x53, 0x43, 0x0b, 0xd8, 0x1a, 0x77, 0xac, 0xa2, 0xc1, 0xfd, 0xdb, 0x80, 0xd9, 0x6d,
                0x7a, 0x12, 0xa7, 0x34, 0xc4, 0x35, 0x7c, 0x3b, 0x27, 0xed, 0x0a, 0x6b, 0xf1, 0x2c, 0x20, 0x6b,
                0xe6, 0x56, 0x95, 0x02, 0x81, 0x81, 0x00, 0xdc, 0xdd, 0xff, 0x72, 0xff, 0x97, 0x3a, 0x87, 0x7d,
                0xef, 0x43, 0xfa, 0xab, 0xd9, 0x7c, 0x6c, 0x60, 0x4a, 0xef, 0x22, 0x27, 0x31, 0xa1, 0x39, 0xa8,
                0xa8, 0xd6, 0x8c, 0x72, 0x88, 0x1e, 0xc5, 0x89, 0x1c, 0x79, 0xc1, 0xc2, 0xdb, 0x12, 0xb8, 0xb4,
                0x50, 0xce, 0x1f, 0x02, 0x14, 0x93, 0x3d, 0x5d, 0x80, 0xaa, 0x6b, 0xaa, 0xb7, 0xdd, 0xcb, 0x79,
                0xce, 0x50, 0xe2, 0x7d, 0x33, 0x4d, 0x2a, 0xdc, 0x05, 0xad, 0xa9, 0x35, 0x6e, 0x80, 0x68, 0x98,
                0x02, 0x76, 0xc5, 0xa3, 0x5f, 0x38, 0x0f, 0x77, 0x7d, 0x01, 0x2a, 0x54, 0xa2, 0x7b, 0x55, 0x25,
                0xcd, 0xb5, 0xed, 0x85, 0xc4, 0xf7, 0xc5, 0xdf, 0xdf, 0x5a, 0x62, 0xd6, 0x81, 0xe9, 0xdc, 0x9f,
                0xa7, 0x3b, 0x55, 0x0f, 0x13, 0x2b, 0xee, 0xb3, 0x5d, 0xb3, 0x1f, 0xfa, 0x76, 0xd3, 0x4f, 0xf8,
                0x7c, 0x0f, 0x8a, 0x87, 0x39, 0x92, 0xa5, 0x02, 0x81, 0x81, 0x00, 0xb7, 0x6d, 0xc8, 0x84, 0x45,
                0xed, 0x58, 0xda, 0x3e, 0xed, 0xb1, 0x4c, 0x7c, 0xb4, 0xa8, 0x14, 0x69, 0x9e, 0xd9, 0x6e, 0xb6,
                0x23, 0xe6, 0xc2, 0x46, 0xa4, 0x7f, 0x5d, 0x2f, 0x43, 0x6c, 0x6a, 0x50, 0x70, 0xb4, 0x12, 0x21,
                0x3a, 0xb6, 0x9e, 0xba, 0xb2, 0x04, 0x73, 0x18, 0x07, 0x21, 0xb3, 0xf9, 0xb7, 0x7f, 0x17, 0x86,
                0x52, 0xfe, 0x2a, 0x77, 0x91, 0x13, 0xfd, 0xca, 0xa8, 0x63, 0x69, 0xa6, 0x7b, 0xb3, 0x91, 0x65,
                0x7f, 0x5c, 0x4b, 0x6a, 0x84, 0x82, 0x16, 0xbc, 0x01, 0x33, 0x07, 0xf3, 0xc5, 0xe8, 0xe7, 0x93,
                0xcd, 0x19, 0x3b, 0xf6, 0xb9, 0x7f, 0x5d, 0x64, 0xb1, 0x69, 0xa9, 0xd7, 0x64, 0xaf, 0x8d, 0x4b,
                0x8f, 0xcd, 0xd2, 0x74, 0x9e, 0x01, 0x3d, 0xd6, 0xdd, 0x0f, 0xb2, 0x62, 0xd6, 0xa1, 0x6c, 0xc9,
                0xaa, 0x8c, 0xf5, 0x7c, 0x15, 0x0d, 0xb2, 0x83, 0x7d, 0xef, 0x65, 0x02, 0x81, 0x80, 0x6e, 0x72,
                0x04, 0xbf, 0x78, 0xd8, 0x22, 0xc8, 0x86, 0x4d, 0x13, 0x6d, 0x52, 0x6a, 0x1b, 0x32, 0x06, 0xa4,
                0xf2, 0x17, 0xde, 0x09, 0x8f, 0x59, 0xc9, 0xe1, 0x44, 0x08, 0x4f, 0x6d, 0x8a, 0x15, 0xb5, 0x8e,
                0xab, 0xc4, 0x1c, 0xb0, 0x78, 0x18, 0x2c, 0xf3, 0x0b, 0x5f, 0xae, 0x26, 0xa3, 0xca, 0x57, 0xeb,
                0x73, 0x4d, 0x0b, 0xbd, 0x8e, 0x07, 0xa6, 0x0d, 0x69, 0x9d, 0x18, 0x86, 0x82, 0xdc, 0x1f, 0xd2,
                0x27, 0x19, 0x2a, 0x42, 0xeb, 0xab, 0x61, 0xf8, 0x39, 0x74, 0x73, 0x51, 0xb6, 0x32, 0xe5, 0xc9,
                0x78, 0xbf, 0xb6, 0x3a, 0xd9, 0xf9, 0xa6, 0x41, 0xcb, 0xeb, 0xce, 0xac, 0xc4, 0x4a, 0x0a, 0xbd,
                0x21, 0xf1, 0xb2, 0xda, 0x9c, 0x1b, 0x5b, 0x2a, 0xc1, 0x1c, 0xb3, 0xc1, 0x1e, 0x82, 0xea, 0xe6,
                0xbc, 0x20, 0x0d, 0x9b, 0x45, 0xe8, 0x03, 0x95, 0xe6, 0x1f, 0x9b, 0xa7, 0x4a, 0x91, 0x02, 0x81,
                0x80, 0x09, 0xdb, 0x83, 0x63, 0xe4, 0xd1, 0x41, 0x93, 0xb3, 0xb4, 0x33, 0xb0, 0xbd, 0xab, 0xf3,
                0x1c, 0x39, 0xc9, 0x89, 0x92, 0x93, 0xfe, 0x65, 0xf0, 0xbd, 0xf0, 0xc9, 0x4d, 0x0a, 0x6e, 0x3c,
                0x16, 0x52, 0xf9, 0x2f, 0x3d, 0xbb, 0x35, 0x49, 0x1f, 0x68, 0xcf, 0x2a, 0x26, 0x30, 0x34, 0x35,
                0x2e, 0xe0, 0x56, 0x3a, 0x43, 0x05, 0x76, 0x1c, 0xff, 0x32, 0x24, 0x29, 0x01, 0x54, 0xa7, 0x38,
                0x0b, 0x46, 0x0e, 0xf6, 0xf5, 0xfa, 0x37, 0xe8, 0xa4, 0xff, 0xa7, 0x13, 0x42, 0x16, 0xde, 0x49,
                0x46, 0x54, 0xb6, 0x4d, 0xc4, 0xcd, 0x64, 0xaa, 0xa6, 0x44, 0x61, 0x60, 0x59, 0xee, 0x9d, 0x42,
                0xbc, 0xe0, 0xe3, 0xb1, 0xb4, 0x73, 0x09, 0xc5, 0xd3, 0x57, 0x72, 0x26, 0x50, 0xce, 0xbc, 0x6f,
                0x84, 0x9e, 0x7f, 0xb1, 0x09, 0x15, 0x0f, 0x7c, 0x4a, 0x27, 0x27, 0x28, 0xff, 0x79, 0xdc, 0x57, 0x80
        };

        KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(toByteArray(keyBase)));
        RSAPrivateCrtKey privk = (RSAPrivateCrtKey) privateKey;
        PublicKey publicKey = kf.generatePublic(new RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent()));
        return new KeyPair(publicKey, privateKey);
    }


}