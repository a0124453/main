package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarProperty;
import lifetracker.calendar.DeadlineTask;
import lifetracker.calendar.Event;
import lifetracker.calendar.GenericEntry;
import lifetracker.calendar.RecurringEvent;
import lifetracker.calendar.RecurringTask;

import java.time.LocalDateTime;

public class EntryToEventVisitor implements EntryVisitor<OldNewEntryPair> {

    private static final String ERROR_EMPTY_START = "Event start date cannot be empty!";
    private static final String ERROR_EMPTY_END = "Event end date cannot be empty!";

    private final String name;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final boolean isForcedConvert;

    public EntryToEventVisitor(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            boolean isForcedConvert) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isForcedConvert = isForcedConvert;
    }

    @Override
    public OldNewEntryPair visit(GenericEntry entry) {
        if (startDateTime == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_START);
        }

        if (endDateTime == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_END);
        }

        Event event = new Event(entry.getName(), startDateTime, endDateTime);
        event.setId(entry.getId());

        return edit(entry, event);
    }

    @Override
    public OldNewEntryPair visit(DeadlineTask task) {
        if (startDateTime == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_START);
        }

        Event event = new Event(task.getName(), startDateTime, task.getDateTime(CalendarProperty.END));
        event.setId(task.getId());
        return edit(task, event);
    }

    @Override
    public OldNewEntryPair visit(RecurringTask task) {
        OldNewEntryPair pair;

        if (isForcedConvert) {
            pair = visit(new DeadlineTask(task));
        } else {
            pair = visit(createRecurringEventFromRecurringTask(task));
        }

        pair.oldEntry = task;
        return pair;
    }

    @Override
    public OldNewEntryPair visit(Event event) {
        Event clone = new Event(event);
        return edit(clone, event);
    }

    @Override
    public OldNewEntryPair visit(RecurringEvent event) {

        if (isForcedConvert) {
            Event newEvent = new Event(event.getName(), event.getDateTime(CalendarProperty.START),
                    event.getDateTime(CalendarProperty.END));
            newEvent.setId(event.getId());
            return edit(event, newEvent);
        } else {
            RecurringEvent clone = new RecurringEvent(event);
            return editCalendarEntry(clone, event);
        }
    }

    private OldNewEntryPair edit(CalendarEntry clone, Event newEvent) {
        if (isForcedConvert) {
            newEvent = new Event(newEvent);
        }

        return editCalendarEntry(clone, newEvent);
    }

    private OldNewEntryPair editCalendarEntry(CalendarEntry clone, CalendarEntry newEvent) {
        if (name != null && !name.isEmpty()) {
            newEvent.setName(name);
        }

        if (startDateTime != null) {
            newEvent.setDateTime(CalendarProperty.START, startDateTime);
        }

        if (endDateTime != null) {
            newEvent.setDateTime(CalendarProperty.END, endDateTime);
        }

        return new OldNewEntryPair(clone, newEvent);
    }

    private RecurringEvent createRecurringEventFromRecurringTask(RecurringTask task) {
        if (startDateTime == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_START);
        }

        RecurringEvent newEvent = new RecurringEvent(task.getName(), startDateTime,
                task.getDateTime(CalendarProperty.END), task.getPeriod());

        newEvent.setId(task.getId());

        if (task.isProperty(CalendarProperty.DATE_LIMITED)) {
            newEvent.setDateTime(CalendarProperty.DATE_LIMIT, task.getDateTime(CalendarProperty.DATE_LIMIT));
        } else if (task.isProperty(CalendarProperty.OCCURRENCE_LIMITED)) {
            newEvent.setOccurrenceLimit(task.getIntegerProperty(CalendarProperty.OCCURRENCE_LIMIT));
        }

        return newEvent;
    }
}
