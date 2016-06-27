package org.hsd.cryptoeditor.crypto.encryption;

/**
 * Created by nils on 6/21/16.
 */
public enum PBEType {


    SHA_128BIT_AES_CBC_BC("PBEWITHSHAAND128BITAES-CBC-BC"),
    SHA_256BIT_AES_CBC_BC("PBEWITHSHAAND256BITAES-CBC-BC"),
    MD5_DES("PBEWithMD5AndDES"),
    SHA_40BIT_RC4("PBEWithSHAAnd40BitRC4");

    private final String name;

    PBEType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
