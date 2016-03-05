package lifetracker.storage;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.util.List;

public class ThreadedFileStorage implements Storage, Flushable {

    public static final String DEFAULT_FILENAME = "lifetracker.dat";

    public static final String FILE_IS_DIRECTORY_ERROR = "Filename provided is actually a directory!";

    public static final String NULL_DATETIME_MARKER = "NA";
    public static final String FIELD_SEPARATOR = " ";

    private File storageFile;

    public ThreadedFileStorage() throws IOException {
        this(DEFAULT_FILENAME);
    }

    public ThreadedFileStorage(String fileName) throws IOException {
        setStore(fileName);
    }

    @Override
    public void setStore(String destination) throws IOException {
        flush();

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
            writeEvents(fileWrite, calendar.getEventList());
            writeTasks(fileWrite, calendar.getTaskList());
        }
    }

    @Override
    public CalendarList load(CalendarList calendar) {
        return null;
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws Exception {
        flush();
    }

    private void writeEvents(BufferedWriter writer, List<CalendarEntry> eventList) throws IOException {
        writer.write(String.valueOf(eventList.size()));

        for (CalendarEntry event : eventList) {
            writer.newLine();
            writer.write(event.getStart().toString());
            writer.write(FIELD_SEPARATOR);
            writer.write(event.getEnd().toString());
            writer.write(FIELD_SEPARATOR);
            writer.write(event.getName());
        }
    }

    private void writeTasks(BufferedWriter writer, List<CalendarEntry> taskList) throws IOException {
        writer.write(String.valueOf(taskList.size()));

        for (CalendarEntry task : taskList) {
            writer.newLine();

            if (task.getEndTime() == null) {
                writer.write(NULL_DATETIME_MARKER);
            } else {
                writer.write(task.getEndTime().toString());
            }
            writer.write(FIELD_SEPARATOR);
            writer.write(task.getName());
        }
    }
}
