package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class RecurringEvent extends RecurringTask {

    private LocalDateTime startDateTime;

    public RecurringEvent(String name, LocalDateTime start, LocalDateTime end, Period period) {
        super(name, end, period);
        startDateTime = start;
    }

    public RecurringEvent(String name, LocalDateTime start, LocalDateTime end, Period period, int limit) {
        super(name, end, period, limit);
        startDateTime = start;
    }

    public RecurringEvent(String name, LocalDateTime start, LocalDateTime deadline, Period period, LocalDate limit) {
        super(name, deadline, period, limit);
        startDateTime = start;
    }

    public RecurringEvent(RecurringEvent entry) {
        super(entry);
        this.startDateTime = entry.startDateTime;
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
                boolean startsToday = (LocalDateTime.now().equals(startDateTime));
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
        }
        super.updateToNext();
    }

}
