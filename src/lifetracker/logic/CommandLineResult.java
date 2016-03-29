package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class CommandLineResult implements ExecuteResult {

    private String comment;
    private List<List<String>> eventList;
    private List<Task> taskList;
    private CommandType commandType;

    private static final FormatStyle DATE_STYLE = FormatStyle.MEDIUM;
    private static final FormatStyle TIME_STYLE = FormatStyle.SHORT;

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
    public List<List<String>> getEventList() {
        return eventList;
    }

    @Override
    public List<Task> getTaskList() {
        return taskList;
    }

    @Override
    public void addTaskLine(int id, String name, LocalDateTime deadline) {
        List<String> record = new ArrayList<>();
        record.add(String.valueOf(id));
        record.add(name);
        
        if(deadline != null) {
            record.add(deadline.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));
        }
        
        Task task = new Task(record);
        
        taskList.add(task);
    }

    @Override
    public void addEventLine(int id, String name, LocalDateTime start, LocalDateTime end) {
        List<String> record = new ArrayList<>();
        record.add(String.valueOf(id));
        record.add(name);
        record.add(start.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));
        record.add(end.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));
        eventList.add(record);
    }

    @Override
    public void setType(String commandString) {
        if (commandString.equals("exit"))
            this.commandType = CommandType.EXIT;

        else if (commandString.equals("ERROR")) {
            this.commandType = CommandType.ERROR;
        }
        
        else
            this.commandType = CommandType.DISPLAY;
    }

    @Override
    public CommandType getType() {
        return this.commandType;
    }
}
