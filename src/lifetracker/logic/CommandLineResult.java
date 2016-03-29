package lifetracker.logic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

public class CommandLineResult implements ExecuteResult {

    private String comment;
    private List<List<String>> eventList;
    private List<List<String>> taskList;
    private CommandType commandType;

    private static final FormatStyle DATE_STYLE = FormatStyle.MEDIUM;
    private static final FormatStyle TIME_STYLE = FormatStyle.SHORT;
    
    private static final String ZERO_YEARS_MONTHS_AND_DAYS_PATTERN = "00Y00M00D";
    private static final String ZERO_HOURS_MINUTES_AND_SECONDS_PATTERN = "00H00M00S";
    
    private static final long SECONDS_PER_HOUR = 3600;
    private static final long SECONDS_PER_MINUTE = 60;

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
    public List<List<String>> getTaskList() {
        return taskList;
    }

    @Override
    public void addTaskLine(int id, String name, boolean isActive, LocalDateTime deadline, TemporalAmount period) {
        List<String> record = new ArrayList<>();
        record.add(String.valueOf(id));
        record.add(name);
        record.add(Boolean.toString(isActive));
        
        if(deadline != null) {
            record.add(deadline.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));
            if(period != null) {
                record.add(convert(period));
            }
        }
        
        taskList.add(record);
    }

    @Override
    public void addEventLine(int id, String name, boolean isActive, LocalDateTime start, LocalDateTime end, TemporalAmount period) {
        List<String> record = new ArrayList<>();
        record.add(String.valueOf(id));
        record.add(name);
        record.add(Boolean.toString(isActive));
        record.add(start.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));
        record.add(end.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));
        
        if(period != null) {
            record.add(convert(period));
        }
        
        eventList.add(record);
    }
    
    public String convert(TemporalAmount temporalAmount) {
        if (temporalAmount != null) {
            if (temporalAmount instanceof Period) {
                Period period = ((Period) temporalAmount).normalized();
                return new StringBuilder("P").append(normalizeString(period.getYears())).append("Y")
                        .append(normalizeString(period.getMonths())).append("M")
                        .append(normalizeString(period.getDays())).append("D").append("T")
                        .append(ZERO_HOURS_MINUTES_AND_SECONDS_PATTERN).toString();
            } else {
                return convertDurationToString((Duration) temporalAmount);
            }
        }
        return null;
    }
    
    private String convertDurationToString(Duration duration) {
        long hours = duration.getSeconds() / SECONDS_PER_HOUR;
        int minutes = (int) ((duration.getSeconds() % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
        int seconds = (int) (duration.getSeconds() % SECONDS_PER_MINUTE);
        return new StringBuilder("P").append(ZERO_YEARS_MONTHS_AND_DAYS_PATTERN).append("T").append(String.valueOf(hours))
                .append("H").append(normalizeString(minutes)).append("M").append(normalizeString(seconds)).append("S")
                .toString();
    }
    
    private String normalizeString(int amount) {
        return normalizeString(String.valueOf(amount));
    }
    
    private String normalizeString(String result) {
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }

    @Override
    public void setType(String commandString) {
        if (commandString.equals("exit"))
            this.commandType = CommandType.EXIT;
        
        else if (commandString.substring(0, 6).equals("saveat")) {
            this.commandType = CommandType.SAVE;
        }

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
