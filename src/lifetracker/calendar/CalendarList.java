package lifetracker.calendar;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarList {

    // get() and set() functions for variables
    List<CalendarEntry> getTaskList();

    List<CalendarEntry> getEventList();

    int add(String name); // floating task

    int add(String name, LocalDateTime due); // deadline task

    int add(String name, LocalDateTime start, LocalDateTime end); // event

    CalendarEntry delete(int id);

    CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd);

    List<CalendarEntry> list(String toSearch);

}
