package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

//@@author A0149467N

public interface ExecuteResult {

    /**
     * collections of command type
     */
    public enum CommandType {
        DISPLAY, SAVE, EXIT, ERROR, HELP
    }

    /**
     * @return The comment received after the execution of command.
     */
    String getComment();

    void setComment(String comment);

    /**
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

    void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end, boolean isOverdue, boolean isActive,
            Period period, int limitOccur, LocalDate limitDate, boolean isNew);

    /**
     * @return The type of the command
     */
    CommandType getType();
    
    void setType(CommandType type);
}
