package lifetracker.command;

import lifetracker.calendar.CalendarList;

/**
 * A generic command object.
 * <p>
 * This abstract class implements command comments, as well as checks for executing and undoing only when allowed.
 * Avoid calling the super method when inheriting to skip such checks.
 */
//@@author A0091173J
public abstract class CommandObject {

    static final String MESSAGE_ERROR = "Error: Command was not executed.";

    private boolean executed = false;

    private String comment = MESSAGE_ERROR;

    public CalendarList execute(CalendarList calendar) {
        assert !executed;
        executed = true;
        return calendar;
    }

    public CalendarList undo(CalendarList calendar) {
        assert executed;
        executed = false;
        return calendar;
    }

    public String getComment() {
        return comment;
    }

    protected void setComment(String comment) {
        assert comment != null;
        this.comment = comment;
    }

    protected boolean isExecuted() {
        return executed;
    }

    protected void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
