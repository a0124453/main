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

    /**
     * @param property
     *            An {@code enum} representing the property to be modified.
     * 
     * @param dateTime
     *            The new {@code LocalDateTime} to be set.
     */
    void setDateTime(CalendarProperty property, LocalDateTime dateTime);

    /**
     * @param period
     *            The period to assign to the (recurring) entry.
     */
    void setPeriod(Period period);

    /**
     * @return The period of the (recurring) entry.
     */
    Period getPeriod();

    /**
     * Switches the status of an active entry to inactive, and vice versa.
     */
    void toggleActive();

    /**
     * @param property
     *            An {@code enum} representing the desired property.
     * 
     * @return A {@code boolean} to indicate whether the entry satisfies the
     *         specified property.
     */
    boolean isProperty(CalendarProperty property);

    /**
     * @param property
     *            An {@code enum} representing the desired property.
     * 
     * @return An integer property of the entry.
     */
    int getIntegerProperty(CalendarProperty property);

}