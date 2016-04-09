package lifetracker.logic;

import java.io.IOException;

import lifetracker.calendar.CalendarList;
import lifetracker.storage.Storage;

//@@author A0149467N-unused

public class LogicStorageStub implements Storage {

    @Override
    public void setStore(String destination) throws IOException {
        
    }
    
    @Override
    public void store(CalendarList calendar) throws IOException {
        
    }
    
    @Override
    public CalendarList load(CalendarList calendar) throws IOException {
        return calendar;
    }
    
    @Override
    public void close() throws Exception {
        
    }
}
