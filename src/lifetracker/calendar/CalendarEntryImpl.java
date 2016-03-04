package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CalendarEntryImpl implements CalendarEntry {

    // variables
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    // constructor
    public CalendarEntryImpl(String name, LocalDateTime start, LocalDateTime end) {
        this.setName(name);
        this.setStart(start);
        this.setEnd(end);
    }

    // get() and set() functions for variables
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
        if (isEvent()) {
            LocalDate eventStartDay = startDateTime.toLocalDate();
            LocalDate eventEndDay = endDateTime.toLocalDate();
            LocalDate today = LocalDate.now();
            return (today.isAfter(eventStartDay) && today.isBefore(eventEndDay));
        }

        if (isFloating()) {
            return false;
        }

        if (isDeadline()) {
            LocalDate eventStartDay = endDateTime.toLocalDate();
            LocalDate today = LocalDate.now();
            return eventStartDay.equals(today);
        }

        return false;
    }

    @Override
    public boolean isOngoing() {
        if (isFloating()) {
            return true;
        }

        if (isEvent()) {
            LocalDateTime now = LocalDateTime.now();
            boolean hasStarted = now.isAfter(startDateTime);
            return (hasStarted && !isOver());
        }

        if (isDeadline()) {
            return (!isOver());
        }

        return false;
    }

    @Override
    public boolean isOver() {
        if (isFloating())
            return false;

        else {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(endDateTime);
        }
    }

    @Override
    public boolean isFloating() {
        return (startDateTime == null && endDateTime == null);
    }

    @Override
    public boolean isEvent() {
        return (startDateTime != null && endDateTime != null);
    }

    @Override
    public boolean isDeadline() {
        return (startDateTime == null && endDateTime != null);
    }
}
