package lifetracker.calendar;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

//@@author A0108473E-unused
//Test no longer works after new Calendar component implemenatation
public class CalendarListTest {

    public static final int NO_OF_FLOATING_TASKS = 3;
    public static final int NO_OF_DEADLINE_TASKS = 3;
    public static final int TOTAL_NO_OF_TASKS = 6;
    public static final int TOTAL_NO_OF_EVENTS = 3;
    public static final int TOTAL_NO_OF_ENTRIES = 9;
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final LocalDateTime TWO_HOURS_LATER = LocalDateTime.now().plusHours(2);

    private static CalendarList testCalendar = new CalendarListImpl();
    private static List<CalendarEntry> expectedTaskList = new ArrayList<>();
    private static List<CalendarEntry> expectedEventList = new ArrayList<>();

    @BeforeClass
    public static void setUpBeforeClass() {
        // add tasks to testCalendar using overloaded add methods
        for (int i = 0; i < TOTAL_NO_OF_ENTRIES; i++) {
            if (i < NO_OF_FLOATING_TASKS) {
                // add(String)
                testCalendar.add(Integer.toString(i));
            } else if (i < TOTAL_NO_OF_TASKS) {
                // add(String, LocalDateTime)
                testCalendar.add(Integer.toString(i), NOW);
            } else {
                // add(String, LocalDateTime, LocalDateTime)
                testCalendar.add(Integer.toString(i), NOW, TWO_HOURS_LATER);
            }
        }
        // set up expectedTaskList and expectedEventList for comparison with
        // lists in testCalendar
        for (int i = 0; i < TOTAL_NO_OF_ENTRIES; i++) {
            if (i < NO_OF_FLOATING_TASKS) {
                CalendarEntry entry = new CalendarEntryImpl(Integer.toString(i), null, null, i);
                expectedTaskList.add(entry);
            } else if (i < TOTAL_NO_OF_TASKS) {
                CalendarEntry entry = new CalendarEntryImpl(Integer.toString(i), null, NOW, i);
                expectedTaskList.add(entry);
            } else {
                CalendarEntry entry = new CalendarEntryImpl(Integer.toString(i), NOW, TWO_HOURS_LATER, i);
                expectedEventList.add(entry);
            }
        }

    }

    @Test
    public void testAdd() {
        // compare names of all tasks in both lists
        for (int i = 0; i < TOTAL_NO_OF_TASKS; i++) {
            assertEquals(expectedTaskList.get(i).getName(), testCalendar.getTaskList().get(i).getName());
        }
        // compare deadlines of all tasks in both lists
        for (int i = 0; i < TOTAL_NO_OF_TASKS; i++) {
            assertEquals(expectedTaskList.get(i).getEnd(), testCalendar.getTaskList().get(i).getEnd());
        }
    }

}
