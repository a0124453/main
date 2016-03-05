package lifetracker.storage;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThreadedFileStorage implements Storage, Flushable {

    public static final String DEFAULT_FILENAME = "lifetracker.dat";

    public static final String FILE_IS_DIRECTORY_ERROR = "Filename provided is actually a directory!";

    public static final String NULL_DATETIME_MARKER = "NA";
    public static final String FIELD_SEPARATOR = " ";

    private final Thread fileStoreThread;
    private final FileStoreProcess fileStoreProcess;
    private File storageFile;

    public ThreadedFileStorage() throws IOException {
        this(DEFAULT_FILENAME);
    }

    public ThreadedFileStorage(String fileName) throws IOException {
        setStore(fileName);

        fileStoreProcess = new FileStoreProcess(storageFile);

        fileStoreThread = new Thread(fileStoreProcess);
        fileStoreThread.start();
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
        fileStoreProcess.submitSaveList(processCalendar(calendar));
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
        fileStoreProcess.submitClose();

        fileStoreThread.join();
    }

    private List<String> processCalendar(CalendarList calendar) {
        List<String> results = new ArrayList<>();

        results.addAll(processEvents(calendar.getEventList()));
        results.addAll(processTasks(calendar.getTaskList()));

        return results;
    }

    private List<String> processEvents(List<CalendarEntry> eventList) {
        List<String> processedLines = new ArrayList<>();

        processedLines.add(String.valueOf(eventList.size()));

        for (CalendarEntry event : eventList) {

            String processedLine = event.getStart().toString()
                    + FIELD_SEPARATOR
                    + event.getEnd().toString()
                    + FIELD_SEPARATOR
                    + event.getName();

            processedLines.add(processedLine);
        }

        return processedLines;
    }

    private List<String> processTasks(List<CalendarEntry> taskList) {
        List<String> processedLines = new ArrayList<>();

        processedLines.add(String.valueOf(taskList.size()));

        for (CalendarEntry task : taskList) {

            String processedLine = (task.getEnd() == null ? NULL_DATETIME_MARKER : task.getEnd().toString())
                    + FIELD_SEPARATOR
                    + task.getName();

            processedLines.add(processedLine);
        }

        return processedLines;
    }
}
