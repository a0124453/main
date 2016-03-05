package lifetracker.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class FileStoreThread implements Runnable{

    private static final String ERROR_INVALID_FILE = "File is null or is invalid!";
    private static final String ERROR_NULL_QUEUE = "Write queue must not be null!";
    private static final String ERROR_UNINITIALIZED = "Thread is not initialized with init()!";

    private static FileStoreThread storeThreadInstance = new FileStoreThread();

    private File storeFile;

    private BlockingQueue<List<String>> writeQueue;

    private boolean initialized = false;

    private FileStoreThread() {
    }

    public static void init(File storeFile, BlockingQueue<List<String>> writeQueue) throws FileNotFoundException {

        if (storeFile == null
                || storeThreadInstance.storeFile.isDirectory()
                || !storeThreadInstance.storeFile.exists()) {
            throw new FileNotFoundException(ERROR_INVALID_FILE);
        }

        if (writeQueue == null) {
            throw new NullPointerException(ERROR_NULL_QUEUE);
        }

        storeThreadInstance.initialized = true;

        storeThreadInstance.storeFile = storeFile;
        storeThreadInstance.writeQueue = writeQueue;
    }

    /**
     * Gets the thread store instance.
     * <p>
     * {@code init()} must be called prior to obtaining an instance, or an Exception will be thrown.
     *
     * @return The FileStoreThread instance
     */
    public static FileStoreThread getInstance() {

        if (!storeThreadInstance.initialized) {
            throw new IllegalStateException(ERROR_UNINITIALIZED);
        }

        return storeThreadInstance;
    }

    @Override
    public void run() {

    }
}
