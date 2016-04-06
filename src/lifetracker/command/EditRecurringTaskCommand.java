package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class EditRecurringTaskCommand extends EditDeadlineTaskCommand {

    static final int OCCUR_INF = -1;
    static final int OCCUR_DATE = -2;

    final Period recurringPeriod;
    final int occurLimit;
    final LocalDate dateLimit;

    public EditRecurringTaskCommand(int id, String name, LocalDateTime endDateTime, Period recurringPeriod,
            boolean isForcedConvert) {
        super(id, name, endDateTime, isForcedConvert);
        this.recurringPeriod = recurringPeriod;
        this.occurLimit = OCCUR_INF;
        this.dateLimit = null;
    }

    public EditRecurringTaskCommand(int id, String name, LocalDateTime endDateTime, Period recurringPeriod,
            int occurLimit) {
        super(id, name, endDateTime, true);
        this.recurringPeriod = recurringPeriod;
        this.occurLimit = occurLimit;
        this.dateLimit = null;
    }

    public EditRecurringTaskCommand(int id, String name, LocalDateTime endDateTime, Period recurringPeriod,
            LocalDate dateLimit) {
        super(id, name, endDateTime, true);
        this.recurringPeriod = recurringPeriod;
        this.dateLimit = dateLimit;
        this.occurLimit = OCCUR_DATE;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        if (occurLimit == OCCUR_INF) {
            oldEntry = calendar.updateToRecurringTask(id, name, endDateTime, recurringPeriod, !isForcedConvert, true);
        } else if (occurLimit == OCCUR_DATE) {
            oldEntry = calendar
                    .updateToRecurringTask(id, name, endDateTime, recurringPeriod, dateLimit, true);
        } else{
            oldEntry = calendar.updateToRecurringTask(id, name, endDateTime, recurringPeriod, occurLimit, true);
        }

        setExecuted(true);
        setComment(String.format(MESSAGE_EDITED, id));
        return super.execute(calendar);
    }
}
