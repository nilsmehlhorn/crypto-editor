package org.hsd.cryptoeditor.concurrency;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;

public class LoadService extends Service<byte[]> {

    private StringProperty url = new SimpleStringProperty();

    public final void setUrl(String value) {
        url.set(value);
    }

    public final String getUrl() {
        return url.get();
    }

    protected Task<byte[]> createTask() {
        return new Task<byte[]>() {

            protected byte[] call() throws Exception {
                File file = new File(url.get());
                InputStream is = new FileInputStream(url.get());
                long length = file.length();
                if (length > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("File too large");
                }

                byte[] bytes = new byte[(int) length];

                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length &&
                        (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }

                if(offset < bytes.length) {
                    throw new IOException("Could not completeley read file " + file.getName());
                }

                is.close();
                return bytes;
            }
        };
    }
}
