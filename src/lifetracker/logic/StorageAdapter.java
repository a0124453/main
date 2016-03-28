package lifetracker.logic;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import lifetracker.storage.Storage;

public class StorageAdapter {

    public void store(CalendarList calendar, Storage storage) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(calendar);
        storage.store(s);
    }
    
    public CalendarList load(Storage storage) throws IOException {
        String l = storage.load();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CalendarList calendar = gson.fromJson(l, CalendarListImpl.class);
        return calendar;
    }
}
