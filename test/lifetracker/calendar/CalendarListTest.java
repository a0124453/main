package lifetracker.calendar;

import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CalendarListTest {

    public static final int listSize = 10;
    public static final LocalDateTime now = LocalDateTime.now();

    private static CalendarListImpl testCalendar = new CalendarListImpl();
    private static List<CalendarEntry> expectedTaskList = new ArrayList<>();

    @BeforeClass
    public static void setUpBeforeClass() {
        // add tasks to testCalendar using overloaded addTask methods
        for (int i = 0; i < listSize; i++) {
            if (i < listSize / 2) {
                // addTask(String)
                testCalendar.add(Integer.toString(i));
            } else {
                // addTask(String, LocalDateTime)
                testCalendar.add(Integer.toString(i), now);
            }
        }
        // set up testTaskList for comparison with task list in testCalendar
        for (int i = 0; i < listSize; i++) {
            if (i < listSize / 2) {
                CalendarEntryImpl task = new CalendarEntryImpl(Integer.toString(i), null, null, i);
                expectedTaskList.add(task);
            } else {
                CalendarEntryImpl task = new CalendarEntryImpl(Integer.toString(i),null ,now, i);
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
            assertEquals(expectedTaskList.get(i).getEnd(), testCalendar.getTaskList().get(i).getEnd());
        }
    }

}
