package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.Period;

public interface CalendarEntry {

    public static final String MESSAGE_ERROR_START_AFTER_END = "Start date/time cannot be after end date/time!";
    public static final String MESSAGE_ERROR_ILLEGAL_TYPE_CHANGE_TASK_TO_EVENT = "A task cannot be changed into an event!";

    public static void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException(MESSAGE_ERROR_START_AFTER_END);
        }
    }

    // get() and set() functions for variables
    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    LocalDateTime getDateTime(CalendarProperty property);

    void setDateTime(CalendarProperty property, LocalDateTime dateTime);

    void setPeriod(Period period);

    Period getPeriod();

    void mark();

    boolean isProperty(CalendarProperty property);

    boolean equals(CalendarEntry entry);

    CalendarEntry copy();

}