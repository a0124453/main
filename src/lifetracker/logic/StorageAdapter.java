package lifetracker.logic;

import java.io.IOException;

import lifetracker.calendar.CalendarList;
import lifetracker.storage.Storage;

public interface StorageAdapter {
    
    void store(CalendarList calendar, Storage storage) throws IOException;
    
    CalendarList load(Storage storage) throws IOException;
}
