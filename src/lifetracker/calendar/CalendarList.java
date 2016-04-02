package lifetracker.calendar;

import java.time.LocalDateTime;
import java.time.Period;
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

    int add(String name, LocalDateTime deadline, Period period); // recurring
                                                                         // task

    int add(String name, LocalDateTime start, LocalDateTime end); // event

    int add(String name, LocalDateTime start, LocalDateTime end, Period period); // recurring
                                                                                         // event

    int add(CalendarEntry entry);

    CalendarEntry delete(int id);

    CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd,
 Period newPeriod);

    CalendarEntry mark(int id);

    CalendarList findByName(String toSearch);

    CalendarList findArchivedByName(String toSearch);

    CalendarList findAllByName(String toSearch);

}
