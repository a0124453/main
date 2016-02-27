package lifetracker.calendar;

import java.util.ArrayList;
import java.util.List;

public interface CalendarList {

	//get() and set() functions for variables
	List<Task> getTaskList();
	
	List<Event> getEventList();
	
	void addEvent(String name, Date startDate, Date endDate, Time startTime, Time endTime);
	
	void addTask(String name, Date deadlineDate, Time deadlineTime);
}
