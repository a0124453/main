package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

//@@author A0091173J
public class AddRecurringCommand extends AddCommand {

    private static final int OCCUR_INF = -1;

    private Period recurringPeriod;

    private int occurLimit = OCCUR_INF;

    private LocalDate dateLimit = null;

    public AddRecurringCommand(String name, LocalDateTime dueDateTime, Period recurringPeriod) {
        super(name, dueDateTime);

        assert recurringPeriod != null;

        this.recurringPeriod = recurringPeriod;
    }

    public AddRecurringCommand(String name, LocalDateTime dueDateTime, Period recurringPeriod, int occurLimit) {
        this(name, dueDateTime, recurringPeriod);
        this.occurLimit = occurLimit;
    }

    public AddRecurringCommand(String name, LocalDateTime dueDateTime, Period recurringPeriod,
            LocalDate dateLimit) {
        this(name, dueDateTime, recurringPeriod);
        this.dateLimit = dateLimit;
    }

    public AddRecurringCommand(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod) {
        super(name, startDateTime, endDateTime);

        assert recurringPeriod != null;

        this.recurringPeriod = recurringPeriod;
    }

    public AddRecurringCommand(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod, int occurLimit) {
        this(name, startDateTime, endDateTime, recurringPeriod);
        this.occurLimit = occurLimit;
    }

    public AddRecurringCommand(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod, LocalDate dateLimit) {
        this(name, startDateTime, endDateTime, recurringPeriod);
        this.dateLimit = dateLimit;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        int newId = addToCalendar(calendar);

        setAddedEntryID(newId);
        addHighlightEntry(newId);

        setExecuted(true);

        setComment(String.format(MESSAGE_ADDED, getName()));

        return calendar;
    }

    private int addToCalendar(CalendarList calendar) {
        if (getStartDateTime() == null) {
            return addAsTask(calendar);
        } else {
            return addAsEvent(calendar);
        }
    }

    private int addAsTask(CalendarList calendar) {
        if (dateLimit != null) {
            return calendar.add(getName(), getEndDateTime(), recurringPeriod, dateLimit);
        } else if (occurLimit != OCCUR_INF) {
            return calendar.add(getName(), getEndDateTime(), recurringPeriod, occurLimit);
        } else {
            return calendar.add(getName(), getEndDateTime(), recurringPeriod);
        }
    }

    private int addAsEvent(CalendarList calendar) {
        if (dateLimit != null) {
            return calendar.add(getName(), getStartDateTime(), getEndDateTime(), recurringPeriod, dateLimit);
        } else if (occurLimit != OCCUR_INF) {
            return calendar.add(getName(), getStartDateTime(), getEndDateTime(), recurringPeriod, occurLimit);
        } else {
            return calendar.add(getName(), getStartDateTime(), getEndDateTime(), recurringPeriod);
        }
    }
}
