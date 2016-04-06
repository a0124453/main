package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

public class CommandLineResult implements ExecuteResult {

    private String comment;
    private List<LogicEvent> eventList;
    private List<LogicTask> taskList;
    private CommandType commandType;

    public CommandLineResult() {
        this.eventList = new ArrayList<>();
        this.taskList = new ArrayList<>();
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String newComment) {
        assert newComment != null;

        comment = newComment;
    }

    @Override
    public List<LogicEvent> getEventList() {
        return eventList;
    }

    @Override
    public List<LogicTask> getTaskList() {
        return taskList;
    }

    @Override
    public void addTaskLine(int id, String name, LocalDateTime deadline, boolean isOverdue, boolean isDone,
            TemporalAmount period, int limitOccur, LocalDateTime limitDate, boolean isNew) {
        LogicTask record = new LogicTaskImpl();
        record.setId(id);
        record.setName(name);
        record.setDeadline(deadline);
        record.setOverdue(isOverdue);
        record.setDone(isDone);
        record.setPeriod(period);
        record.setLimitOccur(limitOccur);
        record.setLimitDate(limitDate);
        record.setNew(isNew);
        taskList.add(record);
    }

    @Override
    public void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end, boolean isOverdue,
            boolean isDone, TemporalAmount period, int limitOccur, LocalDateTime limitDate, boolean isNew) {
        LogicEvent record = new LogicEventImpl();
        record.setId(id);
        record.setName(name);
        record.setStart(start);
        record.setEnd(end);
        record.setOverdue(isOverdue);
        record.setDone(isDone);
        record.setPeriod(period);
        record.setLimitOccur(limitOccur);
        record.setLimitDate(limitDate);
        record.setNew(isNew);
        eventList.add(record);
    }

    @Override
    public void setType(CommandType type) {
        this.commandType = type;
    }

    @Override
    public CommandType getType() {
        return this.commandType;
    }
}
