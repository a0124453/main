package lifetracker.calendar;

import lifetracker.calendar.visitor.VisitableEntry;

import java.time.LocalDateTime;
import java.time.Period;

public interface CalendarEntry extends VisitableEntry {

    String MESSAGE_ERROR_START_AFTER_END = "Start date/time cannot be after end date/time!";

    static void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
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

    int getIntegerProperty(CalendarProperty property);

}