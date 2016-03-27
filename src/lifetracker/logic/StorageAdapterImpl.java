package lifetracker.logic;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lifetracker.calendar.CalendarList;
import lifetracker.storage.Storage;

public class StorageAdapterImpl implements StorageAdapter {

    @Override
    public void store(CalendarList calendar, Storage storage) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(calendar);
        storage.store(s);
    }
    
    @Override
    public CalendarList load(Storage storage) throws IOException {
        String l = storage.load();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CalendarList calendar = gson.fromJson(l, CalendarList.class);
        return calendar;
    }
}
