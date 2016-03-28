package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public interface CalendarList {

    // get() and set() functions for variables
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

    CalendarList find(String toSearch, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime);

    CalendarList findArchived(String toSearch, LocalDate startDate, LocalTime startTime, LocalDate endDate,
            LocalTime endTime);

    CalendarList findAll(String toSearch, LocalDate startDate, LocalTime startTime, LocalDate endDate,
            LocalTime endTime);

}
