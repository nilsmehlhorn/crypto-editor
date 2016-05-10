package org.hsd.cryptoeditor.service;

import javafx.beans.property.*;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.apache.commons.io.IOUtils;
import org.hsd.cryptoeditor.concurrency.LoadService;
import org.hsd.cryptoeditor.concurrency.SaveService;
import org.hsd.cryptoeditor.encryption.AESCryptographer;
import org.hsd.cryptoeditor.encryption.Cryptographer;
import org.hsd.cryptoeditor.encryption.DESCryptographer;
import org.hsd.cryptoeditor.encryption.exception.CryptographerException;
import org.hsd.cryptoeditor.model.Document;

import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nils on 5/1/16.
 */
public class DocumentService {

    private ObjectProperty<Document> current = new SimpleObjectProperty<Document>();

    private SecretKey key;

    public void load(File file) {
        final Document document = new Document();
        document.setFile(file);
        final LoadService loadService = new LoadService();
        loadService.setUrl(file.getPath());
        loadService.setOnSucceeded(event -> {
            InputStream contentIn = new ByteArrayInputStream(loadService.getValue());
            try {
                CipherInputStream decryptor = new AESCryptographer().getDecryptor(contentIn, key);
                document.setText(new String(IOUtils.toByteArray(decryptor)));
                current.set(document);
            } catch (CryptographerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            InputStream in = new ByteArrayInputStream(document.getText().getBytes("UTF-8"));
            Cryptographer c = new AESCryptographer();
            CipherInputStream cIn = c.getEncryptor(in, key);
            saveService.setContentInput(cIn);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (CryptographerException e) {
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
            try {
                key = KeyGenerator.getInstance("AES").generateKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public static DocumentService getInstance() {
        return instance;
    }



}
