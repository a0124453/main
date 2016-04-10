package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

//@@author A0149467N

public interface ExecuteResult {

    public enum CommandType {
        DISPLAY, SAVE, EXIT, ERROR, HELP
    }

    /**
     * Get the comment received after the execution of command.
     *
     * @return The comment received after the execution of command.
     */
    String getComment();

    /**
     * Set the comment received after the execution of command.
     *
     * @param comment The comment received after the execution of command.
     */
    void setComment(String comment);

    /**
     * Get the list of tasks and events
     *
     * @return The updated list of tasks and events displayed in UI
     */
    List<LogicTask> getTaskList();
    
    List<LogicEvent> getEventList();

    /**
     * Adds a LogicTask object to the task list
     *
     * @param id
     *            Id of the task
     * @param name
     *            Name of the task.
     * @param deadline
     *            Deadline of the task.
     * @param isOverdue
     *            Whether the task is overdue.
     * @param isActive
     *            Whether the task is active or is done.
     * @param period
     *            Period of the task
     * @param limitOccur
     *            The occurrence limit for the recurring task
     * @param limitDate
     *            The limit date for the recurring task
     * @param isNew
     *            Whether the task is highlighted
     */
    void addTaskLine(int id, String name, LocalDateTime deadline, boolean isOverdue, boolean isActive,
            Period period, int limitOccur, LocalDate limitDate, boolean isNew);

    /**
     * Adds a LogicEvent object to the event list
     *
     * @param id
     *            Id of the event
     * @param name
     *            Name of the event.
     * @param start
     *            Start date and time of the event.
     * @param end
     *            End date and time of the event.
     * @param isOverdue
     *            Whether the event is overdue.
     * @param isActive
     *            Whether the event is active or is done.
     * @param period
     *            Period of the event
     * @param limitOccur
     *            The occurrence limit for the recurring event
     * @param limitDate
     *            The limit date for the recurring event
     * @param isNew
     *            Whether the event is highlighted
     */
    void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end, boolean isOverdue, boolean isActive,
            Period period, int limitOccur, LocalDate limitDate, boolean isNew);

    /**
     * Get the type of the command
     *
     * @return The type of the command
     */
    CommandType getType();
    
    /**
     * Set the type of the command
     *
     * @param type The type of the command
     */
    void setType(CommandType type);
}
