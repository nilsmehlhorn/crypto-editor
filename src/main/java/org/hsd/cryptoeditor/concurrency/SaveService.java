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
import org.hsd.cryptoeditor.dialog.DialogService;
import org.hsd.cryptoeditor.doc.Document;
import org.hsd.cryptoeditor.doc.PersistenceDTO;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class SaveService extends Service<Void> {
    private StringProperty url = new SimpleStringProperty();
    private PersistenceDTO persistenceDTO;

    public final void setUrl(String value) {
        url.set(value);
    }

    public void setPersistenceDTO(PersistenceDTO persistenceDTO) {
        this.persistenceDTO = persistenceDTO;
    }

    protected Task<Void> createTask() {
        return new Task<Void>() {

            protected Void call() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                InputStream contentInput = new ByteArrayInputStream(mapper.writeValueAsBytes(persistenceDTO));
                Files.copy(contentInput, Paths.get(url.get()), StandardCopyOption.REPLACE_EXISTING);
                return null;
            }
        };
    }
}
