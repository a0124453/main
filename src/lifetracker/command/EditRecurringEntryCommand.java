package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarProperty;

import java.time.LocalDate;
import java.time.Period;

/**
 * @author Shen Yichen <2007.yichen@gmail.com>
 */
public class EditRecurringEntryCommand extends EditGenericTaskCommand {

    static final int OCCUR_INF = -1;
    protected static final int OCCUR_DATE = -2;

    final Period recurringPeriod;
    final int occurLimit;
    protected final LocalDate dateLimit;

    public EditRecurringEntryCommand(int id, String name, Period recurringPeriod, boolean isForcedConvert) {
        super(id, name, isForcedConvert);
        this.recurringPeriod = recurringPeriod;
        this.occurLimit = OCCUR_INF;
        this.dateLimit = null;
    }

    public EditRecurringEntryCommand(int id, String name, Period recurringPeriod, int occurLimit) {
        super(id, name, true);
        this.recurringPeriod = recurringPeriod;
        this.occurLimit = occurLimit;
        this.dateLimit = null;
    }

    public EditRecurringEntryCommand(int id, String name, Period recurringPeriod, LocalDate dateLimit) {
        super(id, name, true);
        this.recurringPeriod = recurringPeriod;
        this.occurLimit = OCCUR_DATE;
        this.dateLimit = dateLimit;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        CalendarEntry entryToEdit = calendar.get(id);

        if (entryToEdit.getDateTime(CalendarProperty.START) == null) {
            if (occurLimit == OCCUR_INF) {
                oldEntry = calendar.updateToRecurringTask(id, name, null, recurringPeriod, !isForcedConvert, true);
            } else if (occurLimit == OCCUR_DATE) {
                oldEntry = calendar.updateToRecurringTask(id, name, null, recurringPeriod, dateLimit, true);
            } else {
                oldEntry = calendar.updateToRecurringTask(id, name, null, recurringPeriod, occurLimit, true);
            }
        } else {
            if (occurLimit == OCCUR_INF) {
                oldEntry = calendar.updateToRecurringEvent(id, name, null, null, recurringPeriod, !isForcedConvert);
            } else if (occurLimit == OCCUR_DATE) {
                oldEntry = calendar.updateToRecurringEvent(id, name, null, null, recurringPeriod, dateLimit);
            } else {
                oldEntry = calendar.updateToRecurringEvent(id, name, null, null, recurringPeriod, occurLimit);
            }
        }

        addHighlightEntry(id);

        setComment(String.format(MESSAGE_EDITED, id));
        setExecuted(true);

        return calendar;
    }
}
