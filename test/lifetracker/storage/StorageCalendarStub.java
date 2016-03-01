package lifetracker.storage;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.Event;
import lifetracker.calendar.Task;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StorageCalendarStub implements CalendarList {

    private List<Task> taskList = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();

    public StorageCalendarStub(){
        taskList.add(new StorageTaskStub("Test Task 1"));
        taskList.add(new StorageTaskStub("Test Task 2 2007-12-03T10:15:30", LocalDateTime.parse("2016-03-14T23:59:59")));

        eventList.add(new StorageEventStub("Test Event 1", LocalDateTime.parse("2016-03-14T23:59:59"), LocalDateTime.parse("2016-03-15T23:59:59")));
        eventList.add(new StorageEventStub("Test Event 2", LocalDateTime.parse("2016-03-14T11:59:59"), LocalDateTime.parse("2016-03-14T23:59:59")));
    }

    @Override
    public List<Task> getTaskList() {
        return taskList;
    }

    @Override
    public List<Event> getEventList() {
        return eventList;
    }

    @Override
    public void addEvent(String name, LocalDateTime start, LocalDateTime end) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void addEvent(Event event) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void addTask(String name, LocalDateTime deadline) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void addTask(Task task) {

        throw new UnsupportedOperationException();
    }

    class StorageEventStub implements Event {

        private String name;
        private LocalDateTime start;
        private LocalDateTime end;

        public StorageEventStub(String name, LocalDateTime start, LocalDateTime end) {
            this.name = name;
            this.start = start;
            this.end = end;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LocalDateTime getStart() {
            return start;
        }

        @Override
        public void setStart(LocalDateTime start) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LocalDateTime getEnd() {
            return end;
        }

        @Override
        public void setEnd(LocalDateTime end) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LocalTime getStartTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public LocalTime getEndTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isToday() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isOngoing() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isOver() {
            throw new UnsupportedOperationException();
        }
    }

    class StorageTaskStub implements Task {

        private String name;
        private LocalDateTime due;

        public StorageTaskStub(String name, LocalDateTime due) {
            this.name = name;
            this.due = due;
        }

        public StorageTaskStub(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LocalDateTime getDeadline() {
            return due;
        }

        @Override
        public void setDeadline(LocalDateTime deadline) {

            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isDueToday() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isOverdue() {
            throw new UnsupportedOperationException();
        }
    }
}
