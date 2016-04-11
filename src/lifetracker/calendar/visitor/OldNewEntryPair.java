package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;

//@@author A0091173J

public class OldNewEntryPair {
    public CalendarEntry oldEntry;
    public CalendarEntry newEntry;

    public OldNewEntryPair(CalendarEntry oldEntry, CalendarEntry newEntry) {
        this.oldEntry = oldEntry;
        this.newEntry = newEntry;
    }

}
