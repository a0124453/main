package lifetracker.logic;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import lifetracker.command.CommandObject;
import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

import java.io.IOException;
import java.util.Stack;

public class LogicImpl implements Logic {

    private static final String ERROR_SAVE = "Warning: There was an error saving to the save file!";
    private static final String ERROR_INVALID_COMMAND = "Error: Command was not a valid command!";

    private Parser commandParser;
    private Storage calendarStorage;
    private CalendarList calendar;
    private Stack<String> commandStack;

    public LogicImpl(Parser parser, Storage storage) throws IOException {
        assert parser != null;
        assert storage != null;

        commandParser = parser;
        calendarStorage = storage;

        calendar = storage.load(new CalendarListImpl());

        commandStack = new Stack<String>();
    }

    @Override
    public ExecuteResult executeCommand(String commandString) {
        assert commandString != null;

        ExecuteResult runResult = new CommandLineResult();
        runResult.setType(commandString);

        if (commandString.equals("exit")) {
            return runResult;

        } else {
            CommandObject commandToExecute;
            CalendarList executedState;

            if (commandString.equals("undo")) {

                commandStack.pop();

                commandToExecute = commandParser.parse(commandString);
                executedState = commandToExecute.undo(calendar);

            } else {

                try {
                    commandToExecute = commandParser.parse(commandString);
                    executedState = commandToExecute.execute(calendar);
                } catch (IllegalArgumentException ex) {
                    ExecuteResult errorResult = new CommandLineResult();
                    errorResult.setComment(ERROR_INVALID_COMMAND);
                    return errorResult;
                }

                commandStack.push(commandString);
            }

            try {
                calendarStorage.store(calendar);
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
                    .forEach(task -> runResult.addTaskLine(task.getId(), task.getName(), task.getEnd()));
        }

        if (!executedState.getEventList().isEmpty()) {
            executedState.getEventList().forEach(
                    event -> runResult.addEventLine(event.getId(), event.getName(), event.getStart(), event.getEnd()));
        }

        return runResult;
    }
}
