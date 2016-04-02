package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.Period;

public class GenericEntry implements CalendarEntry {

    private static final String MESSAGE_ERROR_INVALID_COMMAND = "Invalid command!";

    private String name;
    private int id;
    private boolean isActive;

    public GenericEntry(String name) {
        this.name = name;
    }

    public GenericEntry(CalendarEntry entry) {
        this.name = entry.getName();
        this.id = entry.getId();
        this.isActive = entry.isProperty(CalendarProperty.ACTIVE);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalDateTime getDateTime(CalendarProperty property) {
        return null;
    }

    @Override
    public void setDateTime(CalendarProperty property, LocalDateTime dateTime) {
        throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
    }

    @Override
    public void setPeriod(Period period) {
        throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
    }

    @Override
    public Period getPeriod() {
        return null;
    }

    @Override
    public void mark() {
        if (this.isActive == true) {
            this.isActive = false;
        } else {
            this.isActive = true;
        }
    }

    @Override
    public boolean isProperty(CalendarProperty property) {
        switch (property) {
            case ONGOING :
                return true;
            case ACTIVE :
                return this.isActive;
            case OVER :
                return this.isActive;
            default :
                return false;
        }
    }

}
