package lifetracker.storage;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ThreadedFileStorage implements Storage {

    private static final String DEFAULT_FILENAME = "lifetracker.dat";

    private static final String ERROR_FILE_IS_DIRECTORY = "Filename provided is actually a directory!";
    private static final String ERROR_INTERRUPTED_CLOSE = "Thread was interrupted while finishing up requests!";

    private static final String NULL_DATETIME_MARKER = "NA";
    private static final String FIELD_SEPARATOR = " ";

    private File storageFile;
    private Thread fileStoreThread;
    private FileStoreProcess fileStoreProcess;

    public ThreadedFileStorage() throws IOException {
        this(DEFAULT_FILENAME);
    }

    public ThreadedFileStorage(String fileName) throws IOException {
        storageFile = prepareFile(fileName);
        startThread(storageFile);
    }

    @Override
    public void setStore(String destination) throws IOException {
        try {
            stopThread();
        } catch (InterruptedException e) {
            System.err.println(ERROR_INTERRUPTED_CLOSE);
        }

        storageFile = prepareFile(destination);

        startThread(storageFile);
    }

    @Override
    public void store(String storeJsonString) throws IOException {
        assert storeJsonString != null;

        //TODO: Change everything to store strings
        fileStoreProcess.submitSave(processCalendar(null));
    }

    @Override
    public String load() throws IOException {
        return new String(Files.readAllBytes(storageFile.toPath()), StandardCharsets.UTF_8);
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

            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(storageFile))) {
                fileWriter.write("0");
                fileWriter.newLine();
                fileWriter.write("0");
                fileWriter.newLine();
            }

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

            String processedLine = event.getStart().toString() + FIELD_SEPARATOR + event.getEnd().toString()
                    + FIELD_SEPARATOR + event.getName();

            processedLines.add(processedLine);
        }

        return processedLines;
    }

    private List<String> processTasks(List<CalendarEntry> taskList) {
        List<String> processedLines = new ArrayList<>();

        processedLines.add(String.valueOf(taskList.size()));

        for (CalendarEntry task : taskList) {

            String processedLine = (task.getEnd() == null ? NULL_DATETIME_MARKER : task.getEnd().toString())
                    + FIELD_SEPARATOR + task.getName();

            processedLines.add(processedLine);
        }

        return processedLines;
    }

    private void addTask(CalendarList calendar, String taskLine) {
        StringTokenizer tokenizer = new StringTokenizer(taskLine);

        String dueDateString = tokenizer.nextToken();

        StringBuilder name = new StringBuilder();

        while (tokenizer.hasMoreElements()) {
            name.append(tokenizer.nextToken());
            name.append(" ");
        }

        if (dueDateString.equals(NULL_DATETIME_MARKER)) {
            calendar.add(name.toString().trim());
        } else {
            calendar.add(name.toString().trim(), LocalDateTime.parse(dueDateString));
        }

    }

    private void addEvent(CalendarList calendar, String eventLine) {
        StringTokenizer tokenizer = new StringTokenizer(eventLine);

        LocalDateTime start = LocalDateTime.parse(tokenizer.nextToken());
        LocalDateTime end = LocalDateTime.parse(tokenizer.nextToken());

        StringBuilder name = new StringBuilder();

        while (tokenizer.hasMoreElements()) {
            name.append(tokenizer.nextToken());
            name.append(" ");
        }

        calendar.add(name.toString().trim(), start, end);
    }
}
