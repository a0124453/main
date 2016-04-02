package lifetracker.command;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;

//@@author A0091173J
public interface CommandFactory {
    CommandObject addFloatingTask(String name);

    CommandObject addDeadlineTask(String name, LocalDateTime deadLine);

    CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, TemporalAmount recurringPeriod);

    CommandObject addEvent(String name, LocalDateTime startTime, LocalDateTime endTime);

    CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            TemporalAmount recurringPeriod);

    /**
     * Returns a CommandObject that lists all calendar entries.
     *
     * @return The CommandObject to find all entries
     */
    CommandObject find();

    CommandObject find(String searchString);

    CommandObject findAll();

    CommandObject findAll(String searchString);

    CommandObject delete(int id);

    CommandObject mark(int id);

    CommandObject edit(int id, String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod);
}
