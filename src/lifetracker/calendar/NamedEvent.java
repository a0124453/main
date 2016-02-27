package lifetracker.calendar;

public class NamedEvent implements Event {

	//variables
	private String name;
	private Date startDate;
	private Date endDate;
	private Time startTime;
	private Time endTime;
	
	//constructor
	public NamedEvent(String name, Date startDate, Date endDate, Time startTime, Time endTime){
		this.setName(name);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
	}
	
	//functions
	public void printEvent(){
		System.out.printf("%s ", name);
		startDate.printDate();
		startTime.printTime();
		System.out.printf("to ");
		endDate.printDate();
		endTime.printTime();
		System.out.printf("\n");
	}

	//get() and set() functions for variables
	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#getStartDate()
	 */
	@Override
	public Date getStartDate() {
		return startDate;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#setStartDate(lifetracker.calendar.Date)
	 */
	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#getEndDate()
	 */
	@Override
	public Date getEndDate() {
		return endDate;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#setEndDate(lifetracker.calendar.Date)
	 */
	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#getStartTime()
	 */
	@Override
	public Time getStartTime() {
		return startTime;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#setStartTime(lifetracker.calendar.Time)
	 */
	@Override
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#getEndTime()
	 */
	@Override
	public Time getEndTime() {
		return endTime;
	}

	/* (non-Javadoc)
	 * @see lifetracker.calendar.EventI#setEndTime(lifetracker.calendar.Time)
	 */
	@Override
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
}
