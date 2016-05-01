package org.hsd.cryptoeditor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

/**
 * Created by nils on 5/1/16.
 */
public class TextEntity {

    private File file;

    private StringProperty content = new SimpleStringProperty();


    public String getText() {
        return content.get();
    }

    public void setText(String s) {
        this.content.set(s);
    }

    public StringProperty textProperty() {
        return content;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
