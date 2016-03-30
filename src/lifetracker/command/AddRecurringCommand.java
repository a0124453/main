package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public class AddRecurringCommand extends AddCommand {

    private TemporalAmount recurringPeriod;

    public AddRecurringCommand(String name, LocalDateTime dueDateTime, TemporalAmount recurringPeriod) {
        super(name, dueDateTime);

        assert recurringPeriod != null;

        this.recurringPeriod = recurringPeriod;
    }

    public AddRecurringCommand(String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            TemporalAmount recurringPeriod) {
        super(name, startDateTime, endDateTime);

        assert recurringPeriod != null;

        this.recurringPeriod = recurringPeriod;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        assert calendar != null;

        if (getStartDateTime() == null) {
            calendar.add(getName(), getEndDateTime(), recurringPeriod);
        } else {
            calendar.add(getName(), getStartDateTime(), getEndDateTime(), recurringPeriod);
        }

        setExecuted(true);

        setComment(String.format(MESSAGE_ADDED, getName()));

        return calendar;
    }
}
