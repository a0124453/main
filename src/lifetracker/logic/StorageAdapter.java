package lifetracker.logic;

import lifetracker.calendar.CalendarList;

public interface StorageAdapter {
    
    void store(CalendarList calendar);
    
    CalendarList load(CalendarList calendar);
}
