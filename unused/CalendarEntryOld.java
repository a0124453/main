package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public interface CalendarEntryOld {

    public static final String MESSAGE_ERROR_START_AFTER_END = "Start date/time cannot be after end date/time!";
    public static final String MESSAGE_ERROR_ILLEGAL_TYPE_CHANGE_TASK_TO_EVENT = "A task cannot be changed into an event!";

    public enum EntryType {
        FLOATING, DEADLINE, EVENT
    }

    public static void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException(MESSAGE_ERROR_START_AFTER_END);
        }
    }

    // get() and set() functions for variables
    int getId();

    String getName();

    void setName(String name);

    LocalDateTime getStart();

    void setStart(LocalDateTime start);

    LocalDateTime getEnd();

    void setEnd(LocalDateTime end);

    LocalTime getStartTime();

    LocalTime getEndTime();

    EntryType getType();

    void setPeriod(TemporalAmount period);

    TemporalAmount getPeriod();

    void mark();

    boolean isActive();

    boolean isRecurring();

    boolean isToday();

    boolean isOngoing();

    boolean isOver();

    boolean equals(CalendarEntryOld entry);

    CalendarEntryOld copy();

}
