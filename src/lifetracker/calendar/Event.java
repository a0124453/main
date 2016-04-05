package lifetracker.calendar;

import java.time.LocalDateTime;

public class Event extends DeadlineTask {

    public static final String CLASS_NAME = "Event";

    private LocalDateTime startDateTime;

    public Event(String name, LocalDateTime start, LocalDateTime end) {
        super(name, end);
        this.startDateTime = start;
    }

    public Event(Event entry) {
        super(entry);
        this.startDateTime = entry.startDateTime;
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
            this.startDateTime = dateTime;
        } else {
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

}
