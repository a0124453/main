package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarProperty;
import lifetracker.calendar.DeadlineTask;
import lifetracker.calendar.Event;
import lifetracker.calendar.GenericEntry;
import lifetracker.calendar.RecurringEvent;
import lifetracker.calendar.RecurringTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class EntryToRecurringEventVisitor implements EntryVisitor<OldNewEntryPair> {

    private static final String ERROR_EMPTY_START = "Event start date time cannot be empty!";
    private static final String ERROR_EMPTY_END = "Event end date time cannot be empty!";
    private static final String ERROR_EMPTY_RECURRING = "Event recurring period cannot be empty!";

    private static final int LIMIT_INF = -2;
    private static final int LIMIT_DATE = -1;

    private final String name;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final Period recurringPeriod;
    private final int occurLimit;
    private final LocalDate limitDate;
    private final boolean isLimitKept;

    public EntryToRecurringEventVisitor(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod,
            boolean isLimitKept) {
        this.recurringPeriod = recurringPeriod;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.name = name;
        occurLimit = LIMIT_INF;
        limitDate = null;
        this.isLimitKept = isLimitKept;
    }

    public EntryToRecurringEventVisitor(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod, int occurLimit) {
        assert occurLimit > 0;

        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.recurringPeriod = recurringPeriod;
        this.occurLimit = occurLimit;
        this.limitDate = null;
        this.isLimitKept = false;
    }

    public EntryToRecurringEventVisitor(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod,
            LocalDate limitDate) {

        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.recurringPeriod = recurringPeriod;
        this.occurLimit = LIMIT_DATE;
        this.limitDate = limitDate;
        this.isLimitKept = false;
    }

    @Override
    public OldNewEntryPair visit(GenericEntry entry) {
        if (startDateTime == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_START);
        }
        if (endDateTime == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_END);
        }
        if (recurringPeriod == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_RECURRING);
        }

        RecurringEvent event = new RecurringEvent(entry.getName(), startDateTime, endDateTime, recurringPeriod);
        event.setId(entry.getId());

        return edit(entry, event);
    }

    @Override
    public OldNewEntryPair visit(DeadlineTask task) {
        if (startDateTime == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_START);
        }
        if (recurringPeriod == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_RECURRING);
        }

        RecurringEvent event = new RecurringEvent(task.getName(), startDateTime,
                task.getDateTime(CalendarProperty.END), recurringPeriod);

        event.setId(task.getId());

        return edit(task, event);
    }

    @Override
    public OldNewEntryPair visit(RecurringTask task) {
        if (startDateTime == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_START);
        }

        RecurringEvent event = createRecurringEventFromRecurringTask(task);
        event.setId(task.getId());

        return edit(task, event);
    }

    @Override
    public OldNewEntryPair visit(Event event) {
        if (recurringPeriod == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_RECURRING);
        }

        RecurringEvent newEvent = new RecurringEvent(event.getName(), event.getDateTime(CalendarProperty.START),
                event.getDateTime(CalendarProperty.END), recurringPeriod);
        newEvent.setId(event.getId());

        return edit(event, newEvent);
    }

    @Override
    public OldNewEntryPair visit(RecurringEvent event) {
        RecurringEvent clone = new RecurringEvent(event);

        return edit(clone, event);
    }

    private OldNewEntryPair edit(CalendarEntry clone, RecurringEvent event) {
        if (name != null && !name.isEmpty()) {
            event.setName(name);
        }

        if (startDateTime != null) {
            event.setDateTime(CalendarProperty.START, startDateTime);
        }

        if (endDateTime != null) {
            event.setDateTime(CalendarProperty.END, endDateTime);
        }

        if (recurringPeriod != null) {
            event.setPeriod(recurringPeriod);
        }

        if (occurLimit == LIMIT_INF && !isLimitKept) {
            event.removeLimit();
        } else if (occurLimit == LIMIT_DATE && limitDate != null) {
            event.setDateTime(CalendarProperty.DATE_LIMIT, limitDate.atStartOfDay());
        } else if (occurLimit != LIMIT_INF && occurLimit != LIMIT_DATE) {
            event.setOccurrenceLimit(occurLimit);
        }

        return new OldNewEntryPair(clone, event);
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
