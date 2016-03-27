package lifetracker.command;

import lifetracker.calendar.CalendarList;

//@@author A0091173J
public class ListCommand extends CommandObject {

    private static final String MESSAGE_LIST = "Listing all Events and Tasks";

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        setComment(MESSAGE_LIST);

        return calendar;
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        return calendar;
    }
}
