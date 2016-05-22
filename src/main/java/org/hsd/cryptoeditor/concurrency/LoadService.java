package org.hsd.cryptoeditor.concurrency;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.io.IOUtils;
import org.hsd.cryptoeditor.crypto.CryptoService;
import org.hsd.cryptoeditor.crypto.encryption.Encryption;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.crypto.grapher.Cryptographer;
import org.hsd.cryptoeditor.model.Document;
import org.hsd.cryptoeditor.model.PersistenceDTO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class LoadService extends Service<Document> {

    private StringProperty url = new SimpleStringProperty();

    public final void setUrl(String value) {
        url.set(value);
    }

    protected Task<Document> createTask() {
        return new Task<Document>() {

            protected Document call() throws Exception {
                File file = new File(url.get());
                ObjectMapper objectMapper = new ObjectMapper();
                PersistenceDTO dto = objectMapper.readValue(file, PersistenceDTO.class);
                Document document = new Document();
                Encryption encryption = CryptoService.getInstance().getEncryption(dto.getEncryptionType());
                encryption.setMode(dto.getEncryptionMode());
                encryption.setPadding(dto.getEncryptionPadding());
                if(encryption.getMode().isVectorMode()) {
                    encryption.setInitializationVector(dto.getInitializationVector());
                }
                document.setEncryption(encryption);
                document.setFile(file);
                byte[] bytes = dto.getContent();
                if(encryption.getType() != EncryptionType.NONE) {
                    Cryptographer cryptographer = CryptoService.getInstance().getCryptographer(document.getEncryption());
                    InputStream cryptoIn = cryptographer.getDecryptor(new ByteArrayInputStream(dto.getContent()));
                    bytes = IOUtils.toByteArray(cryptoIn);
                }
                document.setText(new String(bytes));
                return document;
            }
        };
    }
}
