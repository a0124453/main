package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarProperty;
import lifetracker.calendar.DeadlineTask;
import lifetracker.calendar.Event;
import lifetracker.calendar.GenericEntry;
import lifetracker.calendar.RecurringEvent;
import lifetracker.calendar.RecurringTask;

//@@author A0091173J

public class MarkVisitor implements EntryVisitor<OldNewEntryPair> {

    @Override
    public OldNewEntryPair visit(GenericEntry entry) {
        GenericEntry clone = new GenericEntry(entry);
        entry.toggleActive();
        return new OldNewEntryPair(clone, null);
    }

    @Override
    public OldNewEntryPair visit(DeadlineTask task) {
        DeadlineTask clone = new DeadlineTask(task);
        task.toggleActive();
        return new OldNewEntryPair(clone, null);
    }

    /**
     * This method mutates the input task into the next instance, then returns a pair containing a copy of the task
     * before it was mutated, as well as a new deadline task to be archived.
     *
     * @param task The recurring task to mark
     * @return A pair of entries, containing the un-mutated entry and the new entry
     */
    @Override
    public OldNewEntryPair visit(RecurringTask task) {
        RecurringTask clone = new RecurringTask(task);
        DeadlineTask doneTask = new DeadlineTask(task);
        doneTask.toggleActive();

        if (task.hasNext()) {
            //Set an invalid ID.
            doneTask.setId(CalendarList.BASE_ID);
            task.updateToNext();
        }

        return new OldNewEntryPair(clone, doneTask);
    }

    @Override
    public OldNewEntryPair visit(Event event) {
        Event clone = new Event(event);
        event.toggleActive();
        return new OldNewEntryPair(clone, null);
    }

    @Override
    public OldNewEntryPair visit(RecurringEvent event) {
        RecurringEvent clone = new RecurringEvent(event);
        Event doneEvent = recurringEventToEvent(event);
        doneEvent.toggleActive();

        if(event.hasNext()) {
            doneEvent.setId(CalendarList.BASE_ID);
            event.updateToNext();
        }
        return new OldNewEntryPair(clone, doneEvent);
    }

    private Event recurringEventToEvent(RecurringEvent event){
        Event newEvent = new Event(event.getName(), event.getDateTime(CalendarProperty.START), event.getDateTime(CalendarProperty.END));
        newEvent.setId(event.getId());
        return newEvent;
    }
}
