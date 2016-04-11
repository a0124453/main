package lifetracker.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

//@@author A0091173J
public interface CommandFactory {
    CommandObject addGenericTask(String name);

    CommandObject addDeadlineTask(String name, LocalDateTime deadLine);

    CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod);

    CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod, int limit);

    CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod,
            LocalDate limitDate);

    CommandObject addEvent(String name, LocalDateTime startTime, LocalDateTime endTime);

    CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod);

    CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod, int limit);

    CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod, LocalDate limitDate);

    /**
     * Returns a CommandObject that lists all non-archived calendar entries.
     * <p>
     * This method accepts a boolean to filter out only tasks for today.
     *
     * @param isOnlyToday If only entries today are to be returned.
     * @return The CommandObject to find all entries
     */
    CommandObject find(boolean isOnlyToday);

    CommandObject find(String searchString, boolean isOnlyToday);

    CommandObject findAll(boolean isOnlyToday);

    CommandObject findAll(String searchString, boolean isOnlyToday);

    CommandObject findOld(boolean isOnlyToday);

    CommandObject findOld(String searchTerm, boolean isOnlyToday);

    CommandObject delete(int id);

    CommandObject mark(int id);

    CommandObject editGenericTask(int id, String name, boolean isForcedConvert);

    CommandObject editDeadline(int id, String name, LocalDateTime deadline, boolean isLimitKept);

    CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            boolean isLimitInf);

    CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod, int limit);

    CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            LocalDate limitDate);

    CommandObject editEvent(int id, String name, LocalDateTime start, LocalDateTime end, boolean isLimitKept);

    CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod,
            boolean isLimitInf);

    CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, int limit);

    CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, LocalDate limitDate);

    CommandObject editStop(int id, String name);

    CommandObject editRecurring(int id, String name, Period recurringPeriod, boolean isLimitInf);

    CommandObject editRecurring(int id, String name, Period recurringPeriod, int limit);

    CommandObject editRecurring(int id, String name, Period recurringPeriod, LocalDate limitDate);

    CommandObject editOne(int id, String name);

    CommandObject editOneToDeadline(int id, String name, LocalDateTime deadline);

    CommandObject editOneToEvent(int id, String name, LocalDateTime start, LocalDateTime end);
}
