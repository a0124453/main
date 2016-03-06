package lifetracker.calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CalendarListImpl implements CalendarList {

    // variables
    private TreeMap<Integer, CalendarEntry> taskList = new TreeMap<>();
    private TreeMap<Integer, CalendarEntry> eventList = new TreeMap<>();

    // get() and set() functions for variables

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#getTaskList()
     */
    @Override
    public List<CalendarEntry> getTaskList() {
        List<CalendarEntry> list = new ArrayList<CalendarEntry>(taskList.values());
        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#getEventList()
     */
    @Override
    public List<CalendarEntry> getEventList() {
        List<CalendarEntry> list = new ArrayList<CalendarEntry>(eventList.values());
        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String)
     */
    @Override
    public void add(String name) {
        int idToSet = Math.max(eventList.lastKey(), taskList.lastKey()) + 1;
        CalendarEntryImpl ft = new CalendarEntryImpl(name, null, null, idToSet);
        taskList.put(idToSet, ft);

    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String,
     * java.time.LocalDateTime)
     */
    @Override
    public void add(String name, LocalDateTime deadline) {
        int idToSet = Math.max(eventList.lastKey(), taskList.lastKey()) + 1;
        CalendarEntryImpl dt = new CalendarEntryImpl(name, null, deadline, idToSet);
        taskList.put(idToSet, dt);
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String,
     * java.time.LocalDateTime, java.time.LocalDateTime)
     */
    @Override
    public void add(String name, LocalDateTime start, LocalDateTime end) {
        int idToSet = Math.max(eventList.lastKey(), taskList.lastKey()) + 1;
        CalendarEntryImpl e = new CalendarEntryImpl(name, start, end, idToSet);
        eventList.put(idToSet, e);
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#delete(int)
     */
    @Override
    public void delete(int id) {

    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#update(int, java.lang.String)
     */
    @Override
    public void update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd) {

    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#list(String)
     */
    @Override
    public void list(String toSearch) {

    }

}
