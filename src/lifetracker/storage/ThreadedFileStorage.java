package lifetracker.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ThreadedFileStorage implements Storage {

    private static final String DEFAULT_FILENAME = "lifetracker.dat";

    private static final String ERROR_FILE_IS_DIRECTORY = "Filename provided is actually a directory!";
    private static final String ERROR_INTERRUPTED_CLOSE = "Thread was interrupted while finishing up requests!";

    private File storageFile;
    private Thread fileStoreThread;
    private FileStoreProcess fileStoreProcess;

    public ThreadedFileStorage() throws IOException {
        this(DEFAULT_FILENAME);
    }

    public ThreadedFileStorage(String fileName) throws IOException {
        storageFile = prepareFile(fileName);
        startThread(storageFile);
    }

    @Override
    public void setStoreAndStart(String destination) throws IOException {
        try {
            stopThread();
        } catch (InterruptedException e) {
            System.err.println(ERROR_INTERRUPTED_CLOSE);
        }

        storageFile = prepareFile(destination);

        startThread(storageFile);
    }

    @Override
    public void store(String storeJsonString) throws IOException {
        assert storeJsonString != null;

        fileStoreProcess.submitSave(storeJsonString);
    }

    @Override
    public String load() throws IOException {
        return new String(Files.readAllBytes(storageFile.toPath()), StandardCharsets.UTF_8);
    }

    @Override
    public void close() throws Exception {
        stopThread();
    }

    private void startThread(File storageFile) throws FileNotFoundException {
        fileStoreProcess = new FileStoreProcess(storageFile);

        fileStoreThread = new Thread(fileStoreProcess);
        fileStoreThread.start();
    }

    private void stopThread() throws InterruptedException {
        fileStoreProcess.submitClose();

        fileStoreThread.join();
    }

    private File prepareFile(String destination) throws IOException {
        File storageFile = new File(destination);

        if (!storageFile.exists()) {
            storageFile.createNewFile();
        } else if (storageFile.isDirectory()) {
            throw new IOException(ERROR_FILE_IS_DIRECTORY);
        }
        return storageFile;
    }

}
