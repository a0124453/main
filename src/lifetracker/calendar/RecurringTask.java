package lifetracker.calendar;

import lifetracker.calendar.visitor.EntryVisitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class RecurringTask extends DeadlineTask {

    private final String SERIAL_TYPE_IDENTIFIER = "RecurringTask";

    private static final int INF_LIMIT = -1;
    private static final int DATE_LIMIT = -2;
    private static final String MESSAGE_ERROR_NEGATIVE_LIMIT = "Number of occurences must be positive!";
    private static final String MESSAGE_ERROR_INVALID_LIMIT_DATE = "Deadline cannot be before limit date!";

    private Period period;
    private int occcurenceLimit;
    private LocalDate dateLimit;

    public RecurringTask(String name, LocalDateTime deadline, Period period) {
        super(name, deadline);
        this.period = period;
        this.occcurenceLimit = INF_LIMIT;
    }

    public RecurringTask(String name, LocalDateTime deadline, Period period, int limit) {
        super(name, deadline);
        this.period = period;
        if (limit <= 0) {
            throw new IllegalArgumentException(MESSAGE_ERROR_NEGATIVE_LIMIT);
        }
        this.occcurenceLimit = limit;
    }

    public RecurringTask(String name, LocalDateTime deadline, Period period, LocalDate limit) {
        super(name, deadline);
        if (limit.isBefore(deadline.toLocalDate())) {
            throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_LIMIT_DATE);
        }
        this.period = period;
        this.dateLimit = limit;
        this.occcurenceLimit = DATE_LIMIT;
    }

    public RecurringTask(RecurringTask entry) {
        super(entry);
        this.period = entry.period;
        this.occcurenceLimit = entry.occcurenceLimit;
        this.dateLimit = entry.dateLimit;
    }

    @Override
    public LocalDateTime getDateTime(CalendarProperty property) {
        switch (property) {
            case LIMIT:
                if (occcurenceLimit == DATE_LIMIT) {
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
            case LIMIT:
                occcurenceLimit = DATE_LIMIT;
                if (dateTime.toLocalDate().isBefore(this.getDateTime(CalendarProperty.END).toLocalDate())) {
                    throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_LIMIT_DATE);
                } else {
                    this.dateLimit = dateTime.toLocalDate();
                    this.occcurenceLimit = DATE_LIMIT;
                }
                break;
            case END:
                if (occcurenceLimit == DATE_LIMIT && this.dateLimit.isBefore(dateTime.toLocalDate())) {
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
    public boolean isProperty(CalendarProperty property) {
        switch (property) {
            case RECURRING:
                return true;
            default:
                return super.isProperty(property);
        }
    }

    public int getOcccurenceLimit() {
        return occcurenceLimit;
    }

    public boolean hasNext() {
        if (this.occcurenceLimit == INF_LIMIT) {
            return true;
        } else if (this.occcurenceLimit == DATE_LIMIT) {
            return this.getDateTime(CalendarProperty.END).toLocalDate().plus(period).isBefore(dateLimit);
        } else {
            return occcurenceLimit > 1;
        }
    }

    public void updateToNext() {
        if (hasNext()) {
            LocalDateTime currentDeadline = getDateTime(CalendarProperty.END);
            setDateTime(CalendarProperty.END, currentDeadline.plus(this.period));

            if (occcurenceLimit > 0) {
                occcurenceLimit--;
            }
        }
    }

    public void setOccurrenceLimit(int n) {
        assert n > 0;
        occcurenceLimit = n;
    }

    public void removeLimit() {
        occcurenceLimit = INF_LIMIT;
    }

    @Override
    public <T> T accept(EntryVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
