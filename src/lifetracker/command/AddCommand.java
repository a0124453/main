package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddCommand implements CommandObject {
    private String comment;
    private String task;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDateTime startDateTime;
    private LocalTime endTime;
    private LocalDate endDate;
    private LocalDateTime endDateTime;

    @Override
    public CalendarList execute(CalendarList calendar) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

}
