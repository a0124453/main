package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.Period;

import lifetracker.calendar.visitor.VisitableEntry;

public interface CalendarEntry extends VisitableEntry {

    String MESSAGE_ERROR_START_AFTER_END = "Start date/time cannot be after end date/time!";

    static void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException(MESSAGE_ERROR_START_AFTER_END);
        }
    }

    /**
     * @return The unique ID number of the entry.
     */
    int getId();

    /**
     * @param id
     *            The desired ID number to assign to the entry.
     */
    void setId(int id);

    /**
     * @return The description of the entry.
     */
    String getName();

    /**
     * @param name
     *            The desired name to assign to the entry.
     */
    void setName(String name);

    /**
     * @param property
     *            An {@code enum} representing the desired property.
     * 
     * @return A {@code LocalDateTime} property of the entry.
     */
    LocalDateTime getDateTime(CalendarProperty property);

    void setDateTime(CalendarProperty property, LocalDateTime dateTime);

    void setPeriod(Period period);

    Period getPeriod();

    void toggleActive();

    boolean isProperty(CalendarProperty property);

    /**
     * @param property
     *            An {@code enum} representing the desired property.
     * 
     * @return An integer property of the entry.
     */
    int getIntegerProperty(CalendarProperty property);

}