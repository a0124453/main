package lifetracker.calendar;
import java.time.LocalDateTime;

public interface Task {

	//get() and set() functions for variables
	String getName();

	void setName(String name);

	LocalDateTime getDeadline();

	void setDeadline(LocalDateTime deadline);
	
	boolean isDueToday();
	
	boolean isOverdue();

}
