package lifetracker.logic;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import lifetracker.command.CommandObject;
import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LogicImpl implements Logic {

    private static final String ENTRY_COMMON_FORMAT = "%1$d: %2$s ";
    private static final String DUAL_DATE_FORMAT = "%1$s - %2$s";

    private static final FormatStyle DATE_STYLE = FormatStyle.MEDIUM;
    private static final FormatStyle TIME_STYLE = FormatStyle.SHORT;

    private Parser commandParser;
    private Storage calendarStorage;
    private CalendarList calendar;

    public LogicImpl(Parser parser, Storage storage) {
        commandParser = parser;
        calendarStorage = storage;

        calendar = new CalendarListImpl();
    }

    @Override
    public ExecuteResult executeCommand(String commandString) {
        CommandObject commandToExecute = commandParser.parse(commandString);

        CalendarList executedState = commandToExecute.execute(calendar);

        try {
            calendarStorage.store(calendar);
        } catch (IOException ex) {
            //TODO add exception handling
        }

        ExecuteResult runResult = new CommandLineResult();
        runResult.setComment(commandToExecute.getComment());

        executedState.getTaskList().forEach(task -> runResult.addResultLine(taskSummary(task)));

        return runResult;
    }

    private static String taskSummary(CalendarEntry task) {

        String returnString = String.format(ENTRY_COMMON_FORMAT, task.getId(), task.getName());

        if (task.getEndTime() != null) {
            returnString += task.getEndTime().format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE));
        }

        return returnString;
    }
}
