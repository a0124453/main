package lifetracker.command;

import lifetracker.calendar.CalendarList;

public class ListCommand implements CommandObject {

    private static final String MESSAGE_LIST = "Listing all Events and Tasks";

    private String comment = MESSAGE_ERROR;

    private boolean executed = false;

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        comment = MESSAGE_LIST;

        executed = true;

        return calendar;
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        assert executed;

        executed = false;

        return calendar;
    }

    @Override
    public String getComment() {
        return this.comment;
    }
}
