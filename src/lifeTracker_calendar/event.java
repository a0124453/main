package lifeTracker_calendar;

public class event {

	//variables
	private String name;
	private date startDate;
	private date endDate;
	private time startTime;
	private time endTime;
	
	//constructor
	public event(String name, date startDate, date endDate, time startTime, time endTime){
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

	public date getStartDate() {
		return startDate;
	}

	public void setStartDate(date startDate) {
		this.startDate = startDate;
	}

	public date getEndDate() {
		return endDate;
	}

	public void setEndDate(date endDate) {
		this.endDate = endDate;
	}

	public time getStartTime() {
		return startTime;
	}

	public void setStartTime(time startTime) {
		this.startTime = startTime;
	}

	public time getEndTime() {
		return endTime;
	}

	public void setEndTime(time endTime) {
		this.endTime = endTime;
	}
}
