package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CalendarEntryImpl implements CalendarEntry {

    // variables
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
        if (start != null) {
            assert end != null;
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start date/time cannot be after end date/time!");
            }
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
        if (entryType.equals(EntryType.FLOATING)) {
            return true;
        }

        else if (entryType.equals(EntryType.EVENT)) {
            LocalDateTime now = LocalDateTime.now();
            boolean hasStarted = now.isAfter(startDateTime);
            return (hasStarted && !isOver());
        }

        else {
            assert entryType.equals(EntryType.DEADLINE);
            return (!isOver());
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

}
