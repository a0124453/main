package lifetracker.storage;

import lifetracker.calendar.CalendarList;

import java.io.IOException;

/**
 * A storage mechanism for storing calendars.
 */
public interface Storage extends AutoCloseable {

    /**
     * Sets the storage destination, for example, the filename.
     *
     * @param destination The destination string.
     */
    void setStore(String destination) throws IOException;

    void store(CalendarList calendar) throws IOException;

    /**
     * Loads the data from the storage file and adds it to the calendar list.
     * <p>
     * The method then returns the same calendar list.
     *
     * @param calendar The calendar object to load into.
     * @return The loaded calendar
     * @throws IOException If there was an error reading the data file.
     */
    CalendarList load(CalendarList calendar) throws IOException;
}
