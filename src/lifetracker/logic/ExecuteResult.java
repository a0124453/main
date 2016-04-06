package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

public interface ExecuteResult {

    public enum CommandType {
        DISPLAY, SAVE, EXIT, ERROR, HELP
    }

    String getComment();

    void setComment(String comment);

    List<LogicEvent> getEventList();

    List<LogicTask> getTaskList();

    void addTaskLine(int id, String name, LocalDateTime deadline, boolean isOverdue, boolean isActive,
            Period period, int limitOccur, LocalDate limitDate, boolean isNew);

    void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end, boolean isOverdue, boolean isActive,
            Period period, int limitOccur, LocalDate limitDate, boolean isNew);

    CommandType getType();

    void setType(CommandType type);
}
