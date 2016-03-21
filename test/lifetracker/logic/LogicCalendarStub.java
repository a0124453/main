package lifetracker.logic;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LogicCalendarStub implements CalendarList {

    private List<CalendarEntry> taskList = new ArrayList<>();
    private List<CalendarEntry> eventList = new ArrayList<>();

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
    public boolean delete(int id) {

        return false;
    }

    @Override
    public boolean update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd) {

        return false;
    }

    @Override
    public List<CalendarEntry> list(String toSearch) {

        return null;
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
            return false;
        }
    }
}
