package lifetracker.storage;

import lifetracker.calendar.CalendarList;

/**
 * A storage mechanism for storing calendars.
 */
public interface Storage {

    /**
     * Sets the storage destination, for example, the filename.
     *
     * @param destination The destination string.
     */
    void setStore(String destination);

    void store(CalendarList calendar);

    CalendarList load();

}
