package lifetracker.command;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

//@@author A0091173J
public class CommandFactoryImpl implements CommandFactory {
    @Override
    public CommandObject addFloatingTask(String name) {
        return new AddCommand(name);
    }

    @Override
    public CommandObject addDeadlineTask(String name, LocalDateTime deadLine) {
        return new AddCommand(name, deadLine);
    }

    @Override
    public CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, TemporalAmount recurringPeriod) {
        return new AddRecurringCommand(name, deadLine, recurringPeriod);
    }

    @Override
    public CommandObject addEvent(String name, LocalDateTime startTime, LocalDateTime endTime) {
        return new AddCommand(name, startTime, endTime);
    }

    @Override
    public CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            TemporalAmount recurringPeriod) {
        return new AddRecurringCommand(name, startTime, endTime, recurringPeriod);
    }

    @Override
    public CommandObject find() {
        return new FindCommand();
    }

    @Override
    public CommandObject find(String searchString) {
        return new FindCommand(searchString);
    }

    @Override
    public CommandObject findAll() {
        return new FindAllCommand();
    }

    @Override
    public CommandObject findAll(String searchString) {
        return new FindAllCommand(searchString);
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
    public CommandObject edit(int id, String name, LocalDateTime startTime, LocalDateTime endTime,
            TemporalAmount recurringPeriod) {
        return new EditCommand(id, name, startTime, endTime, recurringPeriod);
    }
}
