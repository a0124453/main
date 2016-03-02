package lifetracker.logic;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

import java.io.IOException;

public class LogicImpl implements Logic {

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
        String parsedComment = commandParser.parse(commandString);

        try {
            calendarStorage.store(calendar);
            calendarStorage.store(calendar);
        } catch (IOException ex) {
            //TODO add exception handling
        }

        ExecuteResult runResult = new CommandLineResult();
        runResult.setComment(parsedComment);

        return runResult;
    }
}
