package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.TreeMap;

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
    public int add(String name, LocalDateTime start, LocalDateTime end) {
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end, Period period) {
        return 0;
    }

    @Override
    public CalendarEntry delete(int id) {
        return null;
    }
}
