package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.Period;

import lifetracker.calendar.visitor.EntryVisitor;

public class GenericEntry implements CalendarEntry {

    protected String SERIAL_TYPE_IDENTIFIER = "GenericEntry";

    private static final String MESSAGE_ERROR_INVALID_COMMAND = "Invalid command!";

    private String name;
    private int id;
    private boolean isActive = true;

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

    /**
     * Method not allowed for {@code GenericEntry} object.
     * 
     * @throws IllegalArgumentException
     */
    @Override
    public void setPeriod(Period period) {
        throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_COMMAND);
    }

    /**
     * Returns {@code null} for non-recurring tasks.
     * 
     * @return {@code null}.
     */
    @Override
    public Period getPeriod() {
        return null;
    }

    @Override
    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    /**
     * A {@code GenericEntry} has no deadline and is always ongoing.
     * 
     * @param property
     *            An {@code enum} representing the desired property.
     * 
     * @return A {@code boolean} to indicate whether the entry satisfies the
     *         specified property.
     */
    @Override
    public boolean isProperty(CalendarProperty property) {
        switch (property) {
            case ONGOING :
                return true;
            case ACTIVE :
                return this.isActive;
            case OVER :
                return false;
            case TODAY :
                return true;
            default :
                return false;
        }
    }

    @Override
    public int getIntegerProperty(CalendarProperty property) {
        return 0;
    }

    @Override
    public <T> T accept(EntryVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
