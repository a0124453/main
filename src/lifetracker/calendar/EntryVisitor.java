package lifetracker.calendar;

public interface EntryVisitor<T> {
    T visit(GenericEntry entry);

    T visit(DeadlineTask task);

    T visit(RecurringTask task);

    T visit(Event event);

    T visit(RecurringEvent event);
}
