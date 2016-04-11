package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarProperty;

import java.time.LocalDateTime;

//@@author A0091173J

/**
 * A {@code CommandObject} that edits the name, start and end date/time of an entry in the calendar.
 * <p>
 * This class can also be configured such that the entry is forcefully converted into a event entry.
 */
public class EditEventCommand extends EditDeadlineTaskCommand {

    protected final LocalDateTime startDateTime;

    public EditEventCommand(int id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            boolean isForcedConvert) {
        super(id, name, endDateTime, isForcedConvert);
        this.startDateTime = startDateTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        if (isForcedConvert) {
            oldEntry = calendar.updateToEvent(id, name, startDateTime, endDateTime, true);
        } else {
            CalendarEntry entryToEdit = calendar.get(id);

            if (entryToEdit.isProperty(CalendarProperty.RECURRING)) {
                oldEntry = calendar.updateToRecurringEvent(id, name, startDateTime, endDateTime, null, true);
            } else {
                oldEntry = calendar.updateToEvent(id, name, startDateTime, endDateTime, true);
            }
        }

        addHighlightEntry(id);

        setExecuted(true);
        setComment(String.format(MESSAGE_EDITED, id));

        return calendar;
    }
}
