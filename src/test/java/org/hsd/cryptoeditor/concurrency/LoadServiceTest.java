package org.hsd.cryptoeditor.concurrency;

import de.saxsys.javafx.test.JfxRunner;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionMode;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionPadding;
import org.hsd.cryptoeditor.crypto.encryption.EncryptionType;
import org.hsd.cryptoeditor.doc.PersistenceDTO;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(JfxRunner.class)
public class LoadServiceTest {

    @Test(expected = IllegalStateException.class)
    public void testLoadingWithoutURL() throws Exception {
        // initialize service without url
        LoadService loadService = new LoadService();
        Task<PersistenceDTO> task = loadService.createTask();
    }

    @Test
    public void testLoading() throws Exception {
        String json = "{\"encryptionType\":\"AES\",\"encryptionPadding\":\"PKCS7Padding\",\"encryptionMode\":\"CBC\",\"initializationVector\":\"NDMyMQ==\",\"publicKey\":\"YWJjZA==\",\"content\":\"MTIzNA==\",\"hash\":\"eXhjdmJubQ==\",\"salt\":\"YXNkZmdoamts\"}";
        File testFile = new File("loadingTestDTO");
        testFile.deleteOnExit();
        Files.write(Paths.get(testFile.getPath()), json.getBytes());

        // initialize service
        LoadService loadService = new LoadService();
        loadService.setUrl(testFile.getPath());

        // handle execution on seperate thread by deferring the result
        Task<PersistenceDTO> task = loadService.createTask();
        CompletableFuture<LoadServiceTest.TaskTestResults> compareFuture = createStateCompareFuture(task,
                Worker.State.SUCCEEDED);
        task.run();
        LoadServiceTest.TaskTestResults testResults = compareFuture.get(100, TimeUnit.MILLISECONDS);

        // asser parsing
        PersistenceDTO resultDTO = testResults.result;
        assertNotNull(resultDTO);
        assertThat(resultDTO.getEncryptionMode(), is(EncryptionMode.CBC));
        assertThat(resultDTO.getEncryptionType(), is(EncryptionType.AES));
        assertThat(resultDTO.getEncryptionPadding(), is(EncryptionPadding.PKCS7Padding));
        assertThat(resultDTO.getContent(), is(equalTo("1234".getBytes())));
        assertThat(resultDTO.getInitializationVector(), is(equalTo("4321".getBytes())));
        assertThat(resultDTO.getPublicKey(), is(equalTo("abcd".getBytes())));
        assertThat(resultDTO.getSalt(), is(equalTo("asdfghjkl".getBytes())));
        assertThat(resultDTO.getHash(), is(equalTo("yxcvbnm".getBytes())));

    }

    private CompletableFuture<LoadServiceTest.TaskTestResults> createStateCompareFuture(Task<PersistenceDTO> task,
                                                                                        Worker.State stateToReach) {
        CompletableFuture<LoadServiceTest.TaskTestResults> taskStateReady = new CompletableFuture<>();
        task.stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == stateToReach) {
                        taskStateReady.complete(new LoadServiceTest.TaskTestResults(task.getMessage(),
                                task.getState(), task.getValue()));
                    }
                });
        return taskStateReady;
    }

    private class TaskTestResults {

        String message;
        Worker.State state;
        PersistenceDTO result;

        TaskTestResults(String message, Worker.State state, PersistenceDTO result) {
            this.message = message;
            this.state = state;
            this.result = result;
        }
    }

}