package lifetracker.storage;

import java.io.IOException;
//@@author A0091173J

/**
 * A storage mechanism for storing calendars.
 */
public interface Storage extends AutoCloseable {

    /**
     * Sets the storage destination, for example, the filename, and initializes the storage.
     *
     * @param destination The destination string.
     */
    void setStoreAndStart(String destination) throws IOException;

    /**
     * Stores the String.
     *
     * @param storeString The String to store.
     * @throws IOException If there was an error storage to the storage medium.
     */
    void store(String storeString) throws IOException;

    /**
     * Loads the data from the storage file and returns it directly as a String.
     *
     * @return The content of the file.
     * @throws IOException If there was an error reading the data file.
     */
    String load() throws IOException;
}
