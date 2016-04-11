# A0091173Junused
###### /unused/StorageCalendarStub.java
``` java
//This class was a test stub that was not neccesssary after storage was decoupled from calendar.
package lifetracker.storage;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StorageCalendarStub implements CalendarList {

    private List<CalendarEntry> taskList = new ArrayList<>();
    private List<CalendarEntry> eventList = new ArrayList<>();

    /**
     * Creates a new StorageCalendarStub, populating with data only if {@code populate} is {@code true}.
     *
     * @param populate If the stub should be populated with dummy data.
     */
    public StorageCalendarStub(boolean populate) {
        if (populate) {
            populateData();
        }
    }

    @Override
    public List<CalendarEntry> getTaskList() {
        return taskList;
    }

    @Override
    public List<CalendarEntry> getEventList() {
        return eventList;
    }

    @Override
    public int add(String name) {
        taskList.add(new CalendarEntryStub(name, null, null));
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime due) {
        taskList.add(new CalendarEntryStub(name, null, due));
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end) {
        eventList.add(new CalendarEntryStub(name, start, end));
        return 0;
    }

    @Override
    public CalendarEntry delete(int id) {

        return null;
    }

    @Override
    public CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd) {

        return null;
    }

    @Override
    public List<CalendarEntry> list(String toSearch) {

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StorageCalendarStub))
            return false;

        StorageCalendarStub that = (StorageCalendarStub) o;

        return getTaskList().equals(that.getTaskList()) && getEventList().equals(that.getEventList());

    }

    @Override
    public int hashCode() {
        int result = getTaskList().hashCode();
        result = 31 * result + getEventList().hashCode();
        return result;
    }

    private void populateData() {
        taskList.add(new CalendarEntryStub("Test Task 1", null, null));
        taskList.add(
                new CalendarEntryStub("Test Task 2", null,
                        LocalDateTime.parse("2016-03-14T23:59:59")));

        eventList.add(new CalendarEntryStub("Test Event 1", LocalDateTime.parse("2016-03-14T23:59:59"),
                LocalDateTime.parse("2016-03-15T23:59:59")));
        eventList.add(new CalendarEntryStub("Test Event 2", LocalDateTime.parse("2016-03-14T11:59:59"),
                LocalDateTime.parse("2016-03-14T23:59:59")));
    }

    class CalendarEntryStub implements CalendarEntry {

        private String name;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public CalendarEntryStub(String name, LocalDateTime startTime, LocalDateTime endTime) {
            this.name = name;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {

        }

        @Override
        public LocalDateTime getStart() {
            return startTime;
        }

        @Override
        public void setStart(LocalDateTime start) {

        }

        @Override
        public LocalDateTime getEnd() {
            return endTime;
        }

        @Override
        public void setEnd(LocalDateTime end) {

        }

        @Override
        public LocalTime getStartTime() {
            return null;
        }

        @Override
        public LocalTime getEndTime() {
            return null;
        }

        @Override
        public EntryType getType() {
            return null;
        }

        @Override
        public boolean isToday() {
            return false;
        }

        @Override
        public boolean isOngoing() {
            return false;
        }

        @Override
        public boolean isOver() {
            return false;
        }

        @Override
        public boolean equals(CalendarEntry entry) {
            return equals((Object) entry);
        }

        @Override
        public CalendarEntry copy() {
            return null;
        }

        //Auto-generated function
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof CalendarEntryStub))
                return false;

            CalendarEntryStub that = (CalendarEntryStub) o;

            return getName().equals(that.getName()) && (getStartTime() != null ?
                    getStartTime().equals(that.getStartTime()) :
                    that.getStartTime() == null && (getEndTime() != null ?
                            getEndTime().equals(that.getEndTime()) :
                            that.getEndTime() == null));

        }

        @Override
        public int hashCode() {
            int result = getName().hashCode();
            result = 31 * result + (getStartTime() != null ? getStartTime().hashCode() : 0);
            result = 31 * result + (getEndTime() != null ? getEndTime().hashCode() : 0);
            return result;
        }
    }
}
```
###### /unused/UI.java
``` java
//Replaced by GUI
public class UI {

    private static final String MESSAGE_WELCOME = "Welcome to the Life Tracker, Spend less time planning so you always know what's next.";
    private static final String MESSAGE_INPUT = "Command: ";

    private static final String[] EVENT_HEADERS = {"ID", "Name", "Active?", "Start", "End", "Every"};
    private static final String[] TASK_HEADERS = {"ID", "Name", "Active?","Due", "Every"};

    private static final String EVENT_TITLE = "Events: ";
    private static final String TASK_TITLE = "Tasks: ";

    static Scanner scanner = new Scanner(System.in);

    public UI(Logic l) {
        assert l != null;

        welcomeMessage();
        executeUntilExit(l);
    }

    public void welcomeMessage() {
        System.out.println(MESSAGE_WELCOME);
    }

    public void executeUntilExit(Logic l) {
        assert l != null;

        ExecuteResult result;

        do {
            System.out.printf(MESSAGE_INPUT);
            String input = scanner.nextLine();

            result = l.executeCommand(input);

            if (result.getType() == ExecuteResult.CommandType.DISPLAY) {

               // printTable(EVENT_TITLE, EVENT_HEADERS, result.getEventList());
               // printTable(TASK_TITLE, TASK_HEADERS, result.getTaskList());

            }

            if(result.getType() != ExecuteResult.CommandType.EXIT) {
                System.out.println(result.getComment());
                System.out.println();
            }

        } while (result.getType() != ExecuteResult.CommandType.EXIT);
    }

    private void printTable(String title ,String[] headers ,List<List<String>> data) {

        TextTable displayTable = new TextTable(headers, listToArray(data));

        System.out.println(title);
        displayTable.printTable();
        System.out.println();
    }

    private String[][] listToArray(List<List<String>> list) {
        String[][] outputArray = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {

            String[] row = new String[list.get(i).size()];
            outputArray[i] = list.get(i).toArray(row);
        }

        return outputArray;
    }
}
```
###### /unused/EditCommand.java
``` java
//Was replaced with numerous subclasses
public class EditCommand extends CommandObject {

    private static final String MESSAGE_EDITED = "\"%1$d\" is edited.";
    private static final String MESSAGE_UNDO = "Changes to \"%1$d\" have been undone.";
    private static final String MESSAGE_NOT_FOUND = "%1$d cannot be found!";

    private final int entryID;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Period recurringTime;

    private CalendarEntry oldEntry;

    public EditCommand(int entryID, String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringTime) {
        this.entryID = entryID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurringTime = recurringTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        oldEntry = calendar.update(entryID, name, startTime, endTime, recurringTime);

        if (oldEntry == null) {
            throw new IllegalArgumentException(String.format(MESSAGE_NOT_FOUND, entryID));
        }

        setComment(String.format(MESSAGE_EDITED, entryID));

        return super.execute(calendar);
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        calendar.update(entryID, oldEntry.getName(), oldEntry.getDateTime(CalendarProperty.START), oldEntry.getDateTime(CalendarProperty.END), oldEntry.getPeriod());

        setComment(String.format(MESSAGE_UNDO, entryID));

        return super.undo(calendar);
    }
}
```
