package lifetracker.calendar;

import lifetracker.calendar.visitor.EntryVisitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class RecurringTask extends DeadlineTask {
    protected static final int INF_LIMIT_CONST = -1;
    protected static final int DATE_LIMIT_CONST = -2;
    private static final String MESSAGE_ERROR_NEGATIVE_LIMIT = "Number of occurrences must be positive!";
    private static final String MESSAGE_ERROR_INVALID_LIMIT_DATE = "Limit date cannot be before deadline/end date!";

    private Period period;
    private int occurrenceLimit;
    private LocalDate dateLimit;

    public RecurringTask(String name, LocalDateTime deadline, Period period) {
        super(name, deadline);
        this.period = period;
        this.occurrenceLimit = INF_LIMIT_CONST;
        SERIAL_TYPE_IDENTIFIER = "RecurringTask";
    }

    public RecurringTask(String name, LocalDateTime deadline, Period period, int limit) {
        super(name, deadline);
        this.period = period;
        if (limit <= 0) {
            throw new IllegalArgumentException(MESSAGE_ERROR_NEGATIVE_LIMIT);
        }
        this.occurrenceLimit = limit;
    }

    public RecurringTask(String name, LocalDateTime deadline, Period period, LocalDate limit) {
        super(name, deadline);
        if (limit.isBefore(deadline.toLocalDate())) {
            throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_LIMIT_DATE);
        }
        this.period = period;
        this.dateLimit = limit;
        this.occurrenceLimit = DATE_LIMIT_CONST;
    }

    public RecurringTask(RecurringTask entry) {
        super(entry);
        this.period = entry.period;
        this.occurrenceLimit = entry.occurrenceLimit;
        this.dateLimit = entry.dateLimit;
    }

    @Override
    public LocalDateTime getDateTime(CalendarProperty property) {
        switch (property) {
            case DATE_LIMIT:
                if (occurrenceLimit == DATE_LIMIT_CONST) {
                    return this.dateLimit.atStartOfDay();
                } else {
                    return null;
                }
            default:
                return super.getDateTime(property);
        }
    }

    @Override
    public void setDateTime(CalendarProperty property, LocalDateTime dateTime) {
        switch (property) {
            case DATE_LIMIT:
                occurrenceLimit = DATE_LIMIT_CONST;
                if (dateTime.toLocalDate().isBefore(this.getDateTime(CalendarProperty.END).toLocalDate())) {
                    throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_LIMIT_DATE);
                } else {
                    this.dateLimit = dateTime.toLocalDate();
                    this.occurrenceLimit = DATE_LIMIT_CONST;
                }
                break;
            case END:
                if (occurrenceLimit == DATE_LIMIT_CONST && this.dateLimit.isBefore(dateTime.toLocalDate())) {
                    throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_LIMIT_DATE);
                }
                // Fallthrough
            default:
                super.setDateTime(property, dateTime);
        }
    }

    @Override
    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public int getIntegerProperty(CalendarProperty property) {
        switch (property) {
            case OCCURRENCE_LIMIT:
                return occurrenceLimit;
            default:
                return super.getIntegerProperty(property);
        }
    }

    @Override
    public boolean isProperty(CalendarProperty property) {
        switch (property) {
            case RECURRING:
                return true;
            case DATE_LIMITED:
                return occurrenceLimit == DATE_LIMIT_CONST;
            case OCCURRENCE_LIMITED:
                return occurrenceLimit > 0;
            case ACTIVE:
                return true;
            default:
                return super.isProperty(property);
        }
    }

    public boolean hasNext() {
        if (this.occurrenceLimit == INF_LIMIT_CONST) {
            return true;
        } else if (this.occurrenceLimit == DATE_LIMIT_CONST) {
            LocalDate newDate = getDateTime(CalendarProperty.END).toLocalDate().plus(period);

            return newDate.isBefore(dateLimit) || newDate.isEqual(dateLimit);
        } else {
            return occurrenceLimit > 1;
        }
    }

    public void updateToNext() {
        if (hasNext()) {
            forceUpdateDate();
        }
    }

    public void setOccurrenceLimit(int n) {
        assert n > 0;
        occurrenceLimit = n;
    }

    public void removeLimit() {
        occurrenceLimit = INF_LIMIT_CONST;
    }

    protected void forceUpdateDate() {
        LocalDateTime currentDeadline = getDateTime(CalendarProperty.END);
        super.setDateTime(CalendarProperty.END, currentDeadline.plus(this.period));

        if (occurrenceLimit > 0) {
            occurrenceLimit--;
        }
    }

    @Override
    public <T> T accept(EntryVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
