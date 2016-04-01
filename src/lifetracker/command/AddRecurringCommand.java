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

        int entryID;

        if (getStartDateTime() == null) {
            entryID = calendar.add(getName(), getEndDateTime(), recurringPeriod);
        } else {
            entryID = calendar.add(getName(), getStartDateTime(), getEndDateTime(), recurringPeriod);
        }

        setAddedEntryID(entryID);

        setExecuted(true);

        setComment(String.format(MESSAGE_ADDED, getName()));

        return calendar;
    }
}
