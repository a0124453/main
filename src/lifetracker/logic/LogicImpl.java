package lifetracker.logic;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarProperty;
import lifetracker.command.CommandObject;
import lifetracker.logic.ExecuteResult.CommandType;
import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.EmptyStackException;
import java.util.Properties;
import java.util.Stack;

//@@author A0149467N

public class LogicImpl implements Logic {

    private static final String DEFAULT_SAVE_FILE_NAME = "lifetracker.dat";
    private static final String SAVE_FILE_PROPERTY = "savefile";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String ERROR_SAVE = "Warning: There was an error saving to the save file!";
    private static final String ERROR_INVALID_COMMAND = "Invalid Command: %1$s";
    private static final String ERROR_ERROR_UNDO_STACK_EMPTY = "No command to undo!";
    private static final String ERROR_ERROR_REDO_STACK_EMPTY = "No command to redo!";
    private static final String COMMENT_SAVE = "Calendar is saved at ";

    private Parser commandParser;
    private Storage calendarStorage;
    private CalendarList calendar;
    private Stack<CommandObject> commandStack;
    private Stack<CommandObject> redoStack;
    private Properties property;
    private File propertyFile;

    public LogicImpl(Parser parser, Storage storage) throws IOException {
        assert parser != null;
        assert storage != null;

        commandParser = parser;
        calendarStorage = storage;
        commandStack = new Stack<>();
        redoStack = new Stack<>();

        configureFile();

        StorageAdapter storageAdapter = new StorageAdapter(storage);
        calendar = storageAdapter.load();
    }

    private void configureFile() throws IOException {
        property = new Properties();
        propertyFile = new File(CONFIG_FILE_NAME);

        if (!propertyFile.exists()) {
            propertyFile.createNewFile();
        }

        InputStream fileInputStream = new BufferedInputStream(new FileInputStream(propertyFile));
        property.load(fileInputStream);

        String location = property.getProperty(SAVE_FILE_PROPERTY, DEFAULT_SAVE_FILE_NAME);
        calendarStorage.setStoreAndStart(location);
    }

    @Override
    public ExecuteResult executeCommand(String commandString) {
        assert commandString != null;

        String[] commandContent = commandString.split(" ");
        ExecuteResult runResult = new CommandLineResult();

        if (commandString.equals("exit")) {
            runResult.setType(CommandType.EXIT);
            return runResult;
        } else if (commandContent[0].equals("help")) {
            runResult.setType(CommandType.HELP);
            return runResult;
        } else if (commandContent[0].equals("saveat")) {
            return processSaveatResults(commandString, runResult);
        } else {
            CommandObject commandToExecute;
            CalendarList executedState;

            runResult.setType(CommandType.DISPLAY);

            switch (commandString) {
                case "undo":

                    try {
                        commandToExecute = commandStack.pop();
                        redoStack.push(commandToExecute);
                        executedState = commandToExecute.undo(calendar);
                    } catch (EmptyStackException ex) {
                        ExecuteResult errorResult = new CommandLineResult();
                        errorResult.setComment(String.format(ERROR_INVALID_COMMAND, ERROR_ERROR_UNDO_STACK_EMPTY));
                        errorResult.setType(CommandType.ERROR);
                        return errorResult;
                    }

                    break;
                case "redo":

                    try {
                        commandToExecute = redoStack.pop();
                        commandStack.push(commandToExecute);
                        executedState = commandToExecute.execute(calendar);
                    } catch (EmptyStackException ex) {
                        ExecuteResult errorResult = new CommandLineResult();
                        errorResult.setComment(String.format(ERROR_INVALID_COMMAND, ERROR_ERROR_REDO_STACK_EMPTY));
                        errorResult.setType(CommandType.ERROR);
                        return errorResult;
                    }

                    break;
                default:

                    try {
                        commandToExecute = commandParser.parse(commandString);
                        executedState = commandToExecute.execute(calendar);
                    } catch (IllegalArgumentException ex) {
                        ExecuteResult errorResult = new CommandLineResult();
                        errorResult.setComment(String.format(ERROR_INVALID_COMMAND, ex.getMessage()));
                        errorResult.setType(CommandType.ERROR);
                        return errorResult;
                    }

                    commandStack.push(commandToExecute);
                    redoStack.clear();
                    break;
            }
            store();
            return processExecutionResults(runResult, commandToExecute, executedState);
        }
    }

    private ExecuteResult processSaveatResults(String commandString, ExecuteResult runResult) {
        int position = commandString.indexOf(" ");
        String location = commandString.substring(position + 1);
        try {
            calendarStorage.setStoreAndStart(location);
            property.setProperty(SAVE_FILE_PROPERTY, location);
            OutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(propertyFile));
            property.store(fileOutputStream, "");
        } catch (IOException ex) {
            System.err.println(ERROR_SAVE);
        }

        runResult.setType(CommandType.SAVE);
        runResult.setComment(COMMENT_SAVE + location);
        return runResult;
    }

    private void store() {
        try {
            StorageAdapter storageAdapter = new StorageAdapter(calendarStorage);
            storageAdapter.store(calendar);
        } catch (IOException ex) {
            System.err.println(ERROR_SAVE);
        }
    }

    private ExecuteResult processExecutionResults(ExecuteResult runResult, CommandObject commandExecuted,
            CalendarList executedState) {
        assert commandExecuted != null;
        assert executedState != null;

        runResult.setComment(commandExecuted.getComment());

        if (!executedState.getTaskList().isEmpty()) {
            // TODO load the variable for int limitOccur, LocalDateTime limitDate, boolean isNew
            executedState.getTaskList().forEach(task -> {
                LocalDateTime limitDate = task.getDateTime(CalendarProperty.DATE_LIMIT);

                runResult.addTaskLine(
                        task.getId(),
                        task.getName(),
                        task.getDateTime(CalendarProperty.END),
                        task.isProperty(CalendarProperty.OVER),
                        task.isProperty(CalendarProperty.ACTIVE),
                        task.getPeriod(),
                        task.getIntegerProperty(CalendarProperty.OCCURRENCE_LIMIT),
                        limitDate == null ? null : limitDate.toLocalDate(),
                        false);
            });
        }

        if (!executedState.getEventList().isEmpty()) {
            // TODO load the variable for int limitOccur, LocalDateTime limitDate, boolean isNew
            executedState.getEventList().forEach(event -> {

                LocalDateTime limitDate = event.getDateTime(CalendarProperty.DATE_LIMIT);
                runResult.addEventLine(
                        event.getId(),
                        event.getName(),
                        event.getDateTime(CalendarProperty.START),
                        event.getDateTime(CalendarProperty.END),
                        event.isProperty(CalendarProperty.OVER),
                        event.isProperty(CalendarProperty.ACTIVE),
                        event.getPeriod(),
                        event.getIntegerProperty(CalendarProperty.OCCURRENCE_LIMIT),
                        limitDate == null ? null : limitDate.toLocalDate(),
                        false);
            });
        }

        return runResult;
    }
}
