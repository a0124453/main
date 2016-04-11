package lifetracker.calendar.visitor;

//@@author A0091173J

public interface VisitableEntry {
    <T> T accept(EntryVisitor<T> visitor);
}
