package lifetracker.calendar;

public class NamedEvent {

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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
}
