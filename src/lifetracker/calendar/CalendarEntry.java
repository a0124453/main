package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public interface CalendarEntry {

    public enum EntryType {
        FLOATING, DEADLINE, EVENT
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

    void setRecurring(TemporalAmount period);

    TemporalAmount getPeriod();

    void mark();

    boolean isActive();

    boolean isRecurring();

    boolean isToday();

    boolean isOngoing();

    boolean isOver();

    boolean equals(CalendarEntry entry);

    CalendarEntry copy();

}
