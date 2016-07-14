package org.hsd.cryptoeditor.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.junit.Before;
import org.junit.Test;

import java.security.Security;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CryptoServiceTest {

    @Before
    public void setup() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testGetEncryption() throws Exception {
        CryptoService service = CryptoService.getInstance();
        Encryption aes = service.getEncryption(EncryptionType.AES);
        assertThat(aes.getType(), is(EncryptionType.AES));
        Encryption arc = service.getEncryption(EncryptionType.ARC4);
        assertThat(arc.getType(), is(EncryptionType.ARC4));
        Encryption des = service.getEncryption(EncryptionType.DES);
        assertThat(des.getType(), is(EncryptionType.DES));
        Encryption rsa = service.getEncryption(EncryptionType.RSA);
        assertThat(rsa.getType(), is(EncryptionType.RSA));
    }

    @Test
    public void getCryptographer() throws Exception {

    }

    @Test
    public void testGetHash() throws Exception {
        byte[] expectedHash = new byte[]{
                (byte) 0xff, 0x4e, (byte) 0xf4, 0x24, 0x5d, (byte) 0xa5, (byte) 0xb0, (byte) 0x97,
                (byte) 0x86, (byte) 0xe3, (byte) 0xd3, (byte) 0xde, (byte) 0x8b, 0x43, 0x02, (byte) 0x92,
                (byte) 0xfa, 0x08, 0x19, (byte) 0x84, (byte) 0xdb, 0x27, 0x2d, 0x2b,
                0x13, (byte) 0xed, 0x40, 0x4b, 0x45, 0x35, 0x3d, 0x28
        };
        byte[] input = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.".getBytes();
        assertThat(CryptoService.getInstance().getHash(input), is(equalTo(expectedHash)));
    }

    @Test
    public void testHashMatchWithMatching() throws Exception {
        byte[] hash = new byte[]{
                (byte) 0xff, 0x4e, (byte) 0xf4, 0x24, 0x5d, (byte) 0xa5, (byte) 0xb0, (byte) 0x97,
                (byte) 0x86, (byte) 0xe3, (byte) 0xd3, (byte) 0xde, (byte) 0x8b, 0x43, 0x02, (byte) 0x92,
                (byte) 0xfa, 0x08, 0x19, (byte) 0x84, (byte) 0xdb, 0x27, 0x2d, 0x2b,
                0x13, (byte) 0xed, 0x40, 0x4b, 0x45, 0x35, 0x3d, 0x28
        };
        byte[] input = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.".getBytes();
        assertThat(CryptoService.getInstance().hashMatch(input, hash), is(true));
    }

    @Test
    public void testHashMatchWithNotMatching() throws Exception {
        byte[] hash = new byte[]{
                (byte) 0xff, 0x4e, (byte) 0xf4, 0x24, 0x5d, (byte) 0xa5, (byte) 0xb0, (byte) 0x97,
                (byte) 0x86, (byte) 0xe3, (byte) 0xd3, (byte) 0xde, (byte) 0x8b, 0x43, 0x02, (byte) 0x92,
                (byte) 0xfa, 0x08, 0x19, (byte) 0x84, (byte) 0xdb, 0x27, 0x2d, 0x2b,
                0x13, (byte) 0xed, 0x40, 0x4b, 0x45, 0x35, 0x3d, 0x28
        };
        byte[] input = "1234".getBytes();
        assertThat(CryptoService.getInstance().hashMatch(input, hash), is(false));
    }
}