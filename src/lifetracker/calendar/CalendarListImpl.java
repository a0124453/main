package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import lifetracker.calendar.visitor.EntryToDeadlineTaskVisitor;
import lifetracker.calendar.visitor.EntryToEventVisitor;
import lifetracker.calendar.visitor.EntryToGenericTaskVisitor;
import lifetracker.calendar.visitor.EntryToRecurringEventVisitor;
import lifetracker.calendar.visitor.EntryToRecurringTaskVisitor;
import lifetracker.calendar.visitor.EntryVisitor;
import lifetracker.calendar.visitor.MarkVisitor;
import lifetracker.calendar.visitor.OldNewEntryPair;

public class CalendarListImpl implements CalendarList {

    private static final String ERROR_EMPTY_NAME = "Task/Event's name cannot be empty!";

    // variables
    protected TreeMap<Integer, CalendarEntry> taskList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> eventList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> archivedTaskList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> archivedEventList = new TreeMap<>();

    // get() and set() functions for variables

    @Override
    public List<CalendarEntry> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public List<CalendarEntry> getEventList() {
        List<CalendarEntry> list = new ArrayList<>(eventList.values());
        List<CalendarEntry> sortedList = sortByDateTime(CalendarProperty.END, list);
        sortedList = sortByDateTime(CalendarProperty.START, sortedList);
        return sortedList;
    }

    @Override
    public List<CalendarEntry> getArchivedTaskList() {
        return new ArrayList<>(archivedTaskList.values());
    }

    @Override
    public List<CalendarEntry> getArchivedEventList() {
        return new ArrayList<>(archivedEventList.values());
    }

    @Override
    public int add(String name) {
        return add(new GenericEntry(name));
    }

    @Override
    public int add(String name, LocalDateTime deadline) {
        return add(new DeadlineTask(name, deadline));
    }

    @Override
    public int add(String name, LocalDateTime deadline, Period period) {
        return add(new RecurringTask(name, deadline, period));
    }

    @Override
    public int add(String name, LocalDateTime deadline, Period period, int limit) {
        return add(new RecurringTask(name, deadline, period, limit));
    }

    @Override
    public int add(String name, LocalDateTime deadline, Period period, LocalDate limitDate) {
        return add(new RecurringTask(name, deadline, period, limitDate));
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end) {
        return add(new Event(name, start, end));
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end, Period period) {
        return add(new RecurringEvent(name, start, end, period));
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end, Period period, int limit) {
        return add(new RecurringEvent(name, start, end, period, limit));
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end, Period period, LocalDate limitDate) {
        return add(new RecurringEvent(name, start, end, period, limitDate));
    }

    @Override
    public int add(CalendarEntry entry) {
        assert entry != null;
        assert entry.getName() != null;

        if (entry.getName().isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_NAME);
        }

        if (!isValidId(entry.getId())) {
            entry.setId(getNextId());
        }
        if (entry.isProperty(CalendarProperty.ACTIVE)) {
            if (entry.getDateTime(CalendarProperty.START) == null) {
                taskList.put(entry.getId(), entry);
            } else {
                eventList.put(entry.getId(), entry);
            }
        } else {
            if (entry.getDateTime(CalendarProperty.START) == null) {
                archivedTaskList.put(entry.getId(), entry);
            } else {
                archivedEventList.put(entry.getId(), entry);
            }
        }
        return entry.getId();
    }

    @Override
    public CalendarEntry delete(int id) {
        if (taskList.containsKey(id)) {
            return taskList.remove(id);
        } else if (eventList.containsKey(id)) {
            return eventList.remove(id);
        } else if (archivedEventList.containsKey(id)) {
            return archivedEventList.remove(id);
        } else if (archivedTaskList.containsKey(id)) {
            return archivedTaskList.remove(id);
        }
        throw new IllegalArgumentException(String.format(ERROR_INVALID_ID, id));
    }

    @Override
    public CalendarEntry updateToGeneric(int id, String newName, boolean isConvertForced) {
        EntryToGenericTaskVisitor visitor = new EntryToGenericTaskVisitor(newName, isConvertForced);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry updateToDeadline(int id, String newName, LocalDateTime newDeadline, boolean isConvertForced) {
        EntryToDeadlineTaskVisitor visitor = new EntryToDeadlineTaskVisitor(newName, newDeadline, isConvertForced);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry updateToEvent(int id, String newName, LocalDateTime newStartTime, LocalDateTime newEndTime,
            boolean isConvertForced) {
        EntryToEventVisitor visitor = new EntryToEventVisitor(newName, newStartTime, newEndTime, isConvertForced);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry updateToRecurringTask(int id, String newName, LocalDateTime newDeadLine, Period newPeriod,
            boolean isLimitKept, boolean isConvertForced) {
        EntryToRecurringTaskVisitor visitor = new EntryToRecurringTaskVisitor(newName, newDeadLine, newPeriod,
                isLimitKept, isConvertForced);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry updateToRecurringTask(int id, String newName, LocalDateTime newDeadLine, Period newPeriod,
            int newLimit, boolean isConvertForced) {
        EntryToRecurringTaskVisitor visitor = new EntryToRecurringTaskVisitor(newName, newDeadLine, newPeriod, newLimit,
                isConvertForced);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry updateToRecurringTask(int id, String newName, LocalDateTime newDeadLine, Period newPeriod,
            LocalDate newLimitDate, boolean isConvertForced) {
        EntryToRecurringTaskVisitor visitor = new EntryToRecurringTaskVisitor(newName, newDeadLine, newPeriod,
                newLimitDate, isConvertForced);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry updateToRecurringEvent(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            Period newPeriod, boolean isLimitKept) {
        EntryToRecurringEventVisitor visitor = new EntryToRecurringEventVisitor(newName, newStart, newEnd, newPeriod,
                isLimitKept);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry updateToRecurringEvent(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            Period newPeriod, int newLimit) {
        EntryToRecurringEventVisitor visitor = new EntryToRecurringEventVisitor(newName, newStart, newEnd, newPeriod,
                newLimit);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry updateToRecurringEvent(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            Period newPeriod, LocalDate newLimit) {
        EntryToRecurringEventVisitor visitor = new EntryToRecurringEventVisitor(newName, newStart, newEnd, newPeriod,
                newLimit);
        return updateWithVisitor(visitor, id);
    }

    @Override
    public CalendarEntry update(CalendarEntry newEntry) {
        CalendarEntry oldEntry = null;
        if (isPresent(newEntry.getId())) {
            oldEntry = delete(newEntry.getId());
        }

        add(newEntry);

        return oldEntry;
    }

    @Override
    public OldNewEntryPair mark(int id) {
        CalendarEntry entryToMark = delete(id);

        MarkVisitor visitor = new MarkVisitor();
        OldNewEntryPair pair = entryToMark.accept(visitor);

        add(entryToMark);

        if(pair.newEntry != null){
            update(pair.newEntry);
        }

        return pair;
    }

    @Override
    public CalendarList findByName(String toSearch) {
        CalendarListTemp result = new CalendarListTemp();
        TreeMap<Integer, CalendarEntry> copyTaskList = new TreeMap<>();
        copyTaskList.putAll(this.taskList);
        filterByName(copyTaskList, toSearch);
        TreeMap<Integer, CalendarEntry> copyEventList = new TreeMap<>();
        copyEventList.putAll(this.eventList);
        filterByName(copyEventList, toSearch);
        result.setTaskList(copyTaskList);
        result.setEventList(copyEventList);
        return result;
    }

    @Override
    public CalendarList findArchivedByName(String toSearch) {
        CalendarListTemp result = new CalendarListTemp();
        result.setTaskList(this.archivedTaskList);
        result.setEventList(this.archivedEventList);
        return result.findByName(toSearch);
    }

    @Override
    public CalendarList findAllByName(String toSearch) {
        CalendarListTemp result = (CalendarListTemp) this.findByName(toSearch);
        CalendarListTemp resultArchived = (CalendarListTemp) this.findArchivedByName(toSearch);
        result.taskList.putAll(resultArchived.taskList);
        result.eventList.putAll(resultArchived.eventList);
        return result;
    }

    private CalendarEntry updateWithVisitor(EntryVisitor<OldNewEntryPair> visitor, int id) {
        CalendarEntry entryToEdit = delete(id);
        OldNewEntryPair pair = entryToEdit.accept(visitor);

        add(pair.newEntry);
        return pair.oldEntry;
    }

    TreeMap<Integer, CalendarEntry> filterList(TreeMap<Integer, CalendarEntry> treeMap, String toSearch) {
        TreeMap<Integer, CalendarEntry> copyMap = new TreeMap<>();
        copyMap.putAll(treeMap);
        filterByName(copyMap, toSearch);
        return copyMap;
    }

    void filterByName(TreeMap<Integer, CalendarEntry> treeMap, String toSearch) {
        if (toSearch == null || toSearch.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            String entryName = entry.getValue().getName();
            if (containsAnyWord(entryName, toSearch)) {
                iterator.remove();
            }
        }
    }

    //    void filterByStartDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate startDate) {
    //        if (startDate == null) {
    //            return;
    //        }
    //        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
    //        while (iterator.hasNext()) {
    //            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
    //            if (entry.getValue().getStart() == null) {
    //                iterator.remove();
    //            } else {
    //                LocalDate entryStartDate = entry.getValue().getStart().toLocalDate();
    //                if (!entryStartDate.equals(startDate)) {
    //                    iterator.remove();
    //                }
    //            }
    //        }
    //    }
    //
    //    void filterByStartTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime startTime) {
    //        if (startTime == null) {
    //            return;
    //        }
    //        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
    //        while (iterator.hasNext()) {
    //            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
    //            if (entry.getValue().getStart() == null) {
    //                iterator.remove();
    //            } else {
    //                LocalTime entryStartTime = entry.getValue().getStart().toLocalTime();
    //                if (!entryStartTime.equals(startTime)) {
    //                    iterator.remove();
    //                }
    //            }
    //        }
    //    }
    //
    //    void filterByEndDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate endDate) {
    //        if (endDate == null) {
    //            return;
    //        }
    //        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
    //        while (iterator.hasNext()) {
    //            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
    //            if (entry.getValue().getEnd() == null) {
    //                iterator.remove();
    //            } else {
    //                LocalDate entryEndDate = entry.getValue().getEnd().toLocalDate();
    //                if (!entryEndDate.equals(endDate)) {
    //                    iterator.remove();
    //                }
    //            }
    //        }
    //    }
    //
    //    void filterByEndTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime endTime) {
    //        if (endTime == null) {
    //            return;
    //        }
    //        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
    //        while (iterator.hasNext()) {
    //            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
    //            if (entry.getValue().getEnd() == null) {
    //                iterator.remove();
    //            } else {
    //                LocalTime entryEndTime = entry.getValue().getEnd().toLocalTime();
    //                if (!entryEndTime.equals(endTime)) {
    //                    iterator.remove();
    //                }
    //            }
    //        }
    //    }

    private int getNextId() {
        int taskMax = this.taskList.isEmpty() ? BASE_ID : this.taskList.lastKey();
        int eventMax = this.eventList.isEmpty() ? BASE_ID : this.eventList.lastKey();
        int archivedTaskMax = this.archivedTaskList.isEmpty() ? BASE_ID : this.archivedTaskList.lastKey();
        int archivedEventMax = this.archivedEventList.isEmpty() ? BASE_ID : this.archivedEventList.lastKey();
        int idToSet = Math.max(taskMax, eventMax);
        idToSet = Math.max(idToSet, archivedTaskMax);
        idToSet = Math.max(idToSet, archivedEventMax);
        idToSet += 1;
        return idToSet;
    }

    boolean containsAnyWord(String entryName, String toSearch) {
        String[] arrayOfWords = toSearch.split(" ");
        for (String word : arrayOfWords) {
            if (StringUtils.containsIgnoreCase(entryName, word)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidId(int id) {
        return id > BASE_ID && !isPresent(id);
    }

    private boolean isPresent(int id){
        boolean isPresent = taskList.containsKey(id);
        isPresent |= eventList.containsKey(id);
        isPresent |= archivedTaskList.containsKey(id);
        isPresent |= archivedEventList.containsKey(id);
        return isPresent;
    }

    private List<CalendarEntry> sortByDateTime(CalendarProperty property, List<CalendarEntry> list) {
        Comparator<CalendarEntry> comparator = (CalendarEntry entry1, CalendarEntry entry2) -> {
            LocalDateTime date1 = entry1.getDateTime(property);
            LocalDateTime date2 = entry2.getDateTime(property);
            if (date1 == null && date2 != null) {
                return 1;
            } else if (date1 != null && date2 == null) {
                return -1;
            } else if (date1.isBefore(date2)) {
                return 1;
            } else if (date1.isAfter(date2)) {
                return -1;
            } else {
                return 0;
            }
        };
        Collections.sort(list, comparator);
        return list;
    }

}