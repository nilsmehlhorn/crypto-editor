package org.hsd.cryptoeditor.crypto.grapher;

/**
 * Runtime Exception for error handling during cryptographic operations.
 */
public class CryptographerException extends RuntimeException {
    public CryptographerException(Throwable e) {
        super(e);
    }

    public CryptographerException(String m) {
        super(m);
    }
}
