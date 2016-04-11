package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.util.HashSet;
import java.util.Set;

//@@author A0091173J

/**
 * A generic command object.
 * <p>
 * This abstract class implements command comments, as well as checks for executing and undoing only when allowed.
 * Avoid calling the super method when inheriting to skip such checks.
 */
public abstract class CommandObject {

    protected static final String MESSAGE_ERROR = "Error: Command was not executed.";

    private boolean executed = false;
    private Set<Integer> highlightEntries = new HashSet<>();
    private String comment = MESSAGE_ERROR;

    /**
     * Executes this command.
     * <p>
     * The command will also be marked as executed.
     *
     * @param calendar The {@code CalendarList} object to execute on
     * @return The {@code CalendarList} after execution
     */
    public CalendarList execute(CalendarList calendar) {
        assert !executed;
        executed = true;
        return calendar;
    }

    /**
     * Undo this command.
     * <p>
     * Command will be marked as not executed.
     *
     * @param calendar The {@code CalendarList} object to undo on
     * @return The {@code CalendarList} after undo
     */
    public CalendarList undo(CalendarList calendar) {
        assert executed;
        executed = false;
        return calendar;
    }

    /**
     * Gets the comment String from this CommandObject
     * @return The comment String
     */
    public String getComment() {
        return comment;
    }

    /**
     * Gets a list of entry IDs that was involved in the execution of this {@code CommandObject}.
     * @return The list of entry IDs
     */
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
