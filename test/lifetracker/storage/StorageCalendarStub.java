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
        public boolean isFloating() {
            return false;
        }

        @Override
        public boolean isEvent() {
            return false;
        }

        @Override
        public boolean isDeadline() {
            return false;
        }
    }
}
