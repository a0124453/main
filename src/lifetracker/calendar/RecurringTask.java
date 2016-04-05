package lifetracker.calendar;

import lifetracker.calendar.visitor.EntryVisitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class RecurringTask extends DeadlineTask {

    private final String  SERIAL_TYPE_IDENTIFIER = "RecurringTask";

    private static final int INF_LIMIT = -1;
    private static final int DATE_LIMIT = -2;
    private static final String MESSAGE_ERROR_NEGATIVE_LIMIT = "Number of occurences must be positive!";
    private static final String MESSAGE_ERROR_INVALID_LIMIT_DATE = "Deadline cannot be before limit date!";

    private Period period;
    private int currentOccurence = 1;
    private int limitOccurences;
    private LocalDate limitDate;

    public RecurringTask(String name, LocalDateTime deadline, Period period) {
        super(name, deadline);
        this.period = period;
        this.limitOccurences = INF_LIMIT;
    }

    public RecurringTask(String name, LocalDateTime deadline, Period period, int limit) {
        super(name, deadline);
        this.period = period;
        if (limit <= 0) {
            throw new IllegalArgumentException(MESSAGE_ERROR_NEGATIVE_LIMIT);
        }
        this.limitOccurences = limit;
    }

    public RecurringTask(String name, LocalDateTime deadline, Period period, LocalDate limit) {
        super(name, deadline);
        if (limit.isBefore(deadline.toLocalDate())) {
            throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_LIMIT_DATE);
        }
        this.period = period;
        this.limitDate = limit;
        this.limitOccurences = DATE_LIMIT;
    }

    public RecurringTask(RecurringTask entry) {
        super(entry);
        this.period = entry.period;
        this.limitOccurences = entry.limitOccurences;
        this.currentOccurence = entry.currentOccurence;
        this.limitDate = entry.limitDate;
    }

    @Override
    public LocalDateTime getDateTime(CalendarProperty property) {
        switch (property) {
            case LIMIT :
                if (limitOccurences == DATE_LIMIT) {
                    return this.limitDate.atStartOfDay();
                } else {
                    return null;
                }
            default :
                return super.getDateTime(property);
        }
    }

    @Override
    public void setDateTime(CalendarProperty property, LocalDateTime dateTime) {
        switch (property) {
            case LIMIT :
                limitOccurences = DATE_LIMIT;
                if (dateTime.toLocalDate().isBefore(this.getDateTime(CalendarProperty.END).toLocalDate())) {
                    throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_LIMIT_DATE);
                } else {
                    this.limitDate = dateTime.toLocalDate();
                }
                break;
            case END :
                if (limitOccurences == DATE_LIMIT && this.limitDate.isBefore(dateTime.toLocalDate())) {
                    throw new IllegalArgumentException(MESSAGE_ERROR_INVALID_LIMIT_DATE);
                }
                // Fallthrough
            default :
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

    /*
     * @Override public void mark() { // TODO Auto-generated method stub
     * super.mark(); }
     */

    @Override
    public boolean isProperty(CalendarProperty property) {
        switch (property) {
            case RECURRING :
                return true;
            default :
                return super.isProperty(property);
        }
    }

    public boolean hasNext() {
        if (this.limitOccurences == INF_LIMIT) {
            return true;
        } else if (this.limitOccurences == DATE_LIMIT) {
            return this.getDateTime(CalendarProperty.END).toLocalDate().plus(period).isBefore(limitDate);
        } else {
            return currentOccurence < limitOccurences;
        }
    }

    public void updateToNext() {
        if (hasNext()) {
            LocalDateTime currentDeadline = getDateTime(CalendarProperty.END);
            setDateTime(CalendarProperty.END, currentDeadline.plus(this.period));
            currentOccurence++;
        }
    }

    public void setOccurrenceLimit(int n) {
        if (limitOccurences == DATE_LIMIT || limitOccurences == INF_LIMIT) {
            currentOccurence = 1;
            limitDate = null;
            this.limitOccurences = n;
        } else if (n < currentOccurence) {
            limitOccurences = currentOccurence;
        } else {
            limitOccurences = n;
        }
    }

    @Override
    public <T> T accept(EntryVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
