package lifetracker.logic;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import lifetracker.command.CommandObject;
import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LogicImpl implements Logic {

    private static final String ENTRY_COMMON_FORMAT = "%1$d: %2$s ";
    private static final String DUAL_DATE_TIME_FORMAT = "%1$s - %2$s";

    private static final String TASK_HEADER = "Tasks:";
    private static final String EVENT_HEADER = "Events:";

    private static final String ERROR_SAVE = "Warning: There was an error saving to the save file!";
    private static final String ERROR_INVALID_COMMAND = "Error: Command was not a valid command!";

    private static final FormatStyle DATE_STYLE = FormatStyle.MEDIUM;
    private static final FormatStyle TIME_STYLE = FormatStyle.SHORT;

    private Parser commandParser;
    private Storage calendarStorage;
    private CalendarList calendar;

    public LogicImpl(Parser parser, Storage storage) throws IOException {
    	assert parser != null;
    	assert storage != null;
    	
        commandParser = parser;
        calendarStorage = storage;

        calendar = storage.load(new CalendarListImpl());
    }

    @Override
    public ExecuteResult executeCommand(String commandString) {
    	assert commandString != null;

        CommandObject commandToExecute;
        CalendarList executedState;

        try {
            commandToExecute = commandParser.parse(commandString);
            executedState = commandToExecute.execute(calendar);
        } catch (IllegalArgumentException ex) {
            ExecuteResult errorResult = new CommandLineResult();
            errorResult.setComment(ERROR_INVALID_COMMAND);

            return errorResult;
        }

        try {
            calendarStorage.store(calendar);
        } catch (IOException ex) {
            System.err.println(ERROR_SAVE);
        }

        return processExecutionResults(commandToExecute, executedState);
    }

    private ExecuteResult processExecutionResults(CommandObject commandExecuted, CalendarList executedState) {
    	assert commandExecuted != null;
    	assert executedState != null;

        ExecuteResult runResult = new CommandLineResult();
        runResult.setComment(commandExecuted.getComment());

        if (!executedState.getTaskList().isEmpty()) {
            runResult.addResultLine(TASK_HEADER);
            executedState.getTaskList().forEach(task -> runResult.addResultLine(taskSummary(task)));
            runResult.addResultLine("");
        }

        if (!executedState.getEventList().isEmpty()) {
            runResult.addResultLine(EVENT_HEADER);
            executedState.getEventList().forEach(event -> runResult.addResultLine(eventSummary(event)));
            runResult.addResultLine("");
        }

        return runResult;
    }

    private static String taskSummary(CalendarEntry task) {
    	assert task != null;

        String returnString = String.format(ENTRY_COMMON_FORMAT, task.getId(), task.getName());

        if (task.getEndTime() != null) {
            returnString += task.getEnd().format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE));
        }

        return returnString;
    }

    private static String eventSummary(CalendarEntry event) {
    	assert event != null;

        String returnString = String.format(ENTRY_COMMON_FORMAT, event.getId(), event.getName());

        LocalDateTime start = event.getStart();
        LocalDateTime end = event.getEnd();

        if (start.toLocalDate().equals(end.toLocalDate())) {
            returnString += start.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(DATE_STYLE));
            returnString += " ";
            returnString += String.format(DUAL_DATE_TIME_FORMAT,
                    start.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(TIME_STYLE)),
                    end.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(TIME_STYLE)));
        } else {
            returnString += String.format(DUAL_DATE_TIME_FORMAT,
                    start.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)),
                    end.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));
        }

        return returnString;
    }
}
