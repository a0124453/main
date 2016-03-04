package CommandObject;

import lifetracker.calendar.CalendarList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class AddCommand implements CommandObject {
    private String comment;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDateTime startDateTime;
    private LocalTime endTime;
    private LocalDate endDate;
    private LocalDateTime endDateTime;

    public CalendarList execute(CalendarList calendar) {
        // TODO Auto-generated method stub
        return null;
    }

    public CalendarList undo(CalendarList calendar) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getComments() {
        return this.comment;
    }

    public void setComments(String comments) {
        this.comment = comments;
    }

}
