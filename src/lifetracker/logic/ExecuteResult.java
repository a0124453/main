package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public interface ExecuteResult {

    public enum CommandType {
        DISPLAY, SAVE, EXIT, ERROR, HELP
    }

    String getComment();

    void setComment(String comment);

    List<LogicEvent> getEventList();

    List<LogicTask> getTaskList();

    void addTaskLine(int id, String name, LocalDateTime deadline, boolean isOverdue, boolean isDone,
            TemporalAmount period, int limitOccur, LocalDateTime limitDate, boolean isNew);

    void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end, boolean isOverdue, boolean isDone,
            TemporalAmount period, int limitOccur, LocalDateTime limitDate, boolean isNew);

    CommandType getType();

    void setType(CommandType type);
}
