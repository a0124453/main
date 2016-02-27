package lifetracker.calendar;

import java.util.ArrayList;
import java.util.List;

public class CalendarList {

	//variables
	private static List<Task> taskList = new ArrayList<>();
	private static List<Event> eventList = new ArrayList<>();
	
	//constructor
	public CalendarList(List<Task> taskList, List<Event> eventList){
		CalendarList.setTaskList(taskList);
		CalendarList.setEventList(eventList);
	}

	//get() and set() functions for variables
	public static List<Task> getTaskList() {
		return taskList;	
	}
	public static void setTaskList(List<Task> taskList) {
		CalendarList.taskList = taskList;
	}
	public static List<Event> getEventList() {
		return eventList;
	}
	public static void setEventList(List<Event> eventList) {
		CalendarList.eventList = eventList;
	}
	
	//functions
	private static void addEvent(String name, Date startDate, Date endDate, Time startTime, Time endTime){
		Event e = new NamedEvent(name, startDate, endDate, startTime, endTime);
		eventList.add(e);
	}
	
	private static void addTask(String name, Date deadlineDate, Time deadlineTime){
		Task t = new NamedTask(name, deadlineDate, deadlineTime);
		taskList.add(t);
	}
}
