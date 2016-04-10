package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.util.HashSet;
import java.util.Set;

/**
 * A generic command object.
 * <p>
 * This abstract class implements command comments, as well as checks for executing and undoing only when allowed.
 * Avoid calling the super method when inheriting to skip such checks.
 */
//@@author A0091173J
public abstract class CommandObject {

    protected static final String MESSAGE_ERROR = "Error: Command was not executed.";

    private boolean executed = false;
    private Set<Integer> highlightEntries = new HashSet<>();
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

    public Set<Integer> getHighlightEntries() {
        return new HashSet<>(highlightEntries);
    }

    protected void setComment(String comment) {
        assert comment != null;
        this.comment = comment;
    }

    protected void addHighlightEntry(int id) {
        highlightEntries.add(id);
    }

    protected boolean isExecuted() {
        return executed;
    }

    protected void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
