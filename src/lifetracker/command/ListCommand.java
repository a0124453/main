package lifetracker.command;

import lifetracker.calendar.CalendarList;

public class ListCommand implements CommandObject {

    private static final String MESSAGE_LIST = "Listing all Events and Tasks";

    private String comment = MESSAGE_ERROR;

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        comment = MESSAGE_LIST;

        return calendar;
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getComment() {
        return this.comment;
    }
}
