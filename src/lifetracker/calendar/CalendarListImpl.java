package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Predicate;

import lifetracker.calendar.CalendarEntry.EntryType;

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

    void updateEntryName(CalendarEntry toUpdate, String newName) {
        if (newName != null && !newName.isEmpty()) {
            toUpdate.setName(newName);
        }
    }

    void updateEntryStart(CalendarEntry toUpdate, LocalDateTime newStart) {
        if (newStart != null) {
            toUpdate.setStart(newStart);
        }
    }

    void updateEntryEnd(CalendarEntry toUpdate, LocalDateTime newEnd) {
        if (toUpdate.getType().equals(EntryType.EVENT)) {
            if (newEnd != null) {
                toUpdate.setEnd(newEnd);
            }
        } else if (toUpdate.getType().equals(EntryType.DEADLINE)) {
            if (newEnd != null) {
                toUpdate.setEnd(newEnd);
            }
        } else {
            assert toUpdate.getType().equals(EntryType.FLOATING);
            if (newEnd != null) {
                toUpdate.setEnd(newEnd);
                toUpdate.setType(EntryType.DEADLINE);
            }
        }
        // allowed to change floating task to deadline but not the other way
        // around
    }

    void checkUpdateArguments(CalendarEntry toUpdate, LocalDateTime newStart, LocalDateTime newEnd) {
        if (toUpdate.getType().equals(EntryType.EVENT)) {
            if (newStart != null && newEnd != null) {
                CalendarEntry.checkStartBeforeEnd(newStart, newEnd);
            } else if (newStart != null && newEnd == null) {
                CalendarEntry.checkStartBeforeEnd(newStart, toUpdate.getEnd());
            } else if (newStart == null && newEnd != null) {
                CalendarEntry.checkStartBeforeEnd(toUpdate.getStart(), newEnd);
            }
        } else if (toUpdate.getType().equals(EntryType.DEADLINE)) {
            if (newStart != null) {
                throw new IllegalArgumentException(CalendarEntry.MESSAGE_ERROR_ILLEGAL_TYPE_CHANGE_TASK_TO_EVENT);
            }
        } else {
            assert toUpdate.getType().equals(EntryType.FLOATING);
            if (newStart != null) {
                throw new IllegalArgumentException(CalendarEntry.MESSAGE_ERROR_ILLEGAL_TYPE_CHANGE_TASK_TO_EVENT);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#update(int, java.lang.String)
     */
    @Override
    public CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            TemporalAmount newPeriod) {
        if (taskList.containsKey(id)) {
            CalendarEntry toUpdate = taskList.get(id);
            CalendarEntry copy = toUpdate.copy();
            checkUpdateArguments(toUpdate, newStart, newEnd);
            updateEntryName(toUpdate, newName);
            updateEntryStart(toUpdate, newStart);
            updateEntryEnd(toUpdate, newEnd);
            return copy;
        } else if (eventList.containsKey(id)) {
            CalendarEntry toUpdate = eventList.get(id);
            CalendarEntry copy = toUpdate.copy();
            checkUpdateArguments(toUpdate, newStart, newEnd);
            updateEntryName(toUpdate, newName);
            updateEntryStart(toUpdate, newStart);
            updateEntryEnd(toUpdate, newEnd);
            return copy;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#find(String)
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
