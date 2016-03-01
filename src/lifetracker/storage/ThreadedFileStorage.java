package lifetracker.storage;

import lifetracker.calendar.CalendarList;

import java.io.File;
import java.io.IOException;

public class ThreadedFileStorage implements Storage {

    public static final String DEFAULT_FILENAME = "lifetracker.dat";

    public static final String FILE_IS_DIRECTORY_ERROR = "Filename provided is actually a directory!";

    private File storageFile;

    public ThreadedFileStorage() throws IOException {
        this(DEFAULT_FILENAME);
    }

    public ThreadedFileStorage(String fileName) throws IOException {
       setStore(fileName);
    }

    @Override
    public void setStore(String destination) throws IOException {
        storageFile = new File(destination);

        if (!storageFile.exists()) {
            storageFile.createNewFile();
        } else if (storageFile.isDirectory()) {
            throw new IOException(FILE_IS_DIRECTORY_ERROR);
        }
    }

    @Override
    public void store(CalendarList calendar) {

    }

    @Override
    public CalendarList load() {
        return null;
    }
}
