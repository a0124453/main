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

}
