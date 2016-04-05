package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.DeadlineTask;
import lifetracker.calendar.Event;
import lifetracker.calendar.GenericEntry;
import lifetracker.calendar.RecurringEvent;
import lifetracker.calendar.RecurringTask;

public class EntryToGenericTaskVisitor implements EntryVisitor<EditedEntryPair> {

    private String name;
    private boolean isConvertForced;

    public EntryToGenericTaskVisitor(String name, boolean isConvertForced) {
        this.name = name;
        this.isConvertForced = isConvertForced;
    }

    @Override
    public EditedEntryPair visit(GenericEntry entry) {
        GenericEntry clone = new GenericEntry(entry);

        return edit(clone, entry);
    }

    @Override
    public EditedEntryPair visit(DeadlineTask task) {
        DeadlineTask clone = new DeadlineTask(task);

        return edit(clone, task);
    }

    @Override
    public EditedEntryPair visit(RecurringTask task) {
        RecurringTask clone = new RecurringTask(task);

        return edit(clone, task);
    }

    @Override
    public EditedEntryPair visit(Event event) {
        Event clone = new Event(event);

        return edit(clone, event);
    }

    @Override
    public EditedEntryPair visit(RecurringEvent event) {
        RecurringEvent clone = new RecurringEvent(event);

        return edit(clone, event);
    }

    private EditedEntryPair edit(CalendarEntry clone, GenericEntry newEntry) {
        if (isConvertForced){
            newEntry = new GenericEntry(newEntry);
        }

        if (name != null && !name.isEmpty()) {
            newEntry.setName(name);
        }

        return new EditedEntryPair(clone, newEntry);
    }
}
