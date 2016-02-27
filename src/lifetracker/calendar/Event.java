package lifetracker.calendar;

public interface Event {

	//get() and set() functions for variables
	String getName();

	void setName(String name);

	Date getStartDate();

	void setStartDate(Date startDate);

	Date getEndDate();

	void setEndDate(Date endDate);

	Time getStartTime();

	void setStartTime(Time startTime);

	Time getEndTime();

	void setEndTime(Time endTime);

}