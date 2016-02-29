package lifetracker.calendar;

import java.util.ArrayList;
import java.util.List;
import java.time.*;

public class NamedCalendarList implements CalendarList {

    // variables
    private List<Task> taskList = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();

    // get() and set() functions for variables

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.CalenderList#getTaskList()
     */
    @Override
    public List<Task> getTaskList() {
        return taskList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.CalenderList#getEventList()
     */
    @Override
    public List<Event> getEventList() {
        return eventList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * lifetracker.calendar.CalenderList#addEvent(lifetracker.calendar.Event)
     */
    @Override
    public void addEvent(Event event) {
        eventList.add(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.CalenderList#addEvent(java.lang.String,
     * java.time.LocalDateTime, java.time.LocalDateTime)
     */
    @Override
    public void addEvent(String name, LocalDateTime start, LocalDateTime end) {
        Event e = new NamedEvent(name, start, end);
        eventList.add(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.CalenderList#addTask(lifetracker.calendar.Task)
     */
    @Override
    public void addTask(Task task) {
        taskList.add(task);
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.CalenderList#addTask(java.lang.String,
     * java.time.LocalDateTime)
     */
    @Override
    public void addTask(String name, LocalDateTime deadline) {
        Task t = new NamedTask(name, deadline);
        taskList.add(t);
    }
}
