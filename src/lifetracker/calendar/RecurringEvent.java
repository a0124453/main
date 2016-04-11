package lifetracker.calendar;

import lifetracker.calendar.visitor.EntryVisitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class RecurringEvent extends RecurringTask {

    private LocalDateTime startDateTime;

    public RecurringEvent(String name, LocalDateTime start, LocalDateTime end, Period period) {
        super(name, end, period);
        CalendarEntry.checkStartBeforeEnd(start, end);
        startDateTime = start;
        SERIAL_TYPE_IDENTIFIER = "RecurringEvent";
    }

    public RecurringEvent(String name, LocalDateTime start, LocalDateTime end, Period period, int limit) {
        super(name, end, period, limit);
        CalendarEntry.checkStartBeforeEnd(start, end);
        startDateTime = start;
        SERIAL_TYPE_IDENTIFIER = "RecurringEvent";
    }

    public RecurringEvent(String name, LocalDateTime start, LocalDateTime end, Period period, LocalDate limit) {
        super(name, end, period, limit);
        CalendarEntry.checkStartBeforeEnd(start, end);
        startDateTime = start;
        SERIAL_TYPE_IDENTIFIER = "RecurringEvent";
    }

    public RecurringEvent(RecurringEvent entry) {
        super(entry);
        this.startDateTime = entry.startDateTime;
        SERIAL_TYPE_IDENTIFIER = "RecurringEvent";
    }

    @Override
    public LocalDateTime getDateTime(CalendarProperty property) {
        switch (property) {
        case START :
            return this.startDateTime;
        default :
            return super.getDateTime(property);
        }
    }

    @Override
    public void setDateTime(CalendarProperty property, LocalDateTime dateTime) {
        switch (property) {
        case START :
            // end date is always valid
            CalendarEntry.checkStartBeforeEnd(dateTime, this.getDateTime(CalendarProperty.END));
            this.startDateTime = dateTime;
            break;
        default :
            super.setDateTime(property, dateTime);
            break;
        }
    }

    @Override
    public boolean isProperty(CalendarProperty property) {
        switch (property) {
        case ONGOING :
            boolean hasStarted = LocalDateTime.now().isAfter(startDateTime);
            boolean isOver = isProperty(CalendarProperty.OVER);
            return hasStarted && !isOver;
        case TODAY :
            boolean startsToday = (LocalDate.now().equals(startDateTime.toLocalDate()));
            boolean isOngoing = isProperty(CalendarProperty.ONGOING);
            return startsToday || isOngoing;
        default :
            return super.isProperty(property);
        }
    }

    @Override
    public void updateToNext() {
        if (hasNext()) {
            this.startDateTime = startDateTime.plus(getPeriod());
            forceUpdateDate();
        }
    }

    @Override
    public boolean hasNext() {
        int occurrenceLimit = getIntegerProperty(CalendarProperty.OCCURRENCE_LIMIT);

        if (occurrenceLimit == INF_LIMIT_CONST) {
            return true;
        } else if (occurrenceLimit == DATE_LIMIT_CONST) {
            LocalDate newDate = getDateTime(CalendarProperty.START).toLocalDate().plus(getPeriod());
            LocalDate limit = getDateTime(CalendarProperty.DATE_LIMIT).toLocalDate();

            return newDate.isBefore(limit) || newDate.isEqual(limit);
        } else {
            return occurrenceLimit > 1;
        }
    }

    @Override
    public <T> T accept(EntryVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
