package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

//@@author A0091173J
public class MarkCommand extends CommandObject {

    private static final String MESSAGE_MARKED_DONE = "\"%1$d\" marked as done/archived.";
    private static final String MESSAGE_MARKED_UNDONE = "\"%1$d\" marked as undone/archived.";

    private final int entryId;

    public MarkCommand(int entryId) {
        this.entryId = entryId;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        CalendarEntry markedEntry = calendar.mark(entryId);
        assignComment(markedEntry);

        return super.execute(calendar);
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        CalendarEntry markedEntry = calendar.mark(entryId);
        assignComment(markedEntry);

        return super.undo(calendar);
    }

    private void assignComment(CalendarEntry markedEntry) {
        if (markedEntry.getType() == CalendarEntry.EntryType.DEADLINE && markedEntry.isRecurring()) {
            setComment(String.format(MESSAGE_MARKED_DONE, entryId));
        } else if (markedEntry.isActive()) {
            setComment(String.format(MESSAGE_MARKED_UNDONE, entryId));
        } else {
            setComment(String.format(MESSAGE_MARKED_DONE, entryId));
        }
    }
}
