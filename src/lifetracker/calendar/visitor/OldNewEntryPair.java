package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;

public class OldNewEntryPair {
    public CalendarEntry oldEntry;

    public OldNewEntryPair(CalendarEntry oldEntry, CalendarEntry newEntry) {
        this.oldEntry = oldEntry;
        this.newEntry = newEntry;
    }

    public CalendarEntry newEntry;
}
