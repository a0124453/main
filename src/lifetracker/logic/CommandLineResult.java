package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

public class CommandLineResult implements ExecuteResult {

    private String comment;
    private List<LogicEvent> eventList;
    private List<LogicDeadline> deadlineList;
    private List<LogicFloating> floatingList;
    private CommandType commandType;

    public CommandLineResult() {
        this.eventList = new ArrayList<>();
        this.deadlineList = new ArrayList<>();
        this.floatingList = new ArrayList<>();
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
    public List<LogicDeadline> getDeadlineList() {
        return deadlineList;
    }

    @Override
    public List<LogicFloating> getFloatingList() {
        return floatingList;
    }

    @Override
    public void addTaskLine(int id, String name, LocalDateTime deadline, boolean isOverdue, boolean isDone,
            TemporalAmount period) {
        if (deadline == null) {
            addFloatingLine(id, name, isDone);
        } else {
            addDeadlineLine(id, name, deadline, isOverdue, isDone, period);
        }
    }

    private void addDeadlineLine(int id, String name, LocalDateTime deadline, boolean isOverdue, boolean isDone,
            TemporalAmount period) {
        LogicDeadline record = new LogicDeadlineImpl();
        record.setId(id);
        record.setName(name);
        record.setDeadline(deadline);
        record.setOverdue(isOverdue);
        record.setDone(isDone);
        record.setPeriod(period);
        deadlineList.add(record);
    }

    private void addFloatingLine(int id, String name, boolean isDone) {
        LogicFloating record = new LogicFloatingImpl();
        record.setId(id);
        record.setName(name);
        record.setDone(isDone);
        floatingList.add(record);
    }

    @Override
    public void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end, boolean isOverdue,
            boolean isDone, TemporalAmount period) {
        LogicEvent record = new LogicEventImpl();
        record.setId(id);
        record.setName(name);
        record.setStart(start);
        record.setEnd(end);
        record.setOverdue(isOverdue);
        record.setDone(isDone);
        record.setPeriod(period);
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
