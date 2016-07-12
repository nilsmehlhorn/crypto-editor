package org.hsd.cryptoeditor.crypto.grapher;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.Security;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class BCCryptographerTest {

    @Before
    public void initialize() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Ignore
    @Test
    public void testAESEncryption() throws Exception {
        byte[] input = new byte[]{
            0x0, 0x1, 0x2, 0x3,
            0x4, 0x5, 0x6, 0x7,
            0x8, 0x9, 0xa, 0xb,
            0xc, 0xd, 0xe, 0xf
        };
        Encryption encryption = new Encryption(EncryptionType.AES);
        encryption.setMode(EncryptionMode.CBC);
        encryption.setPadding(EncryptionPadding.PKCS7Padding);
        byte[] keyBytes = new byte[]{
                45, 9, 89, 93,
                39, -5, 2, 38,
                52, -111, -91, -118,
                0, 121, 110, 35
        };

        Key key = KeyGenerator.getInstance(encryption.getType().getName()).generateKey();
        Cryptographer cryptographer = new BCCryptographer();
        cryptographer.setEncryption(encryption);
        cryptographer.setKey(key);
        InputStream encryptor = cryptographer.getEncryptor(new ByteArrayInputStream(input));
        byte[] result = IOUtils.toByteArray(encryptor);
        String expectedHex = "9f59ae43693c954ccf530da5cf0205ac";
        Assert.assertThat(toHex(result), is(equalTo(expectedHex)));
    }

    private static String digits = "0123456789abcdef";

    /**
     * Return length many bytes of the passed in byte array as a hex string.
     *
     * @param data   the bytes to be converted.
     * @param length the number of bytes in the data block to be converted.
     * @return a hex representation of length bytes of data.
     */
    public static String toHex(byte[] data, int length) {
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


}