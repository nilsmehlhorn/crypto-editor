package org.hsd.cryptoeditor;

/**
 * Created by nils on 6/20/16.
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
