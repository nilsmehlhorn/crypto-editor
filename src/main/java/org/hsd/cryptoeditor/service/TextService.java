package org.hsd.cryptoeditor.service;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.hsd.cryptoeditor.concurrency.LoadService;
import org.hsd.cryptoeditor.model.TextEntity;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by nils on 5/1/16.
 */
public class TextService {

    private ObjectProperty<TextEntity> current = new SimpleObjectProperty<TextEntity>();

    public void load(File file) {
        final TextEntity textEntity = new TextEntity();
        textEntity.setFile(file);
        final LoadService loadService = new LoadService();
        loadService.setUrl(file.getPath());
        loadService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent event) {
                textEntity.setText(new String(loadService.getValue()));
                current.set(textEntity);
            }
        });
        loadService.start();
    }

    public ObjectProperty<TextEntity> currentTextEntityProperty() {
        return current;
    }

    /////

    private static final TextService instance = new TextService();

    private TextService() {
        if(instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static TextService getInstance() {
        return instance;
    }



}
