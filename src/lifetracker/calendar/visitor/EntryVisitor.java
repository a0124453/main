package lifetracker.calendar.visitor;

import lifetracker.calendar.DeadlineTask;
import lifetracker.calendar.Event;
import lifetracker.calendar.GenericEntry;
import lifetracker.calendar.RecurringEvent;
import lifetracker.calendar.RecurringTask;

public interface EntryVisitor<T> {
    T visit(GenericEntry entry);

    T visit(DeadlineTask task);

    T visit(RecurringTask task);

    T visit(Event event);

    T visit(RecurringEvent event);
}
