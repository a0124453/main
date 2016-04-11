# A0108473Eunused
###### /unused/CalendarEntryImpl.java
``` java
//Replaced with specialized CalendarEntry classes
public class CalendarEntryImpl implements CalendarEntry {

    // variables
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private EntryType entryType;
    private int id;
    private TemporalAmount period;
    private boolean isActive;

    // constructor
    public CalendarEntryImpl(String name, LocalDateTime start, LocalDateTime end, int id) {
        this.setName(name);
        this.setStart(start);
        this.setEnd(end);
        this.id = id;
        this.period = Period.ZERO;
        this.isActive = true;
        if (start != null) {
            assert end != null;
            CalendarEntry.checkStartBeforeEnd(start, end);
            this.entryType = EntryType.EVENT;
        } else if (start == null && end == null) {
            this.entryType = EntryType.FLOATING;
        } else {
            this.entryType = EntryType.DEADLINE;
        }
    }

    // get() and set() functions for variables
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalDateTime getStart() {
        if (this.getType().equals(EntryType.EVENT) && this.isRecurring()) {
            if (this.isOngoing()) {
                return this.getPrevStart();
            } else {
                return this.getNextStart();
            }
        } else {
            return startDateTime;
        }
    }

    @Override
    public void setStart(LocalDateTime start) {
        this.startDateTime = start;
    }

    @Override
    public LocalDateTime getEnd() {
        if (this.getType().equals(EntryType.EVENT) && this.isRecurring()) {
            return this.getNextEnd();
        } else {
            return endDateTime;
        }
    }

    @Override
    public void setEnd(LocalDateTime end) {
        this.endDateTime = end;
    }

    @Override
    public LocalTime getStartTime() {
        return startDateTime == null ? null : startDateTime.toLocalTime();
    }

    @Override
    public LocalTime getEndTime() {
        return endDateTime == null ? null : endDateTime.toLocalTime();
    }

    @Override
    public EntryType getType() {
        return entryType;
    }

    @Override
    public void setPeriod(TemporalAmount period) {
        this.period = period;
    }

    @Override
    public TemporalAmount getPeriod() {
        return period;
    };

    @Override
    public void mark() {
        if (this.getType().equals(EntryType.FLOATING)) {
            this.toggle();
        } else if (this.getType().equals(EntryType.EVENT)) {
            this.toggle();
        } else {
            assert this.getType().equals(EntryType.DEADLINE);
            if (!this.isRecurring()) {
                this.toggle();
            } else if (this.isRecurring()) {
                LocalDateTime newEnd = this.getEnd().plus(this.getPeriod());
                while (newEnd.isBefore(LocalDateTime.now())) {
                    newEnd = newEnd.plus(this.getPeriod());
                }
                this.setEnd(newEnd);
                if (!this.isActive()) {
                    this.toggle();
                }
            }
        }
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isRecurring() {
        return !period.equals(Period.ZERO);
    }

    @Override
    public boolean isToday() {
        if (entryType.equals(EntryType.EVENT)) {
            LocalDate eventStartDay = startDateTime.toLocalDate();
            LocalDate today = LocalDate.now();
            boolean result = today.isEqual(eventStartDay) || this.isOngoing();
            return result;
        }

        else if (entryType.equals(EntryType.FLOATING)) {
            return false;
        }

        else {
            assert entryType.equals(EntryType.DEADLINE);
            LocalDate deadline = endDateTime.toLocalDate();
            LocalDate today = LocalDate.now();
            return deadline.isEqual(today);
        }

    }

    @Override
    public boolean isOngoing() {
        if (this.entryType.equals(EntryType.FLOATING)) {
            return true;
        }

        else if (this.entryType.equals(EntryType.DEADLINE)) {
            return (!isOver());
        } else {
            assert this.entryType.equals(EntryType.EVENT);
            if (this.isRecurring()) {
                return this.getNextEnd().isBefore(this.getNextStart());
            } else {
                LocalDateTime now = LocalDateTime.now();
                boolean hasStarted = now.isAfter(startDateTime);
                return (hasStarted && !isOver());
            }
        }
    }

    @Override
    public boolean isOver() {
        if (!entryType.equals(EntryType.FLOATING)) {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(endDateTime);
        }

        else {
            assert entryType.equals(EntryType.FLOATING);
            return false;
        }

    }

    @Override
    public boolean equals(CalendarEntry entry) {
        if (!this.getType().equals(entry.getType())) {
            return false;
        }
        boolean result = this.getName().equals(entry.getName());
        if (this.getStart() == null) {
            result = (result && entry.getStart() == null);
        } else {
            result = (result && this.getStart().equals(entry.getStart()));
        }
        if (this.getEnd() == null) {
            result = (result && entry.getEnd() == null);
        } else {
            result = (result && this.getEnd().equals(entry.getEnd()));
        }
        return result;
    }

    @Override
    public CalendarEntry copy() {
        String name = this.getName();
        LocalDateTime start = this.getStart();
        LocalDateTime end = this.getEnd();
        int id = this.getId();
        CalendarEntry copy = new CalendarEntryImpl(name, start, end, id);
        return copy;
    }

    void setType(EntryType entryType) {
        this.entryType = entryType;
    }

    void toggle() {
        if (this.isActive()) {
            this.isActive = false;
        } else {
            this.isActive = true;
        }
    }

    private LocalDateTime getNextStart() {
        assert this.getType().equals(EntryType.EVENT);
        assert this.isRecurring();
        LocalDateTime result = this.startDateTime;
        while (result.isBefore(LocalDateTime.now())) {
            result = result.plus(this.getPeriod());
        }
        return result;
    }

    private LocalDateTime getNextEnd() {
        assert this.getType().equals(EntryType.EVENT);
        assert this.isRecurring();
        LocalDateTime result = this.endDateTime;
        while (result.isBefore(LocalDateTime.now())) {
            result = result.plus(this.getPeriod());
        }
        return result;
    }

    private LocalDateTime getPrevStart() {
        assert this.getType().equals(EntryType.EVENT);
        assert this.isRecurring();
        LocalDateTime result = this.getNextStart();
        while (result.isAfter(LocalDateTime.now())) {
            result = result.minus(this.getPeriod());
        }
        return result;
    }

    @SuppressWarnings("unused")
    private LocalDateTime getPrevEnd() {
        assert this.getType().equals(EntryType.EVENT);
        assert this.isRecurring();
        LocalDateTime result = this.getNextEnd();
        while (result.isAfter(LocalDateTime.now())) {
            result = result.minus(this.getPeriod());
        }
        return result;
    }

}
```
###### /unused/CalendarEntryOld.java
``` java
//Replaced by specialized CalendarEntry implementations
public interface CalendarEntryOld {

    public static final String MESSAGE_ERROR_START_AFTER_END = "Start date/time cannot be after end date/time!";
    public static final String MESSAGE_ERROR_ILLEGAL_TYPE_CHANGE_TASK_TO_EVENT = "A task cannot be changed into an event!";

    public enum EntryType {
        FLOATING, DEADLINE, EVENT
    }

    public static void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException(MESSAGE_ERROR_START_AFTER_END);
        }
    }

    // get() and set() functions for variables
    int getId();

    String getName();

    void setName(String name);

    LocalDateTime getStart();

    void setStart(LocalDateTime start);

    LocalDateTime getEnd();

    void setEnd(LocalDateTime end);

    LocalTime getStartTime();

    LocalTime getEndTime();

    EntryType getType();

    void setPeriod(TemporalAmount period);

    TemporalAmount getPeriod();

    void mark();

    boolean isActive();

    boolean isRecurring();

    boolean isToday();

    boolean isOngoing();

    boolean isOver();

    boolean equals(CalendarEntryOld entry);

    CalendarEntryOld copy();

}
```
###### /unused/CalendarListResult.java
``` java
//Replaced by CalendarListImpl itself
public class CalendarListResult extends CalendarListImpl {

    protected void setTaskList(TreeMap<Integer, CalendarEntry> map) {
        this.taskList = map;
    }

    protected void setEventList(TreeMap<Integer, CalendarEntry> map) {
        this.eventList = map;
    }

    protected void setArchivedTaskList(TreeMap<Integer, CalendarEntry> map) {
        this.archivedTaskList = map;
    }

    protected void setArchivedEventList(TreeMap<Integer, CalendarEntry> map) {
        this.archivedEventList = map;
    }

    @Override
    public int add(String name) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime deadline) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime deadline, Period period) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime deadline, Period period, int limit) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime deadline, Period period, LocalDate limitDate) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end, Period period) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end, Period period, int limit) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end, Period period, LocalDate limitDate) {
        return 0;
    }

    @Override
    public int add(CalendarEntry entry) {
        return 0;
    }

    @Override
    public CalendarEntry delete(int id) {
        return null;
    }
}
```
###### /unused/CalendarListTest.java
``` java
//Test no longer works after new Calendar component implemenatation
public class CalendarListTest {

    public static final int NO_OF_FLOATING_TASKS = 3;
    public static final int NO_OF_DEADLINE_TASKS = 3;
    public static final int TOTAL_NO_OF_TASKS = 6;
    public static final int TOTAL_NO_OF_EVENTS = 3;
    public static final int TOTAL_NO_OF_ENTRIES = 9;
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final LocalDateTime TWO_HOURS_LATER = LocalDateTime.now().plusHours(2);

    private static CalendarList testCalendar = new CalendarListImpl();
    private static List<CalendarEntry> expectedTaskList = new ArrayList<>();
    private static List<CalendarEntry> expectedEventList = new ArrayList<>();

    @BeforeClass
    public static void setUpBeforeClass() {
        // add tasks to testCalendar using overloaded add methods
        for (int i = 0; i < TOTAL_NO_OF_ENTRIES; i++) {
            if (i < NO_OF_FLOATING_TASKS) {
                // add(String)
                testCalendar.add(Integer.toString(i));
            } else if (i < TOTAL_NO_OF_TASKS) {
                // add(String, LocalDateTime)
                testCalendar.add(Integer.toString(i), NOW);
            } else {
                // add(String, LocalDateTime, LocalDateTime)
                testCalendar.add(Integer.toString(i), NOW, TWO_HOURS_LATER);
            }
        }
        // set up expectedTaskList and expectedEventList for comparison with
        // lists in testCalendar
        for (int i = 0; i < TOTAL_NO_OF_ENTRIES; i++) {
            if (i < NO_OF_FLOATING_TASKS) {
                CalendarEntry entry = new CalendarEntryImpl(Integer.toString(i), null, null, i);
                expectedTaskList.add(entry);
            } else if (i < TOTAL_NO_OF_TASKS) {
                CalendarEntry entry = new CalendarEntryImpl(Integer.toString(i), null, NOW, i);
                expectedTaskList.add(entry);
            } else {
                CalendarEntry entry = new CalendarEntryImpl(Integer.toString(i), NOW, TWO_HOURS_LATER, i);
                expectedEventList.add(entry);
            }
        }

    }

    @Test
    public void testAdd() {
        // compare names of all tasks in both lists
        for (int i = 0; i < TOTAL_NO_OF_TASKS; i++) {
            assertEquals(expectedTaskList.get(i).getName(), testCalendar.getTaskList().get(i).getName());
        }
        // compare deadlines of all tasks in both lists
        for (int i = 0; i < TOTAL_NO_OF_TASKS; i++) {
            assertEquals(expectedTaskList.get(i).getEnd(), testCalendar.getTaskList().get(i).getEnd());
        }
    }

}
```
###### /unused/CalendarUnused.java
``` java
//Collection of unused functions from features that we did not implement
public class CalendarUnused {
    void filterByStartDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate startDate) {
        if (startDate == null) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            if (entry.getValue().getStart() == null) {
                iterator.remove();
            } else {
                LocalDate entryStartDate = entry.getValue().getStart().toLocalDate();
                if (!entryStartDate.equals(startDate)) {
                    iterator.remove();
                }
            }
        }
    }

    void filterByStartTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime startTime) {
        if (startTime == null) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            if (entry.getValue().getStart() == null) {
                iterator.remove();
            } else {
                LocalTime entryStartTime = entry.getValue().getStart().toLocalTime();
                if (!entryStartTime.equals(startTime)) {
                    iterator.remove();
                }
            }
        }
    }

    void filterByEndDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate endDate) {
        if (endDate == null) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            if (entry.getValue().getEnd() == null) {
                iterator.remove();
            } else {
                LocalDate entryEndDate = entry.getValue().getEnd().toLocalDate();
                if (!entryEndDate.equals(endDate)) {
                    iterator.remove();
                }
            }
        }
    }

    void filterByEndTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime endTime) {
        if (endTime == null) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            if (entry.getValue().getEnd() == null) {
                iterator.remove();
            } else {
                LocalTime entryEndTime = entry.getValue().getEnd().toLocalTime();
                if (!entryEndTime.equals(endTime)) {
                    iterator.remove();
                }
            }
        }
    }

}
```
