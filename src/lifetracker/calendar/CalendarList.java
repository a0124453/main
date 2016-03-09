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

    boolean delete(int id);

    boolean update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd);

    List<CalendarEntry> list(String toSearch);

}
