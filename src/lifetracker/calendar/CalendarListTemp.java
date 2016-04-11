package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.TreeMap;

public class CalendarListTemp extends CalendarListImpl {

    void setTaskList(TreeMap<Integer, CalendarEntry> map) {
        this.taskList = map;
    }

    void setEventList(TreeMap<Integer, CalendarEntry> map) {
        this.eventList = map;
    }

    void setArchivedTaskList(TreeMap<Integer, CalendarEntry> map) {
        this.archivedTaskList = map;
    }

    void setArchivedEventList(TreeMap<Integer, CalendarEntry> map) {
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
