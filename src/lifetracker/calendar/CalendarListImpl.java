package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Predicate;

public class CalendarListImpl implements CalendarList {

    // variables
    private TreeMap<Integer, CalendarEntry> taskList = new TreeMap<>();
    private TreeMap<Integer, CalendarEntry> eventList = new TreeMap<>();
    private TreeMap<Integer, CalendarEntry> archivedTaskList = new TreeMap<>();
    private TreeMap<Integer, CalendarEntry> archivedEventList = new TreeMap<>();

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
     * @see lifetracker.calendar.CalenderList#getArchivedTaskList()
     */
    @Override
    public List<CalendarEntry> getArchivedTaskList() {
        List<CalendarEntry> list = new ArrayList<CalendarEntry>(archivedTaskList.values());
        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#getArchivedEventList()
     */
    @Override
    public List<CalendarEntry> getArchivedEventList() {
        List<CalendarEntry> list = new ArrayList<CalendarEntry>(archivedEventList.values());
        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String)
     */
    @Override
    public int add(String name) {
        assert name != null && !name.isEmpty();
        int eventMax = eventList.isEmpty() ? 0 : eventList.lastKey();
        int taskMax = taskList.isEmpty() ? 0 : taskList.lastKey();
        int idToSet = Math.max(eventMax, taskMax) + 1;
        CalendarEntryImpl ft = new CalendarEntryImpl(name, null, null, idToSet);
        taskList.put(idToSet, ft);
        return idToSet;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String,
     * java.time.LocalDateTime)
     */
    @Override
    public int add(String name, LocalDateTime deadline) {
        assert name != null && !name.isEmpty();
        assert deadline != null;
        int taskMax = taskList.isEmpty() ? 0 : taskList.lastKey();
        int eventMax = eventList.isEmpty() ? 0 : eventList.lastKey();
        int idToSet = Math.max(taskMax, eventMax) + 1;
        CalendarEntryImpl dt = new CalendarEntryImpl(name, null, deadline, idToSet);
        taskList.put(idToSet, dt);
        return idToSet;
    }

    @Override
    public int add(String name, LocalDateTime deadline, TemporalAmount period) {
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#add(java.lang.String,
     * java.time.LocalDateTime, java.time.LocalDateTime)
     */
    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end) {
        assert start != null;
        assert end != null;
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date is after end date!");
        }
        int eventMax = eventList.isEmpty() ? 0 : eventList.lastKey();
        int taskMax = taskList.isEmpty() ? 0 : taskList.lastKey();
        int idToSet = Math.max(eventMax, taskMax) + 1;
        CalendarEntryImpl e = new CalendarEntryImpl(name, start, end, idToSet);
        eventList.put(idToSet, e);
        return idToSet;
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end, TemporalAmount period) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#delete(int)
     */
    @Override
    public CalendarEntry delete(int id) {
        if (taskList.containsKey(id)) {
            CalendarEntry copy = taskList.get(id);
            taskList.remove(id);
            return copy;
        } else if (eventList.containsKey(id)) {
            CalendarEntry copy = taskList.get(id);
            eventList.remove(id);
            return copy;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#update(int, java.lang.String)
     */
    @Override
    public CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd) {
        if (taskList.containsKey(id)) {
            CalendarEntry toUpdate = taskList.get(id);
            CalendarEntry copy = toUpdate.copy();
            toUpdate.setName(newName);
            toUpdate.setStart(newStart);
            toUpdate.setEnd(newEnd);
            return copy;
        } else if (eventList.containsKey(id)) {
            CalendarEntry toUpdate = eventList.get(id);
            CalendarEntry copy = toUpdate.copy();
            toUpdate.setName(newName);
            toUpdate.setStart(newStart);
            toUpdate.setEnd(newEnd);
            return copy;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#list(String)
     */
    @Override
    public CalendarList find(String toSearch) {
        List<CalendarEntry> result = new ArrayList<CalendarEntry>();
        result.addAll(getTaskList());
        result.addAll(getEventList());
        Predicate<CalendarEntry> p = (entry) -> entry.getName().contains(toSearch);
        result.removeIf(p);
        return null;
    }

    @Override
    public CalendarList findArchived(String toSearch) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarList findAll(String toSearch) {
        // TODO Auto-generated method stub
        return null;
    }

}
