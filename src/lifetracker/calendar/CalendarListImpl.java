package lifetracker.calendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Predicate;

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
    public boolean delete(int id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            return true;
        } else if (eventList.containsKey(id)) {
            eventList.remove(id);
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#update(int, java.lang.String)
     */
    @Override
    public boolean update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd) {
        if (taskList.containsKey(id)) {
            taskList.get(id).setName(newName);
            taskList.get(id).setStart(newStart);
            taskList.get(id).setEnd(newEnd);
            return true;
        } else if (eventList.containsKey(id)) {
            eventList.get(id).setName(newName);
            eventList.get(id).setStart(newStart);
            eventList.get(id).setEnd(newEnd);
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#list(String)
     */
    @Override
    public List<CalendarEntry> list(String toSearch) {
        List<CalendarEntry> result = new ArrayList<CalendarEntry>();
        result.addAll(getTaskList());
        result.addAll(getEventList());
        Predicate<CalendarEntry> p = (entry) -> entry.getName().contains(toSearch);
        result.removeIf(p);
        return result;
    }

}
