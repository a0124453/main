package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

import lifetracker.logic.LogicImpl.CommandType;

public interface ExecuteResult {

    String getComment();

    void setComment(String comment);

    List<List<String>> getEventList();

    List<List<String>> getTaskList();

    void addTaskLine(int id, String name, boolean isActive, LocalDateTime deadline, TemporalAmount period);

    void addEventLine(int id, String name, boolean isActive, LocalDateTime start, LocalDateTime end, TemporalAmount period);

    CommandType getType();

    void setType(CommandType type);
}
