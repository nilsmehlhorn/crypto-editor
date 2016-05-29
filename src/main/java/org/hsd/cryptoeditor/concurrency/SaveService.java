package org.hsd.cryptoeditor.concurrency;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.io.IOUtils;
import org.hsd.cryptoeditor.crypto.CryptoService;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.grapher.Cryptographer;
import org.hsd.cryptoeditor.model.Document;
import org.hsd.cryptoeditor.model.PersistenceDTO;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SaveService extends Service<Void> {
    private StringProperty url = new SimpleStringProperty();
    private Document document;

    public final void setUrl(String value) {
        url.set(value);
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    protected Task<Void> createTask() {
        return new Task<Void>() {

            protected Void call() throws Exception {
                PersistenceDTO dto = new PersistenceDTO();
                dto.setEncryptionType(document.getEncryption().getType());
                dto.setEncryptionMode(document.getEncryption().getMode());
                dto.setEncryptionPadding(document.getEncryption().getPadding());
                InputStream in = new ByteArrayInputStream(document.getText().getBytes("UTF-8"));
                if (document.getEncryption().getType() != EncryptionType.NONE) {
                    Cryptographer cryptographer = CryptoService.getInstance().getCryptographer(document.getEncryption());
                    InputStream cryptoIn = cryptographer.getEncryptor(in);
                    dto.setContent(IOUtils.toByteArray(cryptoIn));
                } else {
                    dto.setContent(IOUtils.toByteArray(in));
                }
                if(document.getEncryption().getMode().isVectorMode()) {
                    dto.setInitializationVector(document.getEncryption().getInitializationVector());
                }
                ObjectMapper mapper = new ObjectMapper();
                InputStream contentInput = new ByteArrayInputStream(mapper.writeValueAsBytes(dto));
                Files.copy(contentInput, Paths.get(url.get()), StandardCopyOption.REPLACE_EXISTING);
                return null;
            }
        };
    }
}
