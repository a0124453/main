package lifetracker.calendar;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarList {

    // get() and set() functions for variables
    List<Task> getTaskList();

    List<Event> getEventList();

    void addEvent(String name, LocalDateTime start, LocalDateTime end);

    void addEvent(Event event);

    void addTask(String name, LocalDateTime deadline);

    void addTask(Task task);
}
