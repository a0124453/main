package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

//@@author A0091173J
public class EditRecurringEventCommand extends EditRecurringTaskCommand {

    final LocalDateTime startDateTime;

    public EditRecurringEventCommand(int id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod, boolean isForcedConvert) {
        super(id, name, endDateTime, recurringPeriod, isForcedConvert);
        this.startDateTime = startDateTime;
    }

    public EditRecurringEventCommand(int id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod, int occurLimit) {
        super(id, name, endDateTime, recurringPeriod, occurLimit);
        this.startDateTime = startDateTime;
    }

    public EditRecurringEventCommand(int id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime,
            Period recurringPeriod, LocalDate dateLimit) {
        super(id, name, endDateTime, recurringPeriod, dateLimit);
        this.startDateTime = startDateTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        if (occurLimit == OCCUR_INF) {
            oldEntry = calendar
                    .updateToRecurringEvent(id, name, startDateTime, endDateTime, recurringPeriod, !isForcedConvert);
        } else if (occurLimit == OCCUR_DATE) {
            oldEntry = calendar
                    .updateToRecurringEvent(id, name, startDateTime, endDateTime, recurringPeriod, dateLimit);
        } else {
            oldEntry = calendar
                    .updateToRecurringEvent(id, name, startDateTime, endDateTime, recurringPeriod, occurLimit);
        }

        addHighlightEntry(id);

        setExecuted(true);
        setComment(String.format(MESSAGE_EDITED, id));

        return calendar;
    }
}
