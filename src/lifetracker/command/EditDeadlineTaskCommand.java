package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarProperty;

import java.time.LocalDateTime;

//@@author A0091173J

/**
 * A {@code CommandObject} that edits the name and the deadline of an entry in the calendar.
 *
 * This command can also specify that the entry be converted forcefully into a DeadlineTask.
 */
public class EditDeadlineTaskCommand extends EditGenericTaskCommand {

    final protected LocalDateTime endDateTime;

    public EditDeadlineTaskCommand(int id, String name, LocalDateTime endDateTime, boolean isForcedConvert) {
        super(id, name, isForcedConvert);
        this.endDateTime = endDateTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        if (isForcedConvert) {
            oldEntry = calendar.updateToDeadline(id, name, endDateTime, true);
        } else {
            CalendarEntry entryToEdit = calendar.get(id);
            if (entryToEdit.isProperty(CalendarProperty.RECURRING)) {
                oldEntry = calendar.updateToRecurringTask(id, name, endDateTime, null, true, false);
            } else {
                oldEntry = calendar.updateToDeadline(id, name, endDateTime, true);
            }
        }

        addHighlightEntry(id);

        setComment(String.format(MESSAGE_EDITED, id));
        setExecuted(true);

        return calendar;
    }
}
