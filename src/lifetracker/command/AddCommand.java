package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;

public class AddCommand implements CommandObject {

    private static final String MESSAGE_ADDED = "\"%1$s\" is added.";

    private final String name;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    private String comment = MESSAGE_ERROR;

    public AddCommand(String name) {
        this.name = name;
        this.startDateTime = null;
        this.endDateTime = null;
    }

    public AddCommand(String name, LocalDateTime dueDateTime) {
        this.name = name;
        this.startDateTime = null;
        this.endDateTime = dueDateTime;
    }

    public AddCommand(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        if (endDateTime == null) {
            calendar.add(name);
        } else if (startDateTime == null) {
            calendar.add(name, endDateTime);
        } else {
            calendar.add(name, startDateTime, endDateTime);
        }

        comment = String.format(MESSAGE_ADDED, name);

        return null;
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getComment() {
        return this.comment;
    }
}
