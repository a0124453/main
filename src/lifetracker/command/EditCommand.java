package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;
import java.util.List;

//@@author A0091173J
public class EditCommand extends CommandObject {

    private static final String MESSAGE_EDITED = "\"%1$d\" is edited.";
    private static final String MESSAGE_NOT_FOUND = "%1$d cannot be found!";

    private final int entryID;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private String oldName;
    private LocalDateTime oldStartTime;
    private LocalDateTime oldEndTime;

    public EditCommand(int entryID, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.entryID = entryID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        CalendarEntry entryToUpdate = findEntry(calendar);

        if (entryToUpdate == null) {
            throw new IllegalArgumentException(String.format(MESSAGE_NOT_FOUND, entryID));
        }

        saveOldValues(entryToUpdate);

        calendar.update(entryID, name, startTime, endTime);

        setComment(String.format(MESSAGE_EDITED, entryID));

        return super.execute(calendar);
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        calendar.update(entryID, oldName, oldStartTime, oldEndTime);

        //TODO set comment

        return super.undo(calendar);
    }

    private void saveOldValues(CalendarEntry entryToUpdate) {
        if (name != null) {
            oldName = entryToUpdate.getName();
        }

        if (startTime != null) {
            oldStartTime = entryToUpdate.getStart();
        }

        if (endTime != null) {
            oldEndTime = entryToUpdate.getEnd();
        }
    }

    private CalendarEntry findEntry(CalendarList calendar) {
        List<CalendarEntry> entriesList = calendar.getEventList();
        entriesList.addAll(calendar.getTaskList());

        for (CalendarEntry entry : entriesList) {
            if (entry.getId() == entryID) {
                return entry;
            }
        }

        return null;
    }
}
