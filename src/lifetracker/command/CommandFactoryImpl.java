package lifetracker.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

//@@author A0091173J

/**
 * An implementation of the {@code CommandFactory}
 *
 * @see CommandFactory
 */
public class CommandFactoryImpl implements CommandFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addGenericTask(String name) {
        return new AddCommand(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addDeadlineTask(String name, LocalDateTime deadLine) {
        return new AddCommand(name, deadLine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod) {
        return new AddRecurringCommand(name, deadLine, recurringPeriod);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod,
            int limit) {
        return new AddRecurringCommand(name, deadLine, recurringPeriod, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod,
            LocalDate limitDate) {
        return new AddRecurringCommand(name, deadLine, recurringPeriod, limitDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addEvent(String name, LocalDateTime startTime, LocalDateTime endTime) {
        return new AddCommand(name, startTime, endTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod) {
        return new AddRecurringCommand(name, startTime, endTime, recurringPeriod);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod, int limit) {
        return new AddRecurringCommand(name, startTime, endTime, recurringPeriod, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod, LocalDate limitDate) {
        return new AddRecurringCommand(name, startTime, endTime, recurringPeriod, limitDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject find(boolean isOnlyToday) {
        return new FindCommand(isOnlyToday);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject find(String searchString, boolean isOnlyToday) {
        return new FindCommand(searchString, isOnlyToday);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject findAll(boolean isOnlyToday) {
        return new FindAllCommand(isOnlyToday);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject findAll(String searchString, boolean isOnlyToday) {
        return new FindAllCommand(searchString, isOnlyToday);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject findOld(boolean isOnlyToday) {
        return new FindOldCommand(isOnlyToday);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject findOld(String searchString, boolean isOnlyToday) {
        return new FindOldCommand(searchString, isOnlyToday);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject delete(int id) {
        return new DeleteCommand(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject mark(int id) {
        return new MarkCommand(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editGenericTask(int id, String name, boolean isForcedConvert) {
        return new EditGenericTaskCommand(id, name, isForcedConvert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editDeadline(int id, String name, LocalDateTime deadline, boolean isLimitRemoved) {
        return new EditDeadlineTaskCommand(id, name, deadline, isLimitRemoved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            boolean isLimitRemoved) {
        return new EditRecurringTaskCommand(id, name, deadline, recurringPeriod, isLimitRemoved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            int limit) {
        return new EditRecurringTaskCommand(id, name, deadline, recurringPeriod, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            LocalDate limitDate) {
        return new EditRecurringTaskCommand(id, name, deadline, recurringPeriod, limitDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            boolean isLimitRemoved) {
        return new EditEventCommand(id, name, start, end, isLimitRemoved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, boolean isLimitRemoved) {
        return new EditRecurringEventCommand(id, name, start, end, recurringPeriod, isLimitRemoved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, int limit) {
        return new EditRecurringEventCommand(id, name, start, end, recurringPeriod, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, LocalDate limitDate) {
        return new EditRecurringEventCommand(id, name, start, end, recurringPeriod, limitDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editStop(int id, String name) {
        return new EditStopCommand(id, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurring(int id, String name, Period recurringPeriod, boolean isLimitRemoved) {
        return new EditRecurringEntryCommand(id, name, recurringPeriod, isLimitRemoved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurring(int id, String name, Period recurringPeriod, int limit) {
        return new EditRecurringEntryCommand(id, name, recurringPeriod, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editRecurring(int id, String name, Period recurringPeriod, LocalDate limitDate) {
        return new EditRecurringEntryCommand(id, name, recurringPeriod, limitDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editOne(int id, String name) {
        return new EditOneCommand(id, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editOneToDeadline(int id, String name, LocalDateTime deadline) {
        return new EditOneCommand(id, name, deadline);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandObject editOneToEvent(int id, String name, LocalDateTime start, LocalDateTime end) {
        return new EditOneCommand(id, name, start, end);
    }
}
