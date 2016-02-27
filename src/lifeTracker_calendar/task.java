package lifeTracker_calendar;

public class task {

	//variables
	private String name;
	private date deadlineDate;
	private time deadlineTime;
	
	//constructor
	public task(String name, date deadlineDate, time deadlineTime){
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public date getDeadlineDate() {
		return deadlineDate;
	}
	public void setDeadlineDate(date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	public time getDeadlineTime() {
		return deadlineTime;
	}
	public void setDeadlineTime(time deadlineTime) {
		this.deadlineTime = deadlineTime;
	}
}
