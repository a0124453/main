package lifetracker.command;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;

import java.time.LocalDateTime;

public class EditCommand implements CommandObject {

    private static final String MESSAGE_EDITED = "\"%1$d\" is edited.";

    private final int entryID;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private String comment = MESSAGE_ERROR;

    public EditCommand(int entryID, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.entryID = entryID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        assert calendar != null;

        calendar.update(entryID, name, startTime, endTime);

        comment = String.format(MESSAGE_EDITED, entryID);

        return new CalendarListImpl();
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        return null;
    }

    @Override
    public String getComment() {
        return comment;
    }
}
