package lifetracker.calendar;

public interface Task {

	//get() and set() functions for variables
	String getName();

	void setName(String name);

	Date getDeadlineDate();

	void setDeadlineDate(Date deadlineDate);

	Time getDeadlineTime();

	void setDeadlineTime(Time deadlineTime);

}