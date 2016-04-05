package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;

public class EditedEntryPair {
    public CalendarEntry oldEntry;

    public EditedEntryPair(CalendarEntry oldEntry, CalendarEntry newEntry) {
        this.oldEntry = oldEntry;
        this.newEntry = newEntry;
    }

    public CalendarEntry newEntry;
}
