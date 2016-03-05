package lifetracker.calendar;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarList {

    // get() and set() functions for variables
    List<CalendarEntry> getTaskList();

    List<CalendarEntry> getEventList();

    void add(String name); // floating task

    void add(String name, LocalDateTime due); // deadline task

    void add(String name, LocalDateTime start, LocalDateTime end); // event

    void delete(int id);

    void update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd);

    void list(String toSearch);

}
