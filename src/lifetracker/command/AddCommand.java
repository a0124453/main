package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;

public class AddCommand implements CommandObject {

    private static final String MESSAGE_ADDED = "\"%1$s\" is added.";
    private static final String MESSAGE_ERROR = "Error: Command was not executed.";

    private String comment = MESSAGE_ERROR;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public AddCommand(String name) {
        this.name = name;
    }

    public AddCommand(String name, LocalDateTime dueDateTime) {
        this.name = name;
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
