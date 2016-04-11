package lifetracker.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

//@@author A0091173J

/**
 * A class that writes strings to a file.
 * <p>
 * This class is designed to be run by a single thread. Running it on multiple threads might cause concurrency issues.
 */
public class FileStoreProcess implements Runnable {

    private static final Logger STORE_LOG = Logger.getGlobal();

    private static final String LOG_ENTRY_ADDED = "Storage Thread: Storage entry %1$d added.";
    private static final String LOG_SHUTDOWN_ENTRY_ADDED = "Storage Thread: Shutdown entry added.";
    private static final String LOG_FILE_WRITTEN = "Storage Thread: Entry %1$d written to file.";
    private static final String LOG_ENTRY_SKIPPED = "Storage Thread: Storage entry %1$d skipped.";
    private static final String LOG_STARTUP = "Storage Thread: Starting";
    private static final String LOG_SHUTDOWN = "Storage Thread: Exiting";

    private static final String ERROR_INVALID_FILE = "File is null or is invalid!";
    private static final String ERROR_WAIT_INTERRUPTED = "FileStoreProcess was interrupted while waiting for data to write.";
    private static final String ERROR_FILE_WRITE = "Error writing to file!";

    private final BlockingQueue<WriteNode> writeQueue = new PriorityBlockingQueue<>();
    private File storeFile;

    FileStoreProcess(File storeFile) throws FileNotFoundException {
        assert storeFile != null;

        if (storeFile.isDirectory() || !storeFile.exists()) {
            throw new FileNotFoundException(ERROR_INVALID_FILE);
        }

        this.storeFile = storeFile;
    }

    /**
     * Adds a string to the write queue.
     * <p>
     * Note that this function respects the order in which the strings are submitted, and a string submitted later will
     * effectively override a string submitted earlier.
     *
     * @param saveString The string to write to the save file.
     */
    public void submitSave(String saveString) {
        assert saveString != null;

        WriteNode newNode = new WriteNode(saveString);

        writeQueue.add(newNode);

        STORE_LOG.log(Level.INFO, String.format(LOG_ENTRY_ADDED, newNode.getSequenceNum()));
    }

    /**
     * Submits a request to close the thread.
     * <p>
     * The thread will join after all write requests, including the ones added after this close request, are fulfilled.
     */
    public void submitClose() {
        writeQueue.add(WriteNode.END_NODE);
        STORE_LOG.log(Level.INFO, LOG_SHUTDOWN_ENTRY_ADDED);
    }

    @Override
    public void run() {

        STORE_LOG.log(Level.INFO, LOG_STARTUP);

        long latestWrittenNodeNum = Long.MIN_VALUE;

        try {
            WriteNode currentNode = writeQueue.take();

            while (currentNode != WriteNode.END_NODE) {

                if (currentNode.getSequenceNum() > latestWrittenNodeNum) {

                    latestWrittenNodeNum = currentNode.getSequenceNum();

                    writeNodeToFile(currentNode);

                    STORE_LOG.log(Level.INFO, String.format(LOG_FILE_WRITTEN, currentNode.getSequenceNum()));
                } else {
                    STORE_LOG.log(Level.WARNING, String.format(LOG_ENTRY_SKIPPED, currentNode.getSequenceNum()));
                }

                currentNode = writeQueue.take();
            }

            STORE_LOG.log(Level.INFO, LOG_SHUTDOWN);

        } catch (InterruptedException e) {
            System.err.println(ERROR_WAIT_INTERRUPTED);
        } catch (IOException e) {
            System.err.println(ERROR_FILE_WRITE);
        }
    }

    private void writeNodeToFile(WriteNode currentNode) throws IOException {
        String writeString = currentNode.getContent();

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(storeFile))) {
                fileWriter.write(writeString);
        }
    }

    private static class WriteNode implements Comparable<WriteNode> {

        private static final WriteNode END_NODE = new WriteNode(-1, null);

        private static int nextSequenceNum = 0;

        private final long sequenceNum;
        private String content;

        WriteNode(String content) {
            sequenceNum = nextSequenceNum;
            nextSequenceNum++;

            this.content = content;
        }

        private WriteNode(int sequenceNum, String content) {
            this.sequenceNum = sequenceNum;
            this.content = content;
        }

        public long getSequenceNum() {
            return sequenceNum;
        }

        private String getContent() {
            return content;
        }

        @Override
        public int compareTo(WriteNode o) {
            if (o.sequenceNum == sequenceNum) {
                return 0;
            } else if (o.sequenceNum > sequenceNum) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
