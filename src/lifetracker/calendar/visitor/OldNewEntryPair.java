package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;

public class OldNewEntryPair {
    public CalendarEntry oldEntry;
    public CalendarEntry newEntry;

    public OldNewEntryPair(CalendarEntry oldEntry, CalendarEntry newEntry) {
        this.oldEntry = oldEntry;
        this.newEntry = newEntry;
    }

}
