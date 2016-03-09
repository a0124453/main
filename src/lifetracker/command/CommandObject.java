package lifetracker.command;

import lifetracker.calendar.CalendarList;

public interface CommandObject {

    String MESSAGE_ERROR = "Error: Command was not executed.";

    CalendarList execute(CalendarList calendar);

    CalendarList undo(CalendarList calendar);

    String getComment();

}
