package lifetracker.command;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;

public class DeleteCommand implements CommandObject {

    private static final String MESSAGE_DELETED = "%1$d is deleted.";
    private static final String MESSAGE_NOT_FOUND = "%1$d cannot be found!";

    private final int entryID;
    private String comment = MESSAGE_ERROR;

    public DeleteCommand(int entryID) {
        this.entryID = entryID;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        assert calendar != null;

        calendar.delete(entryID);

        comment = String.format(MESSAGE_DELETED, entryID);
        return new CalendarListImpl();
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
