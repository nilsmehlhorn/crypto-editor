package org.hsd.cryptoeditor.concurrency;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.io.IOUtils;
import org.hsd.cryptoeditor.crypto.CryptoService;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.grapher.Cryptographer;
import org.hsd.cryptoeditor.dialog.DialogService;

import java.io.InputStream;
import java.util.Optional;

/**
 * Created by nils on 6/20/16.
 */
public class DecryptionService extends Service<byte[]> {

    private InputStream cipherInput;

    private Encryption encryption;

    private char[] password;

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void setCipherInput(InputStream contentInput) {
        this.cipherInput = contentInput;
    }

    @Override
    protected Task<byte[]> createTask() {
        return new Task<byte[]>() {
            protected byte[] call() throws Exception {
                InputStream source = cipherInput;
                if (encryption.getType() != EncryptionType.NONE) {
                    Cryptographer c = CryptoService.getInstance().getCryptographer(encryption, password);
                    source = c.getDecryptor(cipherInput);
                }
                return IOUtils.toByteArray(source);
            }
        };
    }
}
