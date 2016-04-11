package lifetracker.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

//@@author A0091173J

/**
 * A File storage, that runs the file write on a separate thread.
 * <p>
 * This class works by accepting asynchronous request to store data to a file. It then passes the data to a separate
 * thread so it can be stored concurrently. If multiple sets of data are submitted before the first set is written to
 * file, then only the latest set of data will be written.
 */
public class ThreadedFileStorage implements Storage {

    private static final String DEFAULT_FILENAME = "lifetracker.dat";

    private static final String ERROR_FILE_IS_DIRECTORY = "Filename provided is actually a directory!";
    private static final String ERROR_INTERRUPTED_CLOSE = "Thread was interrupted while finishing up requests!";

    private File storageFile;
    private Thread fileStoreThread;
    private FileStoreProcess fileStoreProcess;

    /**
     * Creates a new {@code ThreadedFileStorage} that stores the data in "lifetracker.dat".
     *
     * @throws IOException If there was an accessing the file.
     */
    public ThreadedFileStorage() throws IOException {
        this(DEFAULT_FILENAME);
    }

    /**
     * Creates a new {@code ThreadedFileStorage} that stores the data in the file specified by the file name.
     *
     * @param fileName The file name of the storage file.
     * @throws IOException If there was an error accessing the file.
     */
    public ThreadedFileStorage(String fileName) throws IOException {
        storageFile = prepareFile(fileName);
        startThread(storageFile);
    }

    /**
     * Closes the current running write thread safely, then starts a new thread that writes to the file as specified by
     * filename.
     *
     * @param destination The new file to write to.
     * @throws IOException If there was an error accessing the file.
     */
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

    /**
     * Stores the data to the file.
     * <p>
     * This method only submits the data to the write thread. Write write will happen concurrently.
     *
     * @param storeString The String to store
     * @throws IOException If there was an error writing to the file
     */
    @Override
    public void store(String storeString) throws IOException {
        assert storeString != null;

        fileStoreProcess.submitSave(storeString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String load() throws IOException {
        return new String(Files.readAllBytes(storageFile.toPath()), StandardCharsets.UTF_8);
    }

    /**
     * Closes the write thread safely.
     * <p>
     * This method blocks until the write thread finished writing all previous write submissions.
     *
     * @throws Exception If there was an error stopping the write thread.
     */
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
