package lifetracker.logic;

import java.time.LocalDateTime;
import java.util.List;

public interface ExecuteResult {

    public enum CommandType {
        DISPLAY, EXIT
    }

    String getComment();

    void setComment(String comment);

    List<List<String>> getEventList();

    List<List<String>> getTaskList();

    void addTaskLine(int id, String name, LocalDateTime deadline);

    void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end);

    CommandType getType();

    void setType(String commandString);
}
