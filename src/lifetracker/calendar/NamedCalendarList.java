package lifetracker.calendar;

import java.util.ArrayList;
import java.util.List;

public class NamedCalendarList implements CalendarList{

	//variables
	private List<Task> taskList = new ArrayList<>();
	private List<Event> eventList = new ArrayList<>();

	//get() and set() functions for variables
	
	/* (non-Javadoc)
	 * @see lifetracker.calendar.CalenderList#getTaskList()
	 */
	@Override
	public List<Task> getTaskList() {
		return taskList;	
	}
	
	/* (non-Javadoc)
	 * @see lifetracker.calendar.CalenderList#getEventList()
	 */
	@Override
	public List<Event> getEventList() {
		return eventList;
	}
	
	/* (non-Javadoc)
	 * @see lifetracker.calendar.CalenderList#addEvent()
	 */
	@Override
	public void addEvent(String name, Date startDate, Date endDate, Time startTime, Time endTime){
		Event e = new NamedEvent(name, startDate, endDate, startTime, endTime);
		eventList.add(e);
	}
	
	/* (non-Javadoc)
	 * @see lifetracker.calendar.CalenderList#addTask()
	 */
	@Override
	public void addTask(String name, Date deadlineDate, Time deadlineTime){
		Task t = new NamedTask(name, deadlineDate, deadlineTime);
		taskList.add(t);
	}
}
