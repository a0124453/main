package lifetracker.logic;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import lifetracker.storage.Storage;

public class StorageAdapter {
    
    private Storage calendarStorage;
    
    public StorageAdapter(Storage storage) {
        this.calendarStorage = storage;
    }

    public void store(CalendarList calendar) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(calendar);
        calendarStorage.store(s);
    }
    
    public CalendarList load() throws IOException {
        String l = calendarStorage.load();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CalendarList calendar = gson.fromJson(l, CalendarListImpl.class);
        return calendar;
    }
}
