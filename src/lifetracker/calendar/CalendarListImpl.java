package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import lifetracker.calendar.CalendarEntry.EntryType;

public class CalendarListImpl implements CalendarList {

    // variables
    protected TreeMap<Integer, CalendarEntry> taskList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> eventList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> archivedTaskList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> archivedEventList = new TreeMap<>();

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
        int idToSet = this.add(name, deadline);
        this.taskList.get(idToSet).setPeriod(period);
        return idToSet;
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
        int idToSet = this.add(name, start, end);
        this.eventList.get(idToSet).setPeriod(period);
        return idToSet;
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
    public CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            TemporalAmount newPeriod) {
        CalendarEntry toUpdate;
        CalendarEntry copy;
        if (taskList.containsKey(id)) {
            toUpdate = taskList.get(id);
            copy = toUpdate.copy();
        } else if (eventList.containsKey(id)) {
            toUpdate = eventList.get(id);
            copy = toUpdate.copy();
        } else {
            return null;
        }
        checkUpdateArguments(toUpdate, newStart, newEnd);
        updateEntryName(toUpdate, newName);
        updateEntryStart(toUpdate, newStart);
        updateEntryEnd(toUpdate, newEnd);
        updateEntryPeriod(toUpdate, newPeriod);
        return copy;
    }

    @Override
    public CalendarEntry mark(int id) {
        CalendarEntry entry;
        if (this.taskList.containsKey(id)) {
            entry = this.taskList.get(id);
            assert entry.isActive();
            entry.mark();
            if (entry.getType().equals(EntryType.FLOATING)) {
                this.archiveTask(id);
            } else {
                assert entry.getType().equals(EntryType.DEADLINE);
                if (!entry.isRecurring()) {
                    this.archiveTask(id);
                }
            }
            return entry;
        } else if (this.eventList.containsKey(id)) {
            entry = this.eventList.get(id);
            assert entry.isActive();
            assert entry.getType().equals(EntryType.EVENT);
            entry.mark();
            this.archiveEvent(id);
            return entry;
        } else if (this.archivedTaskList.containsKey(id)) {
            entry = this.archivedTaskList.get(id);
            assert !entry.isActive();
            entry.mark();
            this.unarchiveTask(id);
            return entry;
        } else if (this.archivedEventList.containsKey(id)) {
            entry = this.archivedEventList.get(id);
            assert !entry.isActive();
            assert entry.getType().equals(EntryType.EVENT);
            entry.mark();
            this.unarchiveEvent(id);
            return entry;
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.CalenderList#find(String)
     */
    @Override
    public CalendarList find(String toSearch, LocalDate startDate, LocalTime startTime, LocalDate endDate,
            LocalTime endTime) {
        CalendarListTemp result = new CalendarListTemp();
        TreeMap<Integer, CalendarEntry> copyTaskList = this.filterList(this.taskList, toSearch, startDate, startTime,
                endDate, endTime);
        TreeMap<Integer, CalendarEntry> copyEventList = this.filterList(this.eventList, toSearch, startDate, startTime,
                endDate, endTime);
        result.setTaskList(copyTaskList);
        result.setEventList(copyEventList);
        return result;
    }

    @Override
    public CalendarList findArchived(String toSearch, LocalDate startDate, LocalTime startTime, LocalDate endDate,
            LocalTime endTime) {
        CalendarListTemp result = new CalendarListTemp();
        TreeMap<Integer, CalendarEntry> copyArchivedTaskList = this.filterList(this.archivedTaskList, toSearch,
                startDate, startTime, endDate, endTime);
        TreeMap<Integer, CalendarEntry> copyArchivedEventList = this.filterList(this.archivedEventList, toSearch,
                startDate, startTime, endDate, endTime);
        result.setArchivedTaskList(copyArchivedTaskList);
        result.setArchivedEventList(copyArchivedEventList);
        return result;
    }

    @Override
    public CalendarList findAll(String toSearch, LocalDate startDate, LocalTime startTime, LocalDate endDate,
            LocalTime endTime) {
        CalendarListTemp result = (CalendarListTemp) this.find(toSearch, startDate, startTime, endDate, endTime);
        CalendarListTemp resultArchived = (CalendarListTemp) this.findArchived(toSearch, startDate, startTime, endDate,
                endTime);
        result.setArchivedTaskList(resultArchived.archivedTaskList);
        result.setArchivedEventList(resultArchived.archivedEventList);
        return result;
    }

    TreeMap<Integer, CalendarEntry> filterList(TreeMap<Integer, CalendarEntry> treeMap, String toSearch,
            LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        TreeMap<Integer, CalendarEntry> copyMap = new TreeMap<>();
        copyMap.putAll(treeMap);
        filterByName(copyMap, toSearch);
        filterByStartDate(copyMap, startDate);
        filterByStartTime(copyMap, startTime);
        filterByEndDate(copyMap, endDate);
        filterByEndTime(copyMap, endTime);
        return copyMap;
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
                ((CalendarEntryImpl) toUpdate).setType(EntryType.DEADLINE);
            }
        }
        // allowed to change floating task to deadline but not the other way
        // around
    }

    void updateEntryPeriod(CalendarEntry toUpdate, TemporalAmount newPeriod) {
        if (newPeriod != null) {
            toUpdate.setPeriod(newPeriod);
        }
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

    void filterByName(TreeMap<Integer, CalendarEntry> treeMap, String toSearch) {
        if (toSearch == null || toSearch.isEmpty()) {
            return;
        }
        for (int i = treeMap.firstKey(); i < treeMap.lastKey() + 1; i++) {
            if (treeMap.containsKey(i)) {
                String entryName = treeMap.get(i).getName();
                if (!entryName.contains(toSearch)) {
                    treeMap.remove(i);
                }
            }
        }
    }

    void filterByStartDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate startDate) {
        if (startDate == null) {
            return;
        }
        for (int i = treeMap.firstKey(); i < treeMap.lastKey() + 1; i++) {
            if (treeMap.containsKey(i)) {
                LocalDate entryStartDate = treeMap.get(i).getStart().toLocalDate();
                if (!entryStartDate.equals(startDate)) {
                    treeMap.remove(i);
                }
            }
        }
    }

    void filterByStartTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime startTime) {
        if (startTime == null) {
            return;
        }
        for (int i = treeMap.firstKey(); i < treeMap.lastKey() + 1; i++) {
            if (treeMap.containsKey(i)) {
                LocalTime entryStartTime = treeMap.get(i).getStartTime();
                if (!entryStartTime.equals(startTime)) {
                    treeMap.remove(i);
                }
            }
        }
    }

    void filterByEndDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate endDate) {
        if (endDate == null) {
            return;
        }
        for (int i = treeMap.firstKey(); i < treeMap.lastKey() + 1; i++) {
            if (treeMap.containsKey(i)) {
                LocalDate entryEndDate = treeMap.get(i).getEnd().toLocalDate();
                if (!entryEndDate.equals(endDate)) {
                    treeMap.remove(i);
                }
            }
        }
    }

    void filterByEndTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime endTime) {
        if (endTime == null) {
            return;
        }
        for (int i = treeMap.firstKey(); i < treeMap.lastKey() + 1; i++) {
            if (treeMap.containsKey(i)) {
                LocalTime entryEndTime = treeMap.get(i).getEndTime();
                if (!entryEndTime.equals(endTime)) {
                    treeMap.remove(i);
                }
            }
        }
    }

    void archiveTask(int id) {
        CalendarEntry task = this.taskList.get(id);
        this.taskList.remove(id);
        this.archivedTaskList.put(id, task);
    }

    void unarchiveTask(int id) {
        CalendarEntry task = this.archivedTaskList.get(id);
        this.archivedTaskList.remove(id);
        this.taskList.put(id, task);
    }

    void archiveEvent(int id) {
        CalendarEntry event = this.eventList.get(id);
        this.eventList.remove(id);
        this.archivedEventList.put(id, event);
    }

    void unarchiveEvent(int id) {
        CalendarEntry event = this.archivedEventList.get(id);
        this.archivedEventList.remove(id);
        this.eventList.put(id, event);
    }

}
