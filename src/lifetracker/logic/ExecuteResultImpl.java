package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

//@@author A0149467N

public class ExecuteResultImpl implements ExecuteResult {

    //Comment received after the execution of command
    private String comment;
    
    //List of events and tasks displayed in UI
    private List<LogicEvent> eventList;
    private List<LogicTask> taskList;
    
    //Type of the command
    private CommandType commandType;

    public ExecuteResultImpl() {
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
    public List<LogicTask> getTaskList() {
        return taskList;
    }
    
    @Override
    public List<LogicEvent> getEventList() {
        return eventList;
    }

    @Override
    public void addTaskLine(int id, String name, LocalDateTime deadline, boolean isOverdue, boolean isActive,
            Period period, int limitOccur, LocalDate limitDate, boolean isNew) {
        LogicTask record = new LogicTaskImpl();
        record.setId(id);
        record.setName(name);
        record.setDeadline(deadline);
        record.setOverdue(isOverdue);
        record.setActive(isActive);
        record.setPeriod(period);
        record.setLimitOccur(limitOccur);
        record.setLimitDate(limitDate);
        record.setNew(isNew);
        taskList.add(record);
    }

    @Override
    public void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end, boolean isOverdue,
            boolean isActive, Period period, int limitOccur, LocalDate limitDate, boolean isNew) {
        LogicEvent record = new LogicEventImpl();
        record.setId(id);
        record.setName(name);
        record.setStart(start);
        record.setEnd(end);
        record.setOverdue(isOverdue);
        record.setActive(isActive);
        record.setPeriod(period);
        record.setLimitOccur(limitOccur);
        record.setLimitDate(limitDate);
        record.setNew(isNew);
        eventList.add(record);
    }

    @Override
    public CommandType getType() {
        return this.commandType;
    }
    
    @Override
    public void setType(CommandType type) {
        assert type != null;
        
        this.commandType = type;
    }
}
