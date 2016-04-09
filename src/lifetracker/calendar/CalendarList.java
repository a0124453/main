package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import lifetracker.calendar.visitor.OldNewEntryPair;

public interface CalendarList {

    String ERROR_INVALID_ID = "Entry %1$s is not found!";
    int BASE_ID = 0;

    // getter and setter functions for variables
    List<CalendarEntry> getTaskList();

    List<CalendarEntry> getEventList();

    List<CalendarEntry> getArchivedTaskList();

    List<CalendarEntry> getArchivedEventList();

    /**
     * Adds a floating task.
     *
     * @param name Name of floating task
     * @return The entry ID of the newly added task.
     */
    int add(String name); // floating task

    int add(String name, LocalDateTime deadline); // deadline task

    int add(String name, LocalDateTime deadline, Period period); // recurring task

    int add(String name, LocalDateTime deadline, Period period, int limit);

    int add(String name, LocalDateTime deadline, Period period, LocalDate limitDate);

    int add(String name, LocalDateTime start, LocalDateTime end); // event

    int add(String name, LocalDateTime start, LocalDateTime end, Period period); // recurring event

    int add(String name, LocalDateTime start, LocalDateTime end, Period period, int limit);

    int add(String name, LocalDateTime start, LocalDateTime end, Period period, LocalDate limitDate);

    int add(CalendarEntry entry);

    CalendarEntry delete(int id);

    CalendarEntry updateToGeneric(int id, String newName, boolean isConvertForced);

    CalendarEntry updateToDeadline(int id, String newName, LocalDateTime newDeadline, boolean isConvertForced);

    CalendarEntry updateToEvent(int id, String newName, LocalDateTime newStartTime, LocalDateTime newEndTime,
            boolean isConvertForced);

    CalendarEntry updateToRecurringTask(int id, String newName, LocalDateTime newDeadLine, Period newPeriod,
            boolean isLimitKept, boolean isConvertForced);

    /**
     * Updates an entry into a RecurringTask.
     * <p>
     * Empty fields will be skipped during update, unless a conversion for the entry requires it, in which case an
     * {@code IllegalArgumentException} will be thrown.
     * <p>
     * If {@code isConvertForced} is set, then this method will forcefully convert the entry into a recurring task,
     * even
     * if information will be lost. For example, if the entry was a {@code RecurringEvent}, the start date time will be
     * lost.
     * <p>
     * {@code newLimit} specifies the new number of occurrences to happen after and including the current occurrence.
     * For
     * example, if {@code newLimit} is set to 4, then the recurring task will happen 4 more times (including the
     * current
     * iteration), not matter how many times it had occurred in the past.
     *
     * @param id              The ID of the entry to update
     * @param newName         The new name
     * @param newDeadLine     The new deadline
     * @param newPeriod       The new period
     * @param newLimit        The new occurrence limit
     * @param isConvertForced If information can be discarded during the conversion.
     * @return The old entry object before the update
     * @throws IllegalArgumentException If fields that are required are empty.
     */
    CalendarEntry updateToRecurringTask(int id, String newName, LocalDateTime newDeadLine, Period newPeriod,
            int newLimit, boolean isConvertForced);

    CalendarEntry updateToRecurringTask(int id, String newName, LocalDateTime newDeadLine, Period newPeriod,
            LocalDate newLimitDate, boolean isConvertForced);

    /**
     * Updates an entry into a recurring event without limit.
     * <p>
     * Empty fields will be skipped during update, unless a conversion for the entry requires it, in which case an
     * {@code IllegalArgumentException} will be thrown.
     * <p>
     * If {@code isLimitKept} is set, then this method will discard the previous recurring limit set in an entry
     * with such a limit.
     *
     * @param id              The ID of the entry to update.
     * @param newName         The new name
     * @param newStart        The new start date time
     * @param newEnd          The new end date time
     * @param newPeriod       The new recurring time period
     * @param isLimitKept If the original limit should be discarded
     * @return The old entry before the update
     * @throws IllegalArgumentException If fields that are required are empty.
     */
    CalendarEntry updateToRecurringEvent(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            Period newPeriod, boolean isLimitKept);

    CalendarEntry updateToRecurringEvent(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            Period newPeriod, int newLimit);

    CalendarEntry updateToRecurringEvent(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            Period newPeriod, LocalDate newLimit);

    /**
     * Overrides an entry in the {@code CalendarList} with the new entry provided, if they have the same ID.
     * <p>
     * If there are no old entries with the same ID, the new entry is simply added to the CalendarList.
     *
     * @param newEntry The new entry
     * @return The old entry before the update if present, {@code null} otherwise.
     */
    CalendarEntry update(CalendarEntry newEntry);

    OldNewEntryPair mark(int id);

    CalendarEntry get(int id);

    CalendarList findByName(String toSearch);

    CalendarList findArchivedByName(String toSearch);

    CalendarList findAllByName(String toSearch);

    CalendarList findToday();

}
