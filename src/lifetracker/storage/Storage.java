package lifetracker.storage;

import java.io.IOException;

/**
 * A storage mechanism for storing calendars.
 */
public interface Storage extends AutoCloseable {

    /**
     * Sets the storage destination, for example, the filename.
     *
     * @param destination The destination string.
     */
    void setStoreAndStart(String destination) throws IOException;

    void store(String storeJsonString) throws IOException;

    /**
     * Loads the data from the storage file and returns it directly as a String.
     *
     * @return The content of the file.
     * @throws IOException If there was an error reading the data file.
     */
    String load() throws IOException;
}
