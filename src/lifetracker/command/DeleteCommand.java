package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

//@@author A0091173J
public class DeleteCommand extends CommandObject {

    private static final String MESSAGE_DELETED = "%1$d is deleted.";
    private static final String MESSAGE_NOT_FOUND = "%1$d cannot be found!";
    private static final String MESSAGE_UNDO = "\"%1$s\" re-added.";

    private final int entryID;
    private CalendarEntry entryDeleted;

    public DeleteCommand(int entryID) {
        this.entryID = entryID;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        assert calendar != null;

        entryDeleted = calendar.delete(entryID);

        if (entryDeleted == null) {
            throw new IllegalArgumentException(String.format(MESSAGE_NOT_FOUND, entryID));
        }

        setComment(String.format(MESSAGE_DELETED, entryID));

        return super.execute(calendar);
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        calendar.add(entryDeleted);

        addHighlightEntry(entryDeleted.getId());

        setComment(String.format(MESSAGE_UNDO, entryDeleted.getName()));

        return super.undo(calendar);
    }
}
