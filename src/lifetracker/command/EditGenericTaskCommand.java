package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

//@@author A0091173J
public class EditGenericTaskCommand extends CommandObject {

    protected static final String MESSAGE_EDITED = "%1$s was edited.";
    protected static final String MESSAGE_UNEDITED = "Changes to %1$s were reverted.";

    final int id;
    final String name;
    final boolean isForcedConvert;

    CalendarEntry oldEntry;

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
