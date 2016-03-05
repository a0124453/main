package lifetracker.calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarListImpl implements CalendarList {

    // variables
    private List<CalendarEntry> taskList = new ArrayList<>();
    private List<CalendarEntry> eventList = new ArrayList<>();

    // get() and set() functions for variables

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#getTaskList()
     */
    @Override
    public List<CalendarEntry> getTaskList() {
        return taskList;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#getEventList()
     */
    @Override
    public List<CalendarEntry> getEventList() {
        return eventList;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String,
     * java.time.LocalDateTime)
     */
    @Override
    public void add(String name) {
        CalendarEntryImpl ft = new CalendarEntryImpl(name, null, null);
        taskList.add(ft);
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String,
     * java.time.LocalDateTime)
     */
    @Override
    public void add(String name, LocalDateTime deadline) {
        CalendarEntryImpl dt = new CalendarEntryImpl(name, null, deadline);
        taskList.add(dt);
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String,
     * java.time.LocalDateTime, java.time.LocalDateTime)
     */
    @Override
    public void add(String name, LocalDateTime start, LocalDateTime end) {
        CalendarEntryImpl e = new CalendarEntryImpl(name, start, end);
        eventList.add(e);
    }

}
