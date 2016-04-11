package lifetracker.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

//@@author A0091173J
public class CommandFactoryImpl implements CommandFactory {
    @Override
    public CommandObject addGenericTask(String name) {
        return new AddCommand(name);
    }

    @Override
    public CommandObject addDeadlineTask(String name, LocalDateTime deadLine) {
        return new AddCommand(name, deadLine);
    }

    @Override
    public CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod) {
        return new AddRecurringCommand(name, deadLine, recurringPeriod);
    }

    @Override
    public CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod,
            int limit) {
        return new AddRecurringCommand(name, deadLine, recurringPeriod, limit);
    }

    @Override
    public CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod,
            LocalDate limitDate) {
        return new AddRecurringCommand(name, deadLine, recurringPeriod, limitDate);
    }

    @Override
    public CommandObject addEvent(String name, LocalDateTime startTime, LocalDateTime endTime) {
        return new AddCommand(name, startTime, endTime);
    }

    @Override
    public CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod) {
        return new AddRecurringCommand(name, startTime, endTime, recurringPeriod);
    }

    @Override
    public CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod, int limit) {
        return new AddRecurringCommand(name, startTime, endTime, recurringPeriod, limit);
    }

    @Override
    public CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod, LocalDate limitDate) {
        return new AddRecurringCommand(name, startTime, endTime, recurringPeriod, limitDate);
    }

    @Override
    public CommandObject find(boolean isOnlyToday) {
        return new FindCommand(isOnlyToday);
    }

    @Override
    public CommandObject find(String searchString, boolean isOnlyToday) {
        return new FindCommand(searchString, isOnlyToday);
    }

    @Override
    public CommandObject findAll(boolean isOnlyToday) {
        return new FindAllCommand(isOnlyToday);
    }

    @Override
    public CommandObject findAll(String searchString, boolean isOnlyToday) {
        return new FindAllCommand(searchString, isOnlyToday);
    }

    @Override
    public CommandObject findOld(boolean isOnlyToday){
        return new FindOldCommand(isOnlyToday);
    }

    @Override
    public CommandObject findOld(String searchTerm, boolean isOnlyToday){
        return new FindOldCommand(searchTerm, isOnlyToday);
    }

    @Override
    public CommandObject delete(int id) {
        return new DeleteCommand(id);
    }

    @Override
    public CommandObject mark(int id) {
        return new MarkCommand(id);
    }

    @Override
    public CommandObject editGenericTask(int id, String name, boolean isForcedConvert) {
        return new EditGenericTaskCommand(id, name, isForcedConvert);
    }

    @Override
    public CommandObject editDeadline(int id, String name, LocalDateTime deadline, boolean isLimitRemoved) {
        return new EditDeadlineTaskCommand(id, name, deadline, isLimitRemoved);
    }

    @Override
    public CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            boolean isLimitRemoved) {
        return new EditRecurringTaskCommand(id, name, deadline, recurringPeriod, isLimitRemoved);
    }

    @Override
    public CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            int limit) {
        return new EditRecurringTaskCommand(id, name, deadline, recurringPeriod, limit);
    }

    @Override
    public CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            LocalDate limitDate) {
        return new EditRecurringTaskCommand(id, name, deadline, recurringPeriod, limitDate);
    }

    @Override
    public CommandObject editEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            boolean isLimitRemoved) {
        return new EditEventCommand(id, name, start, end, isLimitRemoved);
    }

    @Override
    public CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, boolean isLimitRemoved) {
        return new EditRecurringEventCommand(id, name, start, end, recurringPeriod, isLimitRemoved);
    }

    @Override
    public CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, int limit) {
        return new EditRecurringEventCommand(id, name, start, end, recurringPeriod, limit);
    }

    @Override
    public CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, LocalDate limitDate) {
        return new EditRecurringEventCommand(id, name, start, end, recurringPeriod, limitDate);
    }

    @Override
    public CommandObject editStop(int id, String name) {
        return new EditStopCommand(id, name);
    }

    @Override
    public CommandObject editRecurring(int id, String name, Period recurringPeriod, boolean isLimitRemoved) {
        return new EditRecurringEntryCommand(id, name, recurringPeriod, isLimitRemoved);
    }

    @Override
    public CommandObject editRecurring(int id, String name, Period recurringPeriod, int limit) {
        return new EditRecurringEntryCommand(id, name, recurringPeriod, limit);
    }

    @Override
    public CommandObject editRecurring(int id, String name, Period recurringPeriod, LocalDate limitDate) {
        return new EditRecurringEntryCommand(id, name, recurringPeriod, limitDate);
    }

    @Override
    public CommandObject editOne(int id, String name) {
        return new EditOneCommand(id, name);
    }

    @Override
    public CommandObject editOneToDeadline(int id, String name, LocalDateTime deadline) {
        return new EditOneCommand(id,name, deadline);
    }

    @Override
    public CommandObject editOneToEvent(int id, String name, LocalDateTime start, LocalDateTime end) {
        return new EditOneCommand(id, name, start, end);
    }
}
