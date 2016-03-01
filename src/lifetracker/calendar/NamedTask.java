package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NamedTask implements Task {

    // variables
    private String name;
    private LocalDateTime deadline;

    // constructor
    public NamedTask(String name, LocalDateTime deadline) {
        this.setName(name);
        this.setDeadline(deadline);
    }

    // functions
    public void printTask() {
        System.out.printf("%s by ", name);
        this.printDeadline();
        System.out.printf("\n");
    }

    public void printDeadline() {
        System.out.println(deadline.format(DateTimeFormatter.ofPattern("EEE dd/MM/uu h:mm a")));
    }

    // get() and set() functions for variables
    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.Task#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.Task#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.Task#getDeadline()
     */
    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.Task#setDeadline(java.time.LocalDateTime)
     */
    @Override
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.Task#isDueToday()
     */
    public boolean isDueToday() {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = deadline.toLocalDate();
        return dueDate.equals(today);
    }

    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.Task#isOverdue()
     */
    public boolean isOverdue() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(deadline);
    }

}
