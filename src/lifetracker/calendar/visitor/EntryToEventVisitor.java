package lifetracker.calendar.visitor;

import lifetracker.calendar.DeadlineTask;
import lifetracker.calendar.Event;
import lifetracker.calendar.GenericEntry;
import lifetracker.calendar.RecurringEvent;
import lifetracker.calendar.RecurringTask;

public class EntryToEventVisitor implements EntryVisitor<OldNewEntryPair> {
    @Override
    public OldNewEntryPair visit(GenericEntry entry) {
        return null;
    }

    @Override
    public OldNewEntryPair visit(DeadlineTask task) {
        return null;
    }

    @Override
    public OldNewEntryPair visit(RecurringTask task) {
        return null;
    }

    @Override
    public OldNewEntryPair visit(Event event) {
        return null;
    }

    @Override
    public OldNewEntryPair visit(RecurringEvent event) {
        return null;
    }
}
