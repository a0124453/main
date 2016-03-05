package lifetracker.storage;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;
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
    public void add(String name) {

    }

    @Override
    public void add(String name, LocalDateTime due) {

    }

    @Override
    public void add(String name, LocalDateTime start, LocalDateTime end) {

    }

    private void populateData() {
        taskList.add(new StorageTaskStub("Test Task 1"));
        taskList.add(
                new StorageTaskStub("Test Task 2 2007-12-03T10:15:30", LocalDateTime.parse("2016-03-14T23:59:59")));

        eventList.add(new StorageEventStub("Test Event 1", LocalDateTime.parse("2016-03-14T23:59:59"),
                LocalDateTime.parse("2016-03-15T23:59:59")));
        eventList.add(new StorageEventStub("Test Event 2", LocalDateTime.parse("2016-03-14T11:59:59"),
                LocalDateTime.parse("2016-03-14T23:59:59")));
    }
}
