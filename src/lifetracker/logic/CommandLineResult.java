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

    private static final String DAY_FIELD = "day(s)";
    private static final String MONTH_FIELD = "month(s)";
    private static final String YEAR_FIELD = "year(s)";

    private static final String MINUTE_FIELD = "minute(s)";
    private static final String HOUR_FIELD = "hour(s)";

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

        if (deadline == null) {
            record.add("");
        } else {
            record.add(deadline.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));

            if (period == null) {
                record.add("");
            } else {
                record.add(convert(period));
            }
        }

        taskList.add(record);
    }

    @Override
    public void addEventLine(int id, String name, boolean isActive, LocalDateTime start, LocalDateTime end,
            TemporalAmount period) {
        List<String> record = new ArrayList<>();
        record.add(String.valueOf(id));
        record.add(name);
        record.add(Boolean.toString(isActive));
        record.add(start.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));
        record.add(end.format(DateTimeFormatter.ofLocalizedDateTime(DATE_STYLE, TIME_STYLE)));

        if (period == null) {
            record.add("");
        } else {
            record.add(convert(period));
        }

        eventList.add(record);
    }

    public String convert(TemporalAmount temporalAmount) {
        if (temporalAmount != null) {
            if (temporalAmount.equals(Period.ZERO) || temporalAmount.equals(Duration.ZERO)) {
                return "";
            } else if (temporalAmount instanceof Period) {
                return convertPeriodToString(((Period) temporalAmount).normalized());
            } else {
                return convertDurationToString((Duration) temporalAmount);
            }
        }
        return "";
    }

    private String convertPeriodToString(Period period) {
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        return formatDuration(years, YEAR_FIELD) + formatDuration(months, MONTH_FIELD) + formatDuration(days, DAY_FIELD);
    }

    private String convertDurationToString(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        return formatDuration(hours, HOUR_FIELD) + formatDuration(minutes, MINUTE_FIELD);
    }

    private String formatDuration(long duration, String label) {
        return duration == 0 ? "" : duration + " " + label + " ";
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
