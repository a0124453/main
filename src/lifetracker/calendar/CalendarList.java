package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

public interface CalendarList {

    String MESSAGE_ERROR_TASK_NOT_FOUND = "Task not found!";
    String MESSAGE_ERROR_EVENT_NOT_FOUND = "Event not found!";

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

    CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            Period newPeriod);

    CalendarEntry mark(int id);

    CalendarList findByName(String toSearch);

    CalendarList findArchivedByName(String toSearch);

    CalendarList findAllByName(String toSearch);

}
