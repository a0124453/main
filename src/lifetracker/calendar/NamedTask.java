package lifetracker.calendar;

public class NamedTask implements Task {

	//variables
	private String name;
	private Date deadlineDate;
	private Time deadlineTime;
	
	//constructor
	public NamedTask(String name, Date deadlineDate, Time deadlineTime){
		this.setName(name);
		this.setDeadlineDate(deadlineDate);
		this.setDeadlineTime(deadlineTime);
	}
	
	//functions
	public void printTask(){
		System.out.printf("%s by ", name);
		deadlineDate.printDate();
		deadlineTime.printTime();
		System.out.printf("\n");
	}
	
	//get() and set() functions for variables
	/* (non-Javadoc)
	 * @see lifetracker.calendar.Task#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see lifetracker.calendar.Task#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see lifetracker.calendar.Task#getDeadlineDate()
	 */
	@Override
	public Date getDeadlineDate() {
		return deadlineDate;
	}
	/* (non-Javadoc)
	 * @see lifetracker.calendar.Task#setDeadlineDate(lifetracker.calendar.Date)
	 */
	@Override
	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	/* (non-Javadoc)
	 * @see lifetracker.calendar.Task#getDeadlineTime()
	 */
	@Override
	public Time getDeadlineTime() {
		return deadlineTime;
	}
	/* (non-Javadoc)
	 * @see lifetracker.calendar.Task#setDeadlineTime(lifetracker.calendar.Time)
	 */
	@Override
	public void setDeadlineTime(Time deadlineTime) {
		this.deadlineTime = deadlineTime;
	}
}
