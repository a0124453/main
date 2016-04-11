package lifetracker.calendar;

import lifetracker.calendar.visitor.EntryVisitor;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@@author A0108473E
public class Event extends DeadlineTask {

    private LocalDateTime startDateTime;

    public Event(String name, LocalDateTime start, LocalDateTime end) {
        super(name, end);
        CalendarEntry.checkStartBeforeEnd(start, end);
        this.startDateTime = start;
        SERIAL_TYPE_IDENTIFIER = "Event";
    }

    public Event(Event entry) {
        super(entry);
        this.startDateTime = entry.startDateTime;
        SERIAL_TYPE_IDENTIFIER = "Event";
    }

    @Override
    public LocalDateTime getDateTime(CalendarProperty property) {
        if (property.equals(CalendarProperty.START)) {
            return startDateTime;
        }
        return super.getDateTime(property);
    }

    @Override
    public void setDateTime(CalendarProperty property, LocalDateTime dateTime) {
        if (property.equals(CalendarProperty.START)) {
            CalendarEntry.checkStartBeforeEnd(dateTime, getDateTime(CalendarProperty.END));

            this.startDateTime = dateTime;
        } else {
            super.setDateTime(property, dateTime);
        }
    }

    @Override
    public boolean isProperty(CalendarProperty property) {
        switch (property) {
            case ONGOING:
                boolean hasStarted = LocalDateTime.now().isAfter(startDateTime);
                boolean isOver = isProperty(CalendarProperty.OVER);
                return hasStarted && !isOver;
            case TODAY:
                boolean startsToday = (LocalDate.now().equals(startDateTime.toLocalDate()));
                boolean isOngoing = isProperty(CalendarProperty.ONGOING);
                return startsToday || isOngoing;
            default:
                return super.isProperty(property);
        }
    }

    @Override
    public <T> T accept(EntryVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
