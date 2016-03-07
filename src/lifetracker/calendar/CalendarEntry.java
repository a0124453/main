package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface CalendarEntry {

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

    boolean isToday();

    boolean isOngoing();

    boolean isOver();

}
