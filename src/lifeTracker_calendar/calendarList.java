package lifeTracker_calendar;

import java.util.ArrayList;
import java.util.List;

public class calendarList {

	//variables
	private static List<task> taskList = new ArrayList<task>();
	private static List<event> eventList = new ArrayList<event>();
	
	//constructor
	public calendarList(List<task> taskList, List<event> eventList){
		calendarList.setTaskList(taskList);
		calendarList.setEventList(eventList);
	}

	//get() and set() functions for variables
	public static List<task> getTaskList() {
		return taskList;
	}
	public static void setTaskList(List<task> taskList) {
		calendarList.taskList = taskList;
	}
	public static List<event> getEventList() {
		return eventList;
	}
	public static void setEventList(List<event> eventList) {
		calendarList.eventList = eventList;
	}
	
	//functions
	private static void addEvent(String name, date startDate, date endDate, time startTime, time endTime){
		event e = new event(name, startDate, endDate, startTime, endTime);
		eventList.add(e);
	}
	
	private static void addTask(String name, date deadlineDate, time deadlineTime){
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
			for(event events: eventList){
				events.printEvent();
			}
		}
	}
}
