package lifetracker.storage;

import lifetracker.calendar.CalendarList;

import java.io.IOException;

/**
 * A storage mechanism for storing calendars.
 */
public interface Storage {

    /**
     * Sets the storage destination, for example, the filename.
     *
     * @param destination The destination string.
     */
    void setStore(String destination) throws IOException;

    void store(CalendarList calendar) throws IOException;

    CalendarList load() throws IOException;

}
