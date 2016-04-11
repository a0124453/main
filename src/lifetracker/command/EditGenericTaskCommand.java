package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

//@@author A0091173J

/**
 * A {@code CommandObject} edits the name of an entry in the calendar.
 * <p>
 * This class can also be configured such that the entry is forcefully converted into a generic entry with only a name.
 */
public class EditGenericTaskCommand extends CommandObject {

    protected static final String MESSAGE_EDITED = "%1$s was edited.";
    protected static final String MESSAGE_UNEDITED = "Changes to %1$s were reverted.";

    protected final int id;
    protected final String name;
    protected final boolean isForcedConvert;

    protected CalendarEntry oldEntry;

    public EditGenericTaskCommand(int id, String name, boolean isForcedConvert) {
        this.id = id;
        this.name = name;
        this.isForcedConvert = isForcedConvert;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        oldEntry = calendar.updateToGeneric(id, name, isForcedConvert);
        addHighlightEntry(id);

        setComment(String.format(MESSAGE_EDITED, id));

        return super.execute(calendar);
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        calendar.update(oldEntry);

        setComment(String.format(MESSAGE_UNEDITED, id));

        return super.undo(calendar);
    }
}
