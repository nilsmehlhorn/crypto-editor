package org.hsd.cryptoeditor.concurrency;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.hsd.cryptoeditor.doc.PersistenceDTO;

import java.io.File;

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
