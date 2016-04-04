package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DeadlineTask extends GenericEntry {

    public static final String CLASS_NAME = "DeadlineTask";

    private LocalDateTime deadline;

    public DeadlineTask(String name, LocalDateTime deadline) {
        super(name);
        this.setDateTime(CalendarProperty.END, deadline);
    }

    public DeadlineTask(DeadlineTask entry) {
        super(entry);
        this.setDateTime(CalendarProperty.END, deadline);
    }

    @Override
    public LocalDateTime getDateTime(CalendarProperty property) {
        if (property.equals(CalendarProperty.END)) {
            return this.deadline;
        } else {
            return super.getDateTime(property);
        }
    }

    @Override
    public void setDateTime(CalendarProperty property, LocalDateTime deadline) {
        if (property.equals(CalendarProperty.END)) {
            this.deadline = deadline;
        } else {
            super.setDateTime(property, deadline);
        }
    }

    @Override
    public boolean isProperty(CalendarProperty property) {
        switch (property) {
            case OVER :
                return LocalDateTime.now().isAfter(this.deadline);
            case ONGOING :
                return LocalDateTime.now().isBefore(this.deadline);
            case TODAY :
                return LocalDate.now().equals(this.deadline.toLocalDate());
            default :
                return super.isProperty(property);
        }
    }

}
