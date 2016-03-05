package lifetracker.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A class that writes lines to a file.
 * <p>
 * This class is designed to be run by a single thread. Running it on multiple threads might cause concurrency issues.
 */
public class FileStoreThread implements Runnable {

    private static final String ERROR_INVALID_FILE = "File is null or is invalid!";
    private static final String ERROR_UNINITIALIZED = "Thread is not initialized with init()!";
    private static final String ERROR_WAIT_INTERRUPTED = "FileStoreThread was interrupted while waiting for data to write.";
    private static final String ERROR_FILE_WRITE = "Error writing to file!";

    private BlockingQueue<WriteNode> writeQueue = new PriorityBlockingQueue<>();
    private File storeFile;

    FileStoreThread(File storeFile) throws FileNotFoundException {
        if (storeFile == null || storeFile.isDirectory() || !storeFile.exists()) {
            throw new FileNotFoundException(ERROR_INVALID_FILE);
        }

        this.storeFile = storeFile;
    }

    /**
     * Takes a list of strings, duplicates it, and adds it to the queue to be written.
     * <p>
     * Note that this function repects the order in which the lists are submitted, and a list submitted later will effectively override a list submitted earlier.
     *
     * @param saveList The list to write to the save file.
     */
    public void submitSaveList(List<String> saveList) {
        writeQueue.add(new WriteNode(new ArrayList<>(saveList)));
    }

    @Override
    public void run() {

        long latestWrittenNodeNum = Long.MIN_VALUE;

        try {
            WriteNode currentNode = writeQueue.take();

            while (currentNode != WriteNode.END_NODE) {

                if (currentNode.getSequenceNum() > latestWrittenNodeNum) {

                    latestWrittenNodeNum = currentNode.getSequenceNum();

                    writeNodeToFile(currentNode);
                }

                currentNode = writeQueue.take();
            }
        } catch (InterruptedException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void writeNodeToFile(WriteNode currentNode) throws IOException {
        List<String> writeLineList = currentNode.getContent();

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(storeFile))) {
            for (String line : writeLineList) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
        }
    }

    private static class WriteNode implements Comparable<WriteNode> {

        private static final WriteNode END_NODE = new WriteNode(-2, null);

        private static int nextSequenceNum = 0;

        private final long sequenceNum;
        private List<String> content;

        WriteNode(List<String> content) {
            sequenceNum = nextSequenceNum;
            nextSequenceNum++;

            this.content = content;
        }

        private WriteNode(int sequenceNum, List<String> content) {
            this.sequenceNum = sequenceNum;
            this.content = content;
        }

        public long getSequenceNum() {
            return sequenceNum;
        }

        private List<String> getContent() {
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
