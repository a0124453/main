package lifetracker.calendar;

public class Date {

	//variables
	private int year;
	private int month;
	private int day;

	//constructor
	public Date(int year, int month, int day) {
	    this.setYear(year);
	    this.setMonth(month);
	    this.setDay(day);
	}
	
	//functions
	public void printDate(){
		System.out.printf("%d-%d-%d ", year, month, day);
	}
	
	//get() and set() functions for variables
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
}
