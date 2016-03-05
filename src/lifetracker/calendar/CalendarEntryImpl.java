package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CalendarEntryImpl implements CalendarEntry {

    // variables
    public enum EntryType {
        FLOATING, DEADLINE, EVENT
    };

    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private EntryType entryType;
    private int id;

    // constructor
    public CalendarEntryImpl(String name, LocalDateTime start, LocalDateTime end, int id) {
        this.setName(name);
        this.setStart(start);
        this.setEnd(end);
        this.id = id;
        if (start == null && end == null) {
            this.entryType = EntryType.FLOATING;
        } else if (start == null && end != null) {
            this.entryType = EntryType.DEADLINE;
        } else if (start != null && end != null) {
            this.entryType = EntryType.EVENT;
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
        return startDateTime;
    }

    @Override
    public void setStart(LocalDateTime start) {
        this.startDateTime = start;
    }

    @Override
    public LocalDateTime getEnd() {
        return endDateTime;
    }

    @Override
    public void setEnd(LocalDateTime end) {
        this.endDateTime = end;
    }

    @Override
    public LocalTime getStartTime() {
        return startDateTime.toLocalTime();
    }

    @Override
    public LocalTime getEndTime() {
        return endDateTime.toLocalTime();
    }

    @Override
    public boolean isToday() {
        if (entryType.equals(EntryType.EVENT)) {
            LocalDate eventStartDay = startDateTime.toLocalDate();
            LocalDate eventEndDay = endDateTime.toLocalDate();
            LocalDate today = LocalDate.now();
            boolean result = today.isEqual(eventStartDay);
            result = result || (today.isAfter(eventStartDay) && today.isBefore(eventEndDay));
            return result;
        }

        if (entryType.equals(EntryType.FLOATING)) {
            return false;
        }

        if (entryType.equals(EntryType.DEADLINE)) {
            LocalDate eventStartDay = endDateTime.toLocalDate();
            LocalDate today = LocalDate.now();
            return eventStartDay.isEqual(today);
        }

        return false;
    }

    @Override
    public boolean isOngoing() {
        if (entryType.equals(EntryType.FLOATING)) {
            return true;
        }

        if (entryType.equals(EntryType.EVENT)) {
            LocalDateTime now = LocalDateTime.now();
            boolean hasStarted = now.isAfter(startDateTime);
            return (hasStarted && !isOver());
        }

        if (entryType.equals(EntryType.DEADLINE)) {
            return (!isOver());
        }

        return false;
    }

    @Override
    public boolean isOver() {
        if (entryType.equals(EntryType.FLOATING))
            return false;

        else {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(endDateTime);
        }
    }

}
