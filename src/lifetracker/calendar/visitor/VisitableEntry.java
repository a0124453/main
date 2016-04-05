package lifetracker.calendar.visitor;

public interface VisitableEntry {
    <T> T accept(EntryVisitor<T> visitor);
}
