package org.hsd.cryptoeditor.crypto.exception;

/**
 * Created by nils on 5/10/16.
 */
public class CryptographerException extends RuntimeException {
    public CryptographerException(Throwable e) {
        super(e);
    }

    public CryptographerException(String m) {
        super(m);
    }
}
