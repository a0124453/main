package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.DeadlineTask;
import lifetracker.calendar.Event;
import lifetracker.calendar.GenericEntry;
import lifetracker.calendar.RecurringEvent;
import lifetracker.calendar.RecurringTask;

public class EntryToGenericTaskVisitor implements EntryVisitor<CalendarEntry> {

    private String name;

    public EntryToGenericTaskVisitor(String name) {
        this.name = name;
    }

    @Override
    public CalendarEntry visit(GenericEntry entry) {
        return genericVisit(entry);
    }

    @Override
    public CalendarEntry visit(DeadlineTask task) {
        return genericVisit(task);
    }

    @Override
    public CalendarEntry visit(RecurringTask task) {
        return genericVisit(task);
    }

    @Override
    public CalendarEntry visit(Event event) {
        return genericVisit(event);
    }

    @Override
    public CalendarEntry visit(RecurringEvent event) {
        return genericVisit(event);
    }

    private CalendarEntry genericVisit(GenericEntry entry){
        CalendarEntry clone = new GenericEntry(entry);

        if(name!=null && !name.isEmpty()){
            entry.setName(name);
        }

        return clone;
    }
}
