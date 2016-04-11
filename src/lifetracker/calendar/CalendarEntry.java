package lifetracker.calendar;

import lifetracker.calendar.visitor.VisitableEntry;

import java.time.LocalDateTime;
import java.time.Period;

//@@author A0108473E
public interface CalendarEntry extends VisitableEntry {

    String MESSAGE_ERROR_START_AFTER_END = "Start date/time cannot be after end date/time!";

    static void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException(MESSAGE_ERROR_START_AFTER_END);
        }
    }

    /**
     * Returns the unique ID number of the entry.
     * 
     * @return The unique ID number of the entry.
     */
    int getId();

    /**
     * Sets the ID number of the entry to {@code id}.
     * 
     * @param id
     *            The desired ID number to assign to the entry.
     */
    void setId(int id);

    /**
     * Returns the name of the entry.
     * 
     * @return The name of the entry.
     */
    String getName();

    /**
     * Sets the name of the entry to {@code name}.
     * 
     * @param name
     *            The desired name to assign to the entry.
     */
    void setName(String name);

    /**
     * Returns the start/end date and time of the entry depending on the
     * {@code property} provided.
     * 
     * @param property
     *            An {@code enum} representing the desired property.
     * 
     * @return A {@code LocalDateTime} property of the entry.
     */
    LocalDateTime getDateTime(CalendarProperty property);

    /**
     * Sets the start/end date and time of the entry depending on the
     * {@code property} provided.
     * 
     * @param property
     *            An {@code enum} representing the property to be modified.
     * 
     * @param dateTime
     *            The new {@code LocalDateTime} to be set.
     */
    void setDateTime(CalendarProperty property, LocalDateTime dateTime);

    /**
     * Sets the period of the (recurring) entry to {@code period}.
     * 
     * @param period
     *            The period to assign to the (recurring) entry.
     */
    void setPeriod(Period period);

    /**
     * Returns the period of the (recurring) entry.
     * 
     * @return The period of the (recurring) entry.
     */
    Period getPeriod();

    /**
     * Switches the status of an active entry to inactive, and vice versa.
     */
    void toggleActive();

    /**
     * Returns a {@code boolean} to indicate whether the entry satisfies the
     * specified property.
     * 
     * @param property
     *            An {@code enum} representing the desired property.
     * 
     * @return A {@code boolean} to indicate whether the entry satisfies the
     *         specified property.
     */
    boolean isProperty(CalendarProperty property);

    /**
     * Returns an integer property of the entry.
     * 
     * @param property
     *            An {@code enum} representing the desired property.
     * 
     * @return An integer property of the entry.
     */
    int getIntegerProperty(CalendarProperty property);

}