package lifetracker.logic;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

import java.io.IOException;

public class LogicImpl implements Logic {

    private static Parser commandParser;
    private static Storage calendarStorage;
    private static CalendarList calendar;

    public LogicImpl(Parser parser, Storage storage) {
        commandParser = parser;
        calendarStorage = storage;

        calendar = new CalendarListImpl();
    }

    @Override
    public ExecuteResult executeCommand(String commandString) {
        commandParser.parse(commandString);

        try {
            calendarStorage.store(calendar);
        } catch (IOException ex){
            //TODO add exception handling
        }

        return null;
    }
}
