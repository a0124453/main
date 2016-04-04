package lifetracker.calendar;

public class EntryEditVisitor implements EntryVisitor<CalendarEntry> {

    public EntryEditVisitor(){

    }

    @Override
    public CalendarEntry visit(GenericEntry entry) {
        return null;
    }

    @Override
    public CalendarEntry visit(DeadlineTask task) {
        return null;
    }

    @Override
    public CalendarEntry visit(RecurringTask task) {
        return null;
    }

    @Override
    public CalendarEntry visit(Event event) {
        return null;
    }

    @Override
    public CalendarEntry visit(RecurringEvent event) {
        return null;
    }
}
