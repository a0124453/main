package lifetracker.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

//@@author A0091173J

/**
 * A {@code CommandFactory} provides methods that generate {@code CommandObjects} for various purposes.
 */
public interface CommandFactory {
    /**
     * Creates a {@code CommandObject} to add an entry with only a name to the calendar.
     *
     * @param name The name of the entry
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addGenericTask(String name);

    /**
     * Creates a {@code CommandObject} to add an entry with name and a deadline to the calendar.
     *
     * @param name     The name of the entry
     * @param deadLine The deadline for the entry
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addDeadlineTask(String name, LocalDateTime deadLine);

    /**
     * Creates a {@code CommandObject} to add an entry with a name and a deadline that can recur to the calendar.
     * <p>
     * No limits will be set.
     *
     * @param name            The name of the entry
     * @param deadLine        The deadline for the entry
     * @param recurringPeriod The time between recurrences
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod);

    /**
     * Creates a {@code CommandObject} to add an entry with a name and a deadline that can recur to the calendar.
     * <p>
     * The limit is set to the number of occurrences as specified by {@code limit}.
     *
     * @param name            The name of the entry
     * @param deadLine        The deadline for the entry
     * @param recurringPeriod The time between recurrences
     * @param limit           The number of occurrences for recurrences
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod, int limit);

    /**
     * Creates a {@code CommandObject} to add an entry with a name and a deadline that can recur to the calendar.
     * <p>
     * The entry will repeat until the limitDate specified.
     *
     * @param name            The name of the entry
     * @param deadLine        The deadline for the entry
     * @param recurringPeriod The time between recurrences
     * @param limitDate       The last date for an occurrence
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addRecurringDeadlineTask(String name, LocalDateTime deadLine, Period recurringPeriod,
            LocalDate limitDate);

    /**
     * Adds an entry with a name, start date/time and end date/time to the calendar.
     *
     * @param name      The name of the entry
     * @param startTime The start date/time
     * @param endTime   The end date/time
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addEvent(String name, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Adds an entry with a name, start date/time and end date/time to the calendar, that recurs periodically.
     * <p>
     * No limit will be set for the recurrence.
     *
     * @param name            The name of the entry
     * @param startTime       The start date/time
     * @param endTime         The end date/time
     * @param recurringPeriod The time between two occurrences
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod);

    /**
     * Adds an entry with a name, start date/time and end date/time to the calendar, that recurs periodically.
     * <p>
     * The limit is set to the number of occurrences as specified by {@code limit}.
     *
     * @param name            The name of the entry
     * @param startTime       The start date/time
     * @param endTime         The end date/time
     * @param recurringPeriod The time between two occurrences
     * @param limit           The number of occurrences for recurrences
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod, int limit);

    /**
     * Adds an entry with a name, start date/time and end date/time to the calendar, that recurs periodically.
     * <p>
     * The entry will repeat until the limitDate specified.
     *
     * @param name            The name of the entry
     * @param startTime       The start date/time
     * @param endTime         The end date/time
     * @param recurringPeriod The time between two occurrences
     * @param limitDate       The last date for an occurrence
     * @return The corresponding {@code CommandObject}
     */
    CommandObject addRecurringEvent(String name, LocalDateTime startTime, LocalDateTime endTime,
            Period recurringPeriod, LocalDate limitDate);

    /**
     * Returns a CommandObject that lists all non-archived calendar entries.
     * <p>
     * This method accepts a boolean to filter out only entries for today.
     *
     * @param isOnlyToday If only entries today are to be returned.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject find(boolean isOnlyToday);

    /**
     * Returns a CommandObject that lists all non-archived calendar entries that matches the search String.
     * <p>
     * This method accepts a boolean to filter out only entries for today.
     *
     * @param searchString The search string
     * @param isOnlyToday  If only entries today are to be returned.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject find(String searchString, boolean isOnlyToday);

    /**
     * Returns a CommandObject that lists all calendar entries.
     * <p>
     * This method accepts a boolean to filter out only entries for today.
     *
     * @param isOnlyToday If only entries today are to be returned.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject findAll(boolean isOnlyToday);

    /**
     * Returns a CommandObject that lists all calendar entries that matches the search string.
     * <p>
     * This method accepts a boolean to filter out only entries for today.
     *
     * @param searchString The search String
     * @param isOnlyToday  If only entries today are to be returned.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject findAll(String searchString, boolean isOnlyToday);

    /**
     * Returns a CommandObject that lists all archived calendar entries.
     * <p>
     * This method accepts a boolean to filter out only tasks for today.
     *
     * @param isOnlyToday If only entries today are to be returned.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject findOld(boolean isOnlyToday);

    /**
     * Returns a CommandObject that lists all archived calendar entries that matches the search string.
     * <p>
     * This method accepts a boolean to filter out only entries for today.
     *
     * @param searchString The search String
     * @param isOnlyToday  If only entries today are to be returned.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject findOld(String searchString, boolean isOnlyToday);

    /**
     * Returns a CommandObject that deletes an entry based on the ID provided.
     *
     * @param id The ID of the entry to delete
     * @return The corresponding {@code CommandObject}
     */
    CommandObject delete(int id);

    /**
     * Returns a CommandObject that marks an entry based on the ID provided.
     *
     * @param id The ID of the entry to mark
     * @return The corresponding {@code CommandObject}
     */
    CommandObject mark(int id);

    /**
     * Returns a CommandObject that edits the name of a entry in the calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param isForcedConvert If the entry should be forced to only have name
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editGenericTask(int id, String name, boolean isForcedConvert);

    /**
     * Returns a CommandObject that edits the name and deadline of a entry in the calendar.
     *
     * @param id             The ID of the entry
     * @param name           The new name to change into
     * @param deadline       The new deadline
     * @param isLimitRemoved If any existing recurring limits should be removed.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editDeadline(int id, String name, LocalDateTime deadline, boolean isLimitRemoved);

    /**
     * Returns a CommandObject that edits the name, deadline and recurring period of a entry in the calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param deadline        The new deadline
     * @param recurringPeriod The new time between occurrences
     * @param isLimitRemoved  If any existing recurring limits should be removed.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            boolean isLimitRemoved);

    /**
     * Returns a CommandObject that edits the name, deadline, recurring period and the recurring limit of a entry in
     * the
     * calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param deadline        The new deadline
     * @param recurringPeriod The new time between occurrences
     * @param limit           The new limit for the number of occurrences
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod, int limit);

    /**
     * Returns a CommandObject that edits the name, deadline, recurring period and the recurring limit of a entry in
     * the
     * calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param deadline        The new deadline
     * @param recurringPeriod The new time between occurrences
     * @param limitDate       The new date for the last occurrence
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurringDeadline(int id, String name, LocalDateTime deadline, Period recurringPeriod,
            LocalDate limitDate);

    /**
     * Returns a CommandObject that edits the name, start date/time and end date/time of a entry in the calendar.
     *
     * @param id             The ID of the entry
     * @param name           The new name to change into
     * @param start          The new start date/time
     * @param end            The new end date/time
     * @param isLimitRemoved If any existing recurring limits should be removed.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editEvent(int id, String name, LocalDateTime start, LocalDateTime end, boolean isLimitRemoved);

    /**
     * Returns a CommandObject that edits the name, start date/time, end date/time, and recurring period of a entry in
     * the calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param start           The new start date/time
     * @param end             The new end date/time
     * @param recurringPeriod The new time between occurrences
     * @param isLimitRemoved  If any existing recurring limits should be removed.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, boolean isLimitRemoved);

    /**
     * Returns a CommandObject that edits the name, start date/time, end date/time, recurring period, and recurring
     * limit of a entry in
     * the calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param start           The new start date/time
     * @param end             The new end date/time
     * @param recurringPeriod The new time between occurrences
     * @param limit           The new limit for the number of occurrences
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, int limit);

    /**
     * Returns a CommandObject that edits the name, start date/time, end date/time, recurring period, and recurring
     * limit of a entry in
     * the calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param start           The new start date/time
     * @param end             The new end date/time
     * @param recurringPeriod The new time between occurrences
     * @param limitDate       The new date for the last occurrence
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurringEvent(int id, String name, LocalDateTime start, LocalDateTime end,
            Period recurringPeriod, LocalDate limitDate);

    /**
     * Returns a CommandObject that stops the recurring entry from recurring further.
     *
     * @param id   The ID of the entry
     * @param name The new name to change into
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editStop(int id, String name);

    /**
     * Returns a CommandObject that changes the name and recurring period of an entry in the calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param recurringPeriod The new time between occurrences
     * @param isLimitRemoved  If any existing recurring limits should be removed.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurring(int id, String name, Period recurringPeriod, boolean isLimitRemoved);

    /**
     * Returns a CommandObject that changes the name, recurring period, and recurring limit of an entry in the
     * calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param recurringPeriod The new time between occurrences
     * @param limit           The new limit for the number of occurrences
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurring(int id, String name, Period recurringPeriod, int limit);

    /**
     * Returns a CommandObject that changes the name, recurring period, and recurring limit of an entry in the
     * calendar.
     *
     * @param id              The ID of the entry
     * @param name            The new name to change into
     * @param recurringPeriod The new time between occurrences
     * @param limitDate       The new last date for occurrences
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editRecurring(int id, String name, Period recurringPeriod, LocalDate limitDate);

    /**
     * Returns a {@code CommandObject} that edits an single instance of a recurring entry.
     * <p>
     * This method produces a {@code CommandObject} that changes the name of a single instance.
     *
     * @param id   The ID of the entry
     * @param name The new name of the instance
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editOne(int id, String name);

    /**
     * Returns a {@code CommandObject} that edits an single instance of a recurring entry.
     * <p>
     * This method produces a {@code CommandObject} that changes the name and deadline of a single instance.
     *
     * @param id       The ID of the entry
     * @param name     The new name of the instance
     * @param deadline The new deadline fo the instance
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editOneToDeadline(int id, String name, LocalDateTime deadline);

    /**
     * Returns a {@code CommandObject} that edits an single instance of a recurring entry.
     * <p>
     * This method produces a {@code CommandObject} that changes the name, start and end date/time of a single
     * instance.
     *
     * @param id    The ID of the entry
     * @param name  The new name of the instance
     * @param start The new start date/time the instance
     * @param end   The new end date/time for the instance.
     * @return The corresponding {@code CommandObject}
     */
    CommandObject editOneToEvent(int id, String name, LocalDateTime start, LocalDateTime end);
}
