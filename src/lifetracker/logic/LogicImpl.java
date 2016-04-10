package lifetracker.logic;

import lifetracker.calendar.CalendarEntry;
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
import java.util.Set;
import java.util.Stack;

//@@author A0149467N

public class LogicImpl implements Logic {

    //configure file constant
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String SAVE_FILE_PROPERTY = "savefile";
    private static final String DEFAULT_SAVE_FILE_NAME = "lifetracker.dat";

    //Error message
    private static final String ERROR_INVALID_COMMAND = "Invalid Command: %1$s";
    private static final String ERROR_SAVE = "Warning: There was an error saving to the save file!";
    private static final String ERROR_ERROR_UNDO_STACK_EMPTY = "No command to undo!";
    private static final String ERROR_ERROR_REDO_STACK_EMPTY = "No command to redo!";

    //save comment
    private static final String COMMENT_SAVE = "Calendar is saved at ";

    private Parser commandParser;
    private Storage calendarStorage;
    private CalendarList calendar;

    private Stack<CommandObject> commandStack;
    private Stack<CommandObject> redoStack;

    //This is used to load and store the configuration files
    private Properties property;
    //This is the configuration file
    private File propertyFile;

    /**
    * Constructor of Logic. Initialize parser, storage and two stacks,
    *   configure the configuration file and load the calendar from storage
    * 
    * @param parser
    * @param storage
    * @throws IOException
    *       If an I/O error occurs when configuring the file or load the calendar
    */
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

    /**
    * Configure the configuration file
    * 
    * @throws IOException
    *       If an I/O error occurs when createNewFile or load the fileInputStream or store the calendar
    */
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

    /**
    * Decide what kind of command it is and set the CommandType for them,
    *   pass the command string to respective handlers
    * 
    * @param commandString
    * @return The result after execution
    */
    @Override
    public ExecuteResult executeCommand(String commandString) {
        assert commandString != null;

        String[] commandContent = commandString.split(" ");
        ExecuteResult runResult = new ExecuteResultImpl();

        if (commandContent[0].equals("saveat")) {
            runResult.setType(CommandType.SAVE);
            return processSaveat(commandString, runResult);
        }

        switch (commandString) {
            case "exit":
                runResult.setType(CommandType.EXIT);
                return runResult;
            case "help":
                runResult.setType(CommandType.HELP);
                return runResult;
            default:
                runResult.setType(CommandType.DISPLAY);
                switch (commandString) {
                    case "undo":
                        return undo(runResult);
                    case "redo":
                        return redo(runResult);
                    default:
                        return otherCommand(commandString, runResult);
                }
        }
    }

    /**
    * Save the calendar at the specified location according to the user input and set the comment.
    * 
    * @param commandString
    * @param runResult
    *       The ExecutedResult object to be modified so that it can be returned to UI
    * @return The ExecuteResult object to be returned to UI
    */
    private ExecuteResult processSaveat(String commandString, ExecuteResult runResult) {
        int position = commandString.indexOf(" ");
        String location = commandString.substring(position + 1);

        saveat(location);

        runResult.setComment(COMMENT_SAVE + location);
        return runResult;
    }

    /**
    * Save the calendar at the specified location and change the configuration file
    * 
    * @param location
    */
    private void saveat(String location) {
        try {
            calendarStorage.setStoreAndStart(location);
            property.setProperty(SAVE_FILE_PROPERTY, location);
            OutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(propertyFile));
            property.store(fileOutputStream, "");
        } catch (IOException ex) {
            System.err.println(ERROR_SAVE);
        }
    }

    /**
    * Pop the command stack, push the command to the redo stack and undo the command.
    *   Store the changes and pass the result from parser to be processed
    *   If the command stack is empty, return an error result.
    * 
    * @param runResult
    *       The ExecutedResult object to be modified so that it can be returned to UI
    * @return The ExecuteResult object to be returned to UI
    */
    private ExecuteResult undo(ExecuteResult runResult) {
        CommandObject commandToExecute;
        CalendarList executedState;

        try {
            commandToExecute = commandStack.pop();
            redoStack.push(commandToExecute);
            executedState = commandToExecute.undo(calendar);
        } catch (EmptyStackException ex) {
            ExecuteResult errorResult = new ExecuteResultImpl();
            errorResult.setComment(String.format(ERROR_INVALID_COMMAND, ERROR_ERROR_UNDO_STACK_EMPTY));
            errorResult.setType(CommandType.ERROR);
            return errorResult;
        }

        store();
        return processExecutionResults(runResult, commandToExecute, executedState);
    }

    /**
    * Pop the redo stack, push the command to the command stack and redo the command.
    *   Store the changes and pass the result from parser to be processed
    *   If the redo stack is empty, return an error result.
    * 
    * @param runResult
    *       The ExecutedResult object to be modified so that it can be returned to UI
    * @return The ExecuteResult object to be returned to UI
    */
    private ExecuteResult redo(ExecuteResult runResult) {
        CommandObject commandToExecute;
        CalendarList executedState;

        try {
            commandToExecute = redoStack.pop();
            commandStack.push(commandToExecute);
            executedState = commandToExecute.execute(calendar);
        } catch (EmptyStackException ex) {
            ExecuteResult errorResult = new ExecuteResultImpl();
            errorResult.setComment(String.format(ERROR_INVALID_COMMAND, ERROR_ERROR_REDO_STACK_EMPTY));
            errorResult.setType(CommandType.ERROR);
            return errorResult;
        }

        store();
        return processExecutionResults(runResult, commandToExecute, executedState);
    }

    /**
    * Pass the commandString to parser and execute the command.
    *   Push the command to the command stack and clear the redo stack.
    *   Store the changes and pass the result from parser to be processed.
    *   If the command is invalid, return an error result.
    * 
    * @param commandString
    * @param runResult
    *       The ExecutedResult object to be modified so that it can be returned to UI
    * @return The ExecuteResult object to be returned to UI
    */
    private ExecuteResult otherCommand(String commandString, ExecuteResult runResult) {
        CommandObject commandToExecute;
        CalendarList executedState;

        try {
            commandToExecute = commandParser.parse(commandString);
            executedState = commandToExecute.execute(calendar);
        } catch (IllegalArgumentException ex) {
            ExecuteResult errorResult = new ExecuteResultImpl();
            errorResult.setComment(String.format(ERROR_INVALID_COMMAND, ex.getMessage()));
            errorResult.setType(CommandType.ERROR);
            return errorResult;
        }

        commandStack.push(commandToExecute);
        redoStack.clear();

        store();
        return processExecutionResults(runResult, commandToExecute, executedState);
    }

    /**
    * Store the calendar to the storage
    */
    private void store() {
        try {
            StorageAdapter storageAdapter = new StorageAdapter(calendarStorage);
            storageAdapter.store(calendar);
        } catch (IOException ex) {
            System.err.println(ERROR_SAVE);
        }
    }

    /**
    * Set the comment and for each task/event in the calendar, add task/event to the runResult
    * 
    * @param runResult
    *       The ExecutedResult object to be modified so that it can be returned to UI
    * @param commandExecuted
    *       The parser parsed the command string to a command object: commandExecuted
    * @param executedState
    *       The CalendarList to be manipulated so that it can be displayed in UI
    * @return The ExecuteResult object to be returned to UI
    */
    private ExecuteResult processExecutionResults(ExecuteResult runResult, CommandObject commandExecuted,
            CalendarList executedState) {
        assert commandExecuted != null;
        assert executedState != null;

        Set<Integer> entriesToHighlight = commandExecuted.getHighlightEntries();

        runResult.setComment(commandExecuted.getComment());

        if (!executedState.getTaskList().isEmpty()) {
            executedState.getTaskList()
                    .forEach(task -> addTask(runResult, task, entriesToHighlight.contains(task.getId())));
        }

        if (!executedState.getEventList().isEmpty()) {
            executedState.getEventList()
                    .forEach(event -> addEvent(runResult, event, entriesToHighlight.contains(event.getId())));
        }

        return runResult;
    }

    /**
    * Add the task to the runResult
    * 
    * @param runResult
    *       The ExecutedResult object to be modified so that it can be returned to UI
    * @param task
    *       The task to be added to runResult
    * @param isHighlighted
    *       Whether the task will be highlighted in UI
    */
    private void addTask(ExecuteResult runResult, CalendarEntry task, boolean isHighlighted) {
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
                isHighlighted);
    }

    /**
    * Add the event to the runResult
    * 
    * @param runResult
    *       The ExecutedResult object to be modified so that it can be returned to UI
    * @param event
    *       The event to be added to runResult
    * @param isHighlighted
    *       Whether the event will be highlighted in UI
    */
    private void addEvent(ExecuteResult runResult, CalendarEntry event, boolean isHighlighted) {
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
                isHighlighted);
    }
}
