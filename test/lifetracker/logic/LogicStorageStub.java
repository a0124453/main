package lifetracker.logic;

import java.io.IOException;

import lifetracker.calendar.CalendarList;
import lifetracker.storage.Storage;

public class LogicStorageStub implements Storage {

    @Override
    public void setStore(String destination) throws IOException {
        
    }
    
    @Override
    public void store(CalendarList calendar) throws IOException {
        
    }
    
    @Override
    public CalendarList load(CalendarList calendar) throws IOException {
        return null;
    }
    
    @Override
    public void close() throws Exception {
        
    }
}
