package lifetracker.storage;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.Event;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ThreadedFileStorage implements Storage {

    public static final String DEFAULT_FILENAME = "lifetracker.dat";

    public static final String FILE_IS_DIRECTORY_ERROR = "Filename provided is actually a directory!";

    private File storageFile;
    private List<Event> eventList;

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
    public void store(CalendarList calendar) throws IOException {
        try (BufferedWriter fileWrite = new BufferedWriter(new FileWriter(storageFile))) {
            writeEvents(fileWrite, eventList);
        }
    }

    @Override
    public CalendarList load() {
        return null;
    }

    private void writeEvents(BufferedWriter writer, List<Event> eventList) throws IOException {
        writer.write(String.valueOf(eventList.size()));

        for (Event event : eventList) {
            writer.newLine();
            writer.write(event.getStart().toString());
            writer.write(" ");
            writer.write(event.getEnd().toString());
            writer.write(" ");
            writer.write(event.getName());
        }
    }
}
