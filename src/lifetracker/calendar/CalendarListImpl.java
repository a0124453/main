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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    private static final double WORD_SIMILARITY_THRESHOLD_INDEX = 0.85;

    // variables
    protected TreeMap<Integer, CalendarEntry> taskList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> eventList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> archivedTaskList = new TreeMap<>();
    protected TreeMap<Integer, CalendarEntry> archivedEventList = new TreeMap<>();

    // get() and set() functions for variables

    @Override
    public List<CalendarEntry> getTaskList() {
        List<CalendarEntry> active = taskList.values().stream()
                .filter(entry -> entry.isProperty(CalendarProperty.ACTIVE)).collect(Collectors.toList());

        sortByDateTime(CalendarProperty.END, active);

        List<CalendarEntry> nonActive = taskList.values().stream()
                .filter(entry -> !entry.isProperty(CalendarProperty.ACTIVE)).collect(Collectors.toList());

        sortReverseByDateTime(CalendarProperty.END, nonActive);
        active.addAll(nonActive);

        return active;
    }

    @Override
    public List<CalendarEntry> getEventList() {

        List<CalendarEntry> active = eventList.values().stream()
                .filter(entry -> entry.isProperty(CalendarProperty.ACTIVE)).collect(Collectors.toList());

        sortByDateTime(CalendarProperty.END, active);
        sortByDateTime(CalendarProperty.START, active);

        List<CalendarEntry> nonActive = eventList.values().stream()
                .filter(entry -> !entry.isProperty(CalendarProperty.ACTIVE)).collect(Collectors.toList());

        sortReverseByDateTime(CalendarProperty.END, nonActive);
        sortReverseByDateTime(CalendarProperty.START, nonActive);

        active.addAll(nonActive);

        return active;
    }

    @Override
    public List<CalendarEntry> getArchivedTaskList() {
        List<CalendarEntry> list = new ArrayList<>(archivedTaskList.values());
        return sortReverseByDateTime(CalendarProperty.END, list);
    }

    @Override
    public List<CalendarEntry> getArchivedEventList() {
        List<CalendarEntry> list = new ArrayList<>(archivedEventList.values());
        List<CalendarEntry> sortedList = sortReverseByDateTime(CalendarProperty.END, list);
        sortedList = sortReverseByDateTime(CalendarProperty.START, sortedList);
        return sortedList;
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

        if (pair.newEntry != null) {
            update(pair.newEntry);
        }

        return pair;
    }

    @Override
    public CalendarEntry get(int id) {
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        } else if (eventList.containsKey(id)) {
            return eventList.get(id);
        } else if (archivedEventList.containsKey(id)) {
            return archivedEventList.get(id);
        } else if (archivedTaskList.containsKey(id)) {
            return archivedTaskList.get(id);
        }
        throw new IllegalArgumentException(String.format(ERROR_INVALID_ID, id));
    }

    @Override
    public CalendarList findByName(String toSearch) {
        CalendarListResult result = new CalendarListResult();
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
        CalendarListResult result = new CalendarListResult();
        result.setTaskList(this.archivedTaskList);
        result.setEventList(this.archivedEventList);
        return result.findByName(toSearch);
    }

    @Override
    public CalendarList findAllByName(String toSearch) {
        CalendarListResult result = new CalendarListResult();

        TreeMap<Integer, CalendarEntry> combinedTask = new TreeMap<>(taskList);
        combinedTask.putAll(archivedTaskList);
        result.setTaskList(combinedTask);

        TreeMap<Integer, CalendarEntry> combinedEvent = new TreeMap<>(eventList);
        combinedEvent.putAll(archivedEventList);
        result.setEventList(combinedEvent);

        return result.findByName(toSearch);
    }

    @Override
    public CalendarList findToday() {
        CalendarListResult result = new CalendarListResult();

        Map<Integer, CalendarEntry> todayTasks = taskList.entrySet().stream()
                .filter(e -> e.getValue().isProperty(CalendarProperty.TODAY))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        result.setTaskList(new TreeMap<>(todayTasks));

        Map<Integer, CalendarEntry> todayEvents = eventList.entrySet().stream()
                .filter(e -> e.getValue().isProperty(CalendarProperty.TODAY))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        result.setEventList(new TreeMap<>(todayEvents));

        return result;
    }

    private CalendarEntry updateWithVisitor(EntryVisitor<OldNewEntryPair> visitor, int id) {
        CalendarEntry entryToEdit = delete(id);
        try {
            OldNewEntryPair pair = entryToEdit.accept(visitor);
            add(pair.newEntry);
            return pair.oldEntry;
        } catch (IllegalArgumentException ex) {
            add(entryToEdit);
            throw ex;
        }
    }

    private TreeMap<Integer, CalendarEntry> filterList(TreeMap<Integer, CalendarEntry> treeMap, String toSearch) {
        TreeMap<Integer, CalendarEntry> copyMap = new TreeMap<>();
        copyMap.putAll(treeMap);
        filterByName(copyMap, toSearch);
        return copyMap;
    }

    private void filterByName(TreeMap<Integer, CalendarEntry> treeMap, String toSearch) {
        if (toSearch == null || toSearch.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            String entryName = entry.getValue().getName();
            if (!containsAnyWord(entryName, toSearch)) {
                iterator.remove();
            }
        }
    }

    // void filterByStartDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate
    // startDate) {
    // if (startDate == null) {
    // return;
    // }
    // Iterator<Map.Entry<Integer, CalendarEntry>> iterator =
    // treeMap.entrySet().iterator();
    // while (iterator.hasNext()) {
    // Map.Entry<Integer, CalendarEntry> entry = iterator.next();
    // if (entry.getValue().getStart() == null) {
    // iterator.remove();
    // } else {
    // LocalDate entryStartDate = entry.getValue().getStart().toLocalDate();
    // if (!entryStartDate.equals(startDate)) {
    // iterator.remove();
    // }
    // }
    // }
    // }
    //
    // void filterByStartTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime
    // startTime) {
    // if (startTime == null) {
    // return;
    // }
    // Iterator<Map.Entry<Integer, CalendarEntry>> iterator =
    // treeMap.entrySet().iterator();
    // while (iterator.hasNext()) {
    // Map.Entry<Integer, CalendarEntry> entry = iterator.next();
    // if (entry.getValue().getStart() == null) {
    // iterator.remove();
    // } else {
    // LocalTime entryStartTime = entry.getValue().getStart().toLocalTime();
    // if (!entryStartTime.equals(startTime)) {
    // iterator.remove();
    // }
    // }
    // }
    // }
    //
    // void filterByEndDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate
    // endDate) {
    // if (endDate == null) {
    // return;
    // }
    // Iterator<Map.Entry<Integer, CalendarEntry>> iterator =
    // treeMap.entrySet().iterator();
    // while (iterator.hasNext()) {
    // Map.Entry<Integer, CalendarEntry> entry = iterator.next();
    // if (entry.getValue().getEnd() == null) {
    // iterator.remove();
    // } else {
    // LocalDate entryEndDate = entry.getValue().getEnd().toLocalDate();
    // if (!entryEndDate.equals(endDate)) {
    // iterator.remove();
    // }
    // }
    // }
    // }
    //
    // void filterByEndTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime
    // endTime) {
    // if (endTime == null) {
    // return;
    // }
    // Iterator<Map.Entry<Integer, CalendarEntry>> iterator =
    // treeMap.entrySet().iterator();
    // while (iterator.hasNext()) {
    // Map.Entry<Integer, CalendarEntry> entry = iterator.next();
    // if (entry.getValue().getEnd() == null) {
    // iterator.remove();
    // } else {
    // LocalTime entryEndTime = entry.getValue().getEnd().toLocalTime();
    // if (!entryEndTime.equals(endTime)) {
    // iterator.remove();
    // }
    // }
    // }
    // }

    private int getNextId() {
        int taskMax = getLastKey(this.taskList);
        int eventMax = getLastKey(this.eventList);
        int archivedTaskMax = getLastKey(this.archivedTaskList);
        int archivedEventMax = getLastKey(this.archivedEventList);
        int idToSet = maxId(taskMax, eventMax, archivedTaskMax, archivedEventMax) + 1;
        return idToSet;
    }

    private int getLastKey(TreeMap<Integer, CalendarEntry> treeMap) {
        int key = treeMap.isEmpty() ? BASE_ID : treeMap.lastKey();
        return key;
    }

    private int maxId(int taskMax, int eventMax, int archivedTaskMax, int archivedEventMax) {
        int maxId = Math.max(taskMax, eventMax);
        maxId = Math.max(maxId, archivedTaskMax);
        maxId = Math.max(maxId, archivedEventMax);
        return maxId;
    }

    private boolean isValidId(int id) {
        return id > BASE_ID && !isPresent(id);
    }

    private boolean isPresent(int id) {
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

            if (date1 == null && date2 == null) {
                return 0;
            } else if (date1 == null) {
                return 1;
            } else if (date2 == null) {
                return -1;
            } else if (date1.isBefore(date2)) {
                return -1;
            } else if (date1.isAfter(date2)) {
                return 1;
            } else {
                return 0;
            }
        };
        Collections.sort(list, comparator);
        return list;
    }

    private List<CalendarEntry> sortReverseByDateTime(CalendarProperty property, List<CalendarEntry> list) {
        List<CalendarEntry> sortedList = new ArrayList<CalendarEntry>();
        sortedList = sortByDateTime(property, list);
        Collections.reverse(sortedList);

        Comparator<CalendarEntry> comparator = (CalendarEntry entry1, CalendarEntry entry2) -> {
            LocalDateTime date1 = entry1.getDateTime(property);
            LocalDateTime date2 = entry2.getDateTime(property);

            if (date1 == null && date2 == null) {
                return 0;
            } else if (date1 == null) {
                return 1;
            } else if (date2 == null) {
                return -1;
            } else {
                return 0;
            }
        };

        Collections.sort(sortedList, comparator);

        return sortedList;
    }

    private boolean containsAnyWord(String entryName, String toSearch) {
        String[] toSearchArray = toSearch.split(" ");
        String[] entryNameArray = entryName.split(" ");
        for (String word1 : toSearchArray) {
            for (String word2 : entryNameArray) {
                if (isSimilar(word1, word2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSimilar(String word1, String word2) {
        return StringUtils.getJaroWinklerDistance(word1, word2) > WORD_SIMILARITY_THRESHOLD_INDEX;
    }

}