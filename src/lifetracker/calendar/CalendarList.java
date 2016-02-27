package lifetracker.calendar;

import java.util.ArrayList;
import java.util.List;

public class CalendarList {

	//variables
	private static List<task> taskList = new ArrayList<task>();
	private static List<NamedEvent> eventList = new ArrayList<NamedEvent>();
	
	//constructor
	public CalendarList(List<task> taskList, List<NamedEvent> eventList){
		CalendarList.setTaskList(taskList);
		CalendarList.setEventList(eventList);
	}

	//get() and set() functions for variables
	public static List<task> getTaskList() {
		return taskList;
	}
	public static void setTaskList(List<task> taskList) {
		CalendarList.taskList = taskList;
	}
	public static List<NamedEvent> getEventList() {
		return eventList;
	}
	public static void setEventList(List<NamedEvent> eventList) {
		CalendarList.eventList = eventList;
	}
	
	//functions
	private static void addEvent(String name, Date startDate, Date endDate, Time startTime, Time endTime){
		NamedEvent e = new NamedEvent(name, startDate, endDate, startTime, endTime);
		eventList.add(e);
	}
	
	private static void addTask(String name, Date deadlineDate, Time deadlineTime){
		task t = new task(name, deadlineDate, deadlineTime);
		taskList.add(t);
	}
	
	private static void listAllTask(){
		if(taskList.isEmpty()){
			System.out.println("No task in the calendar!");
		}
		
		else{
			for(task tasks: taskList){
				tasks.printTask();
			}
		}
	}
	
	private static void listAllEvent(){
		if(eventList.isEmpty()){
			System.out.println("No event in the calendar!");
		}
		
		else{
			for(NamedEvent events: eventList){
				events.printEvent();
			}
		}
	}
}
