package lifetracker.calendar;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class CalendarListTest {

    public static final int listSize = 10;
    public static final LocalDateTime now = LocalDateTime.now();

    private static CalendarListImpl testCalendar = new CalendarListImpl();
    private static List<Task> expectedTaskList = new ArrayList<Task>();

    @BeforeClass
    public static void setUp() {
        // add tasks to testCalendar using overloaded addTask methods
        for (int i = 0; i < listSize; i++) {
            if (i < listSize / 2) {
                // addTask(String, LocalDateTime)
                testCalendar.addTask(Integer.toString(i), null);
            } else {
                // addTask(TaskImpl)
                TaskImpl task = new TaskImpl(Integer.toString(i), now);
                testCalendar.addTask(task);
            }
        }
        // set up testTaskList for comparison with task list in testCalendar
        for (int i = 0; i < listSize; i++) {
            if (i < listSize / 2) {
                TaskImpl task = new TaskImpl(Integer.toString(i), null);
                expectedTaskList.add(task);
            } else {
                TaskImpl task = new TaskImpl(Integer.toString(i), now);
                expectedTaskList.add(task);
            }
        }

    }

    @Test
    public void runTest() {
        // compare names of all tasks in both lists
        for (int i = 0; i < listSize; i++) {
            assertEquals(expectedTaskList.get(i).getName(), testCalendar.getTaskList().get(i).getName());
        }
        // compare deadlines of all tasks in both lists
        for (int i = 0; i < listSize; i++) {
            if (expectedTaskList.get(i).getDeadline() == null) {
                assertEquals(null, testCalendar.getTaskList().get(i).getDeadline());
            } else {
                assertEquals(expectedTaskList.get(i).getDeadline(), testCalendar.getTaskList().get(i).getDeadline());
            }
        }
    }

}
