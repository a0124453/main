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

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns {@code null} because a {@code GenericEntry} has no associated
     * {@code LocalDateTime}.
     * 
     * @param property
     *            An {@code enum} representing the desired property.
     * @return {@code null}.
     */
    @Override
    public LocalDateTime getDateTime(CalendarProperty property) {
        return null;
    }

    /**
     * Method not allowed for {@code GenericEntry} object.
     * 
     * @throws IllegalArgumentException
     */
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
     * Returns {@code null} for {@code GenericEntry}.
     * 
     * @return {@code null}.
     */
    @Override
    public Period getPeriod() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    /**
     * Returns a {@code boolean} to indicate whether the entry satisfies the
     * specified property.
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

    /**
     * A {@code GenericEntry} has no associated integer property (other than ID
     * number, which has its own getter).
     * 
     * @param property
     *            An {@code enum} representing the desired property.
     * @return 0.
     */
    @Override
    public int getIntegerProperty(CalendarProperty property) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(EntryVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
