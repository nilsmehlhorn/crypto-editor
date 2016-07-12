package org.hsd.cryptoeditor;

/**
 * Runtime Exception for general error handling in the application
 */
public class CryptoEditorException extends RuntimeException {

    public CryptoEditorException(Throwable e) {
        super(e);
    }

    public CryptoEditorException(String m) {
        super(m);
    }

    public CryptoEditorException(String m, Throwable e) {
        super(m, e);
    }
}
