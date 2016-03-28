package lifetracker.logic;

import lifetracker.calendar.CalendarList;
import lifetracker.command.CommandObject;
import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;

public class LogicImpl implements Logic {

    private static final String ERROR_SAVE = "Warning: There was an error saving to the save file!";
    private static final String ERROR_INVALID_COMMAND = "Error: Command was not a valid command!";
    private static final String COMMENT_SAVE = "Calendar is saved at ";

    private Parser commandParser;
    private Storage calendarStorage;
    private CalendarList calendar;
    private Stack<CommandObject> commandStack;

    public LogicImpl(Parser parser, Storage storage) throws IOException {
        assert parser != null;
        assert storage != null;

        commandParser = parser;
        calendarStorage = storage;

        StorageAdapter storageAdapter = new StorageAdapterImpl();
        calendar = storageAdapter.load(storage);

        commandStack = new Stack<CommandObject>();
    }

    @Override
    public ExecuteResult executeCommand(String commandString) {
        assert commandString != null;
        
        String[] commandContent = commandString.split(" ");

        ExecuteResult runResult = new CommandLineResult();
        runResult.setType(commandString);

        if (commandString.equals("exit")) {
            return runResult;

        }
        
        else if (commandContent[0].equals("saveat")) {
            int position = commandString.indexOf(" ");
            String location = commandString.substring(position + 1);
            
            try {
                calendarStorage.setStore(location);
            } catch (IOException ex) {
                System.err.println(ERROR_SAVE);
            }
            
            runResult.setComment(COMMENT_SAVE + location);
            return runResult;
        }
        
        else {
            CommandObject commandToExecute;
            CalendarList executedState;

            if (commandString.equals("undo")) {
                
                try {
                    commandToExecute = commandStack.pop();
                    executedState = commandToExecute.undo(calendar);
                } catch (EmptyStackException ex) {
                    ExecuteResult errorResult = new CommandLineResult();
                    errorResult.setComment(ERROR_INVALID_COMMAND);
                    errorResult.setType("ERROR");
                    return errorResult;
                }

            } else {

                try {
                    commandToExecute = commandParser.parse(commandString);
                    executedState = commandToExecute.execute(calendar);
                } catch (IllegalArgumentException ex) {
                    ExecuteResult errorResult = new CommandLineResult();
                    errorResult.setComment(ERROR_INVALID_COMMAND);
                    errorResult.setType("ERROR");
                    return errorResult;
                }

                commandStack.push(commandToExecute);
            }

            try {
                StorageAdapter storageAdapter = new StorageAdapterImpl();
                storageAdapter.store(calendar, calendarStorage);
            } catch (IOException ex) {
                System.err.println(ERROR_SAVE);
            }

            return processExecutionResults(runResult, commandToExecute, executedState);
        }
    }

    private ExecuteResult processExecutionResults(ExecuteResult runResult, CommandObject commandExecuted,
            CalendarList executedState) {
        assert commandExecuted != null;
        assert executedState != null;

        runResult.setComment(commandExecuted.getComment());

        if (!executedState.getTaskList().isEmpty()) {
            executedState.getTaskList()
                    .forEach(task -> runResult.addTaskLine(task.getId(), task.getName(), task.isActive(), task.getEnd(), task.getPeriod()));
        }

        if (!executedState.getEventList().isEmpty()) {
            executedState.getEventList().forEach(
                    event -> runResult.addEventLine(event.getId(), event.getName(), event.isActive(), event.getStart(), event.getEnd(), event.getPeriod()));
        }

        return runResult;
    }
}
