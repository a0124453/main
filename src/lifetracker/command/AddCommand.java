package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;

//@@author A0091173J
public class AddCommand extends CommandObject {

    private static final String MESSAGE_UNDO = "%1$d: \"%2$s\" removed.";
    protected static final String MESSAGE_ADDED = "\"%1$s\" is added.";

    private final String name;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    private int addedEntryID;

    public AddCommand(String name) {
        assert name != null;

        this.name = name;
        this.startDateTime = null;
        this.endDateTime = null;
    }

    public AddCommand(String name, LocalDateTime dueDateTime) {
        assert name != null;
        assert dueDateTime != null;

        this.name = name;
        this.startDateTime = null;
        this.endDateTime = dueDateTime;
    }

    public AddCommand(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        assert name != null;
        assert startDateTime != null;
        assert endDateTime != null;

        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        if (endDateTime == null) {
            addedEntryID = calendar.add(name);
        } else if (startDateTime == null) {
            addedEntryID = calendar.add(name, endDateTime);
        } else {
            addedEntryID = calendar.add(name, startDateTime, endDateTime);
        }

        addHighlightEntry(addedEntryID);

        setComment(String.format(MESSAGE_ADDED, name));

        return super.execute(calendar);
    }

    @Override
    public CalendarList undo(CalendarList calendar) {

        assert calendar != null;

        calendar.delete(addedEntryID);

        setComment(String.format(MESSAGE_UNDO, addedEntryID, name));

        return super.undo(calendar);
    }

    protected String getName() {
        return name;
    }

    protected LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    protected LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    protected void setAddedEntryID(int addedEntryID) {
        this.addedEntryID = addedEntryID;
    }
}
