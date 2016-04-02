package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public interface ExecuteResult {
    
    public enum CommandType {
        DISPLAY, SAVE, EXIT, ERROR
    }

    String getComment();

    void setComment(String comment);

    List<LogicEvent> getEventList();

    List<LogicDeadline> getDeadlineList();
    
    List<LogicFloating> getFloatingList();

    void addFloatingLine(int id, String name, boolean isDone);
    
    void addDeadlineLine(int id, String name, LocalDateTime deadline, boolean isOverdue, boolean isDone, TemporalAmount period);

    void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end, boolean isOverdue, boolean isDone, TemporalAmount period);

    CommandType getType();

    void setType(CommandType type);
}
