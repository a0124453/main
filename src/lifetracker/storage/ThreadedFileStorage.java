package lifetracker.storage;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThreadedFileStorage implements Storage {

    private static final String DEFAULT_FILENAME = "lifetracker.dat";

    private static final String ERROR_FILE_IS_DIRECTORY = "Filename provided is actually a directory!";
    private static final String ERROR_INTERRUPTED_CLOSE = "Thread was interrupted while finishing up requests!";

    private static final String NULL_DATETIME_MARKER = "NA";
    private static final String FIELD_SEPARATOR = " ";

    private Thread fileStoreThread;
    private FileStoreProcess fileStoreProcess;

    public ThreadedFileStorage() throws IOException {
        this(DEFAULT_FILENAME);
    }

    public ThreadedFileStorage(String fileName) throws IOException {
        startThread(prepareFile(fileName));
    }

    @Override
    public void setStore(String destination) throws IOException {
        try {
            stopThread();
        } catch (InterruptedException e) {
            System.err.println(ERROR_INTERRUPTED_CLOSE);
        }

        File newStorageFile = prepareFile(destination);

        startThread(newStorageFile);
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
