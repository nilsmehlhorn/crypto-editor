package org.hsd.cryptoeditor.concurrency;

import de.saxsys.javafx.test.JfxRunner;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import org.hamcrest.CoreMatchers;
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

@RunWith(JfxRunner.class)
public class SaveServiceTest {

    @Test(expected = IllegalStateException.class)
    public void testSavingWithoutURL() throws Exception {
        PersistenceDTO dto = new PersistenceDTO();

        // initialize service without url
        SaveService saveService = new SaveService();
        saveService.setPersistenceDTO(dto);
        Task<Void> task = saveService.createTask();
    }

    @Test(expected = IllegalStateException.class)
    public void testSavingWithoutDTO() throws Exception {
        File testFile = new File("testDTO");
        testFile.deleteOnExit();    // remove file after test

        PersistenceDTO dto = new PersistenceDTO();

        // initialize service without url
        SaveService saveService = new SaveService();
        saveService.setUrl(testFile.getPath());
        Task<Void> task = saveService.createTask();
    }

    @Test
    public void testSaving() throws Exception {
        File testFile = new File("savingTestDTO");
        testFile.deleteOnExit();    // remove file after test

        // setup dto for saving
        PersistenceDTO dto = new PersistenceDTO();
        dto.setContent("1234".getBytes());
        dto.setInitializationVector("4321".getBytes());
        dto.setPublicKey("abcd".getBytes());
        dto.setEncryptionMode(EncryptionMode.CBC);
        dto.setEncryptionPadding(EncryptionPadding.PKCS7Padding);
        dto.setEncryptionType(EncryptionType.AES);
        dto.setSalt("asdfghjkl".getBytes());
        dto.setHash("yxcvbnm".getBytes());

        // initialize service
        SaveService saveService = new SaveService();
        saveService.setUrl(testFile.getPath());
        saveService.setPersistenceDTO(dto);

        // handle execution on seperate thread by deferring the result
        Task<Void> task = saveService.createTask();
        CompletableFuture<TaskTestResults> compareFuture = createStateCompareFuture(task,
                Worker.State.SUCCEEDED);
        task.run();
        TaskTestResults result = compareFuture.get(100, TimeUnit.MILLISECONDS);

        // assert serialization
        byte[] encoded = Files.readAllBytes(Paths.get(testFile.getPath()));
        String json = new String(encoded);
        System.out.println(json);
        assertThat(json, CoreMatchers.allOf(
                containsString("\"content\":\"MTIzNA=="),
                containsString("\"salt\":\"YXNkZmdoamts"),
                containsString("\"hash\":\"eXhjdmJubQ=="),
                containsString("\"publicKey\":\"YWJjZA=="),
                containsString("\"initializationVector\":\"NDMyMQ=="),
                containsString("\"encryptionType\":\"AES"),
                containsString("\"encryptionMode\":\"CBC\""),
                containsString("\"encryptionPadding\":\"PKCS7Padding\"")));
    }

    private CompletableFuture<TaskTestResults> createStateCompareFuture(Task<Void> task,
                                                                        Worker.State stateToReach) {
        CompletableFuture<TaskTestResults> taskStateReady = new CompletableFuture<>();
        task.stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == stateToReach) {
                        taskStateReady.complete(new TaskTestResults(task.getMessage(),
                                task.getState(), task.getValue()));
                    }
                });
        return taskStateReady;
    }

    private class TaskTestResults {

        String message;
        Worker.State state;
        Void result;

        TaskTestResults(String message, Worker.State state, Void result) {
            this.message = message;
            this.state = state;
            this.result = result;
        }
    }
}