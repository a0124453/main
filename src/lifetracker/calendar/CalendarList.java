package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public interface CalendarList {

    public static final String MESSAGE_ERROR_TASK_NOT_FOUND = "Task not found!";
    public static final String MESSAGE_ERROR_EVENT_NOT_FOUND = "Event not found!";

    // getter and setter functions for variables
    List<CalendarEntry> getTaskList();

    List<CalendarEntry> getEventList();

    List<CalendarEntry> getArchivedTaskList();

    List<CalendarEntry> getArchivedEventList();

    int add(String name); // floating task

    int add(String name, LocalDateTime deadline); // deadline task

    int add(String name, LocalDateTime deadline, TemporalAmount period); // recurring
                                                                         // task

    int add(String name, LocalDateTime start, LocalDateTime end); // event

    int add(String name, LocalDateTime start, LocalDateTime end, TemporalAmount period); // recurring
                                                                                         // event

    CalendarEntry delete(int id);

    CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
            TemporalAmount newPeriod);

    CalendarEntry mark(int id);

    CalendarList findByName(String toSearch);

    CalendarList findArchivedByName(String toSearch);

    CalendarList findAllByName(String toSearch);

}
