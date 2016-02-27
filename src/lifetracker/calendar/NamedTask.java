package lifetracker.calendar;

public class NamedTask {

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDeadlineDate() {
		return deadlineDate;
	}
	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	public Time getDeadlineTime() {
		return deadlineTime;
	}
	public void setDeadlineTime(Time deadlineTime) {
		this.deadlineTime = deadlineTime;
	}
}
