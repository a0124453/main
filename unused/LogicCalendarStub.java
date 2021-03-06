package lifetracker.logic;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//@@author A0149467N-unused

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

    class CalendarEntryStub implements CalendarEntry {

        private String name;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int id;

        public CalendarEntryStub(String name, LocalDateTime startTime, LocalDateTime endTime) {
            this.name = name;
            this.startTime = startTime;
            this.endTime = endTime;
        }

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
            return startTime == null ? null : startTime.toLocalTime();
        }

        @Override
        public LocalTime getEndTime() {
            return endTime == null ? null : endTime.toLocalTime();
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
    }
}
