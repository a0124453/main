package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarProperty;

import java.time.LocalDateTime;
import java.time.Period;

//@@author A0091173J-unused
//Was replaced with numerous subclasses
public class EditCommand extends CommandObject {

    private static final String MESSAGE_EDITED = "\"%1$d\" is edited.";
    private static final String MESSAGE_UNDO = "Changes to \"%1$d\" have been undone.";
    private static final String MESSAGE_NOT_FOUND = "%1$d cannot be found!";

    private final int entryID;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Period recurringTime;

    private CalendarEntry oldEntry;

    public EditCommand(int entryID, String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringTime) {
        this.entryID = entryID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurringTime = recurringTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        oldEntry = calendar.update(entryID, name, startTime, endTime, recurringTime);

        if (oldEntry == null) {
            throw new IllegalArgumentException(String.format(MESSAGE_NOT_FOUND, entryID));
        }

        setComment(String.format(MESSAGE_EDITED, entryID));

        return super.execute(calendar);
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        calendar.update(entryID, oldEntry.getName(), oldEntry.getDateTime(CalendarProperty.START), oldEntry.getDateTime(CalendarProperty.END), oldEntry.getPeriod());

        setComment(String.format(MESSAGE_UNDO, entryID));

        return super.undo(calendar);
    }
}
