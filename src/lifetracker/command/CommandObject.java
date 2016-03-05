package lifetracker.command;
import lifetracker.calendar.CalendarList;


public interface CommandObject {
    
    CalendarList execute(CalendarList calendar);
    
    CalendarList undo(CalendarList calendar);
    
    String getComments();

}
