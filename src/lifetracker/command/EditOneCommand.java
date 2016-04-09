package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.visitor.OldNewEntryPair;

import java.time.LocalDateTime;

//@@author A0091173J
public class EditOneCommand extends CommandObject {

    private static final String ERROR_NON_RECURRING = "Cannot edit single occurrence of non-recurring task!";
    private static final String MESSAGE_EDITED = "Single occurrence of \"%1$d\" edited to \"%2$d\".";
    private static final String MESSAGE_UNDO = "Single occurrence changes undone.";

    private final int id;
    private final String name;
    private final LocalDateTime start;
    private final LocalDateTime end;

    private CalendarEntry editedEntry;
    private int newEntryId;

    public EditOneCommand(int id, String name) {
        this.id = id;
        this.name = name;
        this.start = null;
        this.end = null;
    }

    public EditOneCommand(int id, String name, LocalDateTime start) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = null;
    }

    public EditOneCommand(int id, String name, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        if (end == null) {
            processEditName(calendar);
        } else if (start == null) {
            processEditDeadLine(calendar);
        } else {
            processEditEvent(calendar);
        }

        setComment(String.format(MESSAGE_EDITED, id, editedEntry.getId()));

        return super.execute(calendar);
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        super.undo(calendar);

        calendar.delete(newEntryId);
        calendar.update(editedEntry);

        setComment(MESSAGE_UNDO);

        return calendar;
    }

    private void processEditName(CalendarList calendar){
        markEntry(calendar);

        calendar.updateToGeneric(newEntryId, name, false);
    }

    private void processEditDeadLine(CalendarList calendar){
        markEntry(calendar);

        calendar.updateToDeadline(newEntryId, name, end, true);
    }

    private void processEditEvent(CalendarList calendar){
        markEntry(calendar);

        calendar.updateToEvent(newEntryId, name, start, end, true);
    }

    private void markEntry(CalendarList calendar){
        OldNewEntryPair pair = calendar.mark(id);

        assert pair.oldEntry != null;

        editedEntry = pair.oldEntry;

        if(pair.newEntry == null){
            calendar.mark(id);
            throw new IllegalArgumentException(ERROR_NON_RECURRING);
        }

        newEntryId = pair.newEntry.getId();
        calendar.mark(newEntryId);


    }

}
