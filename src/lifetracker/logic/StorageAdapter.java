package lifetracker.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarEntryImplDeserializer;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import lifetracker.storage.Storage;

import java.io.IOException;

//@@author A0149467N

public class StorageAdapter {

    private Storage calendarStorage;

    public StorageAdapter(Storage storage) {
        this.calendarStorage = storage;
    }

    /**
     * Convert the calendar to Json and let the storage store it
     *
     * @param calendar Calendar to be stored
     * 
     * @throws IOException
     *      If an I/O error occurs during store
     */
    public void store(CalendarList calendar) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(calendar);
        calendarStorage.store(s);
    }

    /**
     * Load the Json string for calendar from storage and convert it to CalendarList
     *
     * @return The calendar stored in the storage
     * 
     * @throws IOException
     *      If an I/O error occurs during load()
     */
    public CalendarList load() throws IOException {
        String l = calendarStorage.load();

        if (l.isEmpty()) {
            return new CalendarListImpl();
        } else {
            Gson gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(CalendarEntry.class, new CalendarEntryImplDeserializer())
                    .setPrettyPrinting().create();
            CalendarList calendar = gson.fromJson(l, CalendarListImpl.class);
            return calendar;
        }
    }
}
