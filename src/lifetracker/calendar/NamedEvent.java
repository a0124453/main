package lifetracker.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NamedEvent implements Event {

    // variables
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    // constructor
    public NamedEvent(String name, LocalDateTime start, LocalDateTime end) {
        this.setName(name);
        this.setStart(start);
        this.setEnd(end);
    }

    // functions
    public void printEvent() {
        System.out.printf("%s ", name);
        this.printDateTime(startDateTime);
        System.out.printf("to ");
        this.printDateTime(endDateTime);
        System.out.printf("\n");
    }

    public void printDateTime(LocalDateTime dateTime) {
        System.out.println(dateTime.format(DateTimeFormatter.ofPattern("EEE dd/MM/uu h:mm a")));
    }

    // get() and set() functions for variables
    /*
     * (non-Javadoc)
     * 
     * @see lifetracker.calendar.EventI#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#getStart()
     */
    @Override
    public LocalDateTime getStart() {
        return startDateTime;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#setStar(java.time.LocalDateTime)
     */
    @Override
    public void setStart(LocalDateTime start) {
        this.startDateTime = start;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#getEnd()
     */
    @Override
    public LocalDateTime getEnd() {
        return endDateTime;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#setEnd(java.time.LocalDateTime)
     */
    @Override
    public void setEnd(LocalDateTime end) {
        this.endDateTime = end;
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#getStartTime()
     */
    @Override
    public LocalTime getStartTime() {
        return startDateTime.toLocalTime();
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#getEndTime()
     */
    @Override
    public LocalTime getEndTime() {
        return endDateTime.toLocalTime();
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#isToday()
     */
    @Override
    public boolean isToday() {
        LocalDate eventStartDay = startDateTime.toLocalDate();
        LocalDate today = LocalDate.now();
        return eventStartDay.equals(today);
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#isOngoing()
     */
    @Override
    public boolean isOngoing() {
        LocalDateTime now = LocalDateTime.now();
        boolean hasStarted = now.isAfter(startDateTime);
        return (hasStarted && !isOver());
    }

    /*
     * (non-Javadoc)
     *
     * @see lifetracker.calendar.EventI#isOver()
     */
    @Override
    public boolean isOver() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(endDateTime);
    }

}
