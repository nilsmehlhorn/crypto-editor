package org.hsd.cryptoeditor.service;

import javafx.beans.property.*;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.hsd.cryptoeditor.concurrency.LoadService;
import org.hsd.cryptoeditor.concurrency.SaveService;
import org.hsd.cryptoeditor.model.Document;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by nils on 5/1/16.
 */
public class DocumentService {

    private ObjectProperty<Document> current = new SimpleObjectProperty<Document>();

    public void load(File file) {
        final Document document = new Document();
        document.setFile(file);
        final LoadService loadService = new LoadService();
        loadService.setUrl(file.getPath());
        loadService.setOnSucceeded(event -> {
            document.setText(new String(loadService.getValue()));
            current.set(document);
        });
        loadService.setOnFailed(f -> {
            DialogService.getInstance().showErrorDialog("Could not load file", "An error occured while loading the file");
        });
        loadService.start();
    }

    public void saveCurrent(final File file) {
        save(current.get(), file);
    }

    public void save(Document document, File file) {
        SaveService saveService = new SaveService();
        saveService.setUrl(file.getPath());
        try {
            saveService.setContentInput(new ByteArrayInputStream(document.getText().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        saveService.setOnSucceeded(event -> document.setFile(file));
        saveService.start();
    }

    public ObjectProperty<Document> currentDocumentProperty() {
        return current;
    }

    /////

    private static final DocumentService instance = new DocumentService();

    private DocumentService() {
        if(instance != null) {
            throw new IllegalStateException("Already instantiated");
        } else {
            current.set(new Document());
        }
    }

    public static DocumentService getInstance() {
        return instance;
    }



}
