package lifeTracker_calendar;

public class time {

	//variables
	private int hour;
	private int minute;
	
	//constructor
	public time(int hour, int minute){
		this.setHour(hour);
		this.setMinute(minute);
	}
	
	//functions
	public void printTime(){
		System.out.printf("%d:%d ", hour, minute);
	}
	
	//get() and set() functions for variables
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
}
