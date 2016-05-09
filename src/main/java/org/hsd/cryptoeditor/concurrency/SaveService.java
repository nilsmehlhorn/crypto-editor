package org.hsd.cryptoeditor.concurrency;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;

public class SaveService extends Service {
    private StringProperty url = new SimpleStringProperty();
    private InputStream contentInput;

    public final void setUrl(String value) {
        url.set(value);
    }

    public final String getUrl() {
        return url.get();
    }

    public final void setContentInput(InputStream in) {
        this.contentInput = in;
    }

    protected Task createTask() {
        return new Task() {

            protected Object call() throws Exception {
                FileOutputStream output = new FileOutputStream(url.get());
                byte[] buf = new byte[1024];
                int i;
                while((i = contentInput.read(buf)) > 0) {
                    output.write(buf, 0, i);
                }
                contentInput.close();
                output.close();
                return null;
            }
        };
    }
}
