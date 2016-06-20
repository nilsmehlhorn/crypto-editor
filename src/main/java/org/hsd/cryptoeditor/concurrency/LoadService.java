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
import org.hsd.cryptoeditor.dialog.DialogService;
import org.hsd.cryptoeditor.doc.Document;
import org.hsd.cryptoeditor.doc.PersistenceDTO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Optional;

public class LoadService extends Service<PersistenceDTO> {

    private StringProperty url = new SimpleStringProperty();

    public final void setUrl(String value) {
        url.set(value);
    }

    protected Task<PersistenceDTO> createTask() {
        return new Task<PersistenceDTO>() {

            protected PersistenceDTO call() throws Exception {
                File file = new File(url.get());
                ObjectMapper objectMapper = new ObjectMapper();
                PersistenceDTO dto = objectMapper.readValue(file, PersistenceDTO.class);
                return dto;
            }
        };
    }
}
