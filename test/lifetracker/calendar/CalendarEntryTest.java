package lifetracker.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.BeforeClass;
import org.junit.Test;

import lifetracker.calendar.CalendarEntry.EntryType;

public class CalendarEntryTest {

    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final LocalDateTime TWO_HOURS_FROM_NOW = LocalDateTime.now().plusHours(2);
    public static final LocalDateTime THIS_TIME_TOMORROW = LocalDateTime.now().plusDays(1);
    public static final LocalDateTime TWO_HOURS_FROM_NOW_TOMORROW = THIS_TIME_TOMORROW.plusHours(2);
    public static final LocalDateTime THIS_TIME_YESTERDAY = LocalDateTime.now().minusDays(1);
    public static final LocalDateTime THIS_TIME_TWO_DAYS_AGO = LocalDateTime.now().minusDays(2);
    public static final String TEST_NAME = "entry%d";
    static CalendarEntry testEntry1; // floating
    static CalendarEntry testEntry2; // ongoing deadline task due today
    static CalendarEntry testEntry3; // ongoing deadline task due tomorrow
    static CalendarEntry testEntry4; // overdue deadline task
    static CalendarEntry testEntry5; // ongoing event
    static CalendarEntry testEntry6; // upcoming event starting today
    static CalendarEntry testEntry7; // upcoming event starting tomorrow
    static CalendarEntry testEntry8; // event over
    static CalendarEntry testEntry9; // to test exceptions
    static CalendarEntry copyEntry1;
    static CalendarEntry copyEntry2;
    static CalendarEntry copyEntry6;

    static String getTestEntryName(int k) {
        return String.format(TEST_NAME, k);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        testEntry1 = new CalendarEntryImpl(getTestEntryName(1), null, null, 1);
        testEntry2 = new CalendarEntryImpl(getTestEntryName(2), null, TWO_HOURS_FROM_NOW, 2);
        testEntry3 = new CalendarEntryImpl(getTestEntryName(3), null, THIS_TIME_TOMORROW, 3);
        testEntry4 = new CalendarEntryImpl(getTestEntryName(4), null, THIS_TIME_YESTERDAY, 4);
        testEntry5 = new CalendarEntryImpl(getTestEntryName(5), THIS_TIME_YESTERDAY, THIS_TIME_TOMORROW, 5);
        testEntry6 = new CalendarEntryImpl(getTestEntryName(6), TWO_HOURS_FROM_NOW, THIS_TIME_TOMORROW, 6);
        testEntry7 = new CalendarEntryImpl(getTestEntryName(7), THIS_TIME_TOMORROW, TWO_HOURS_FROM_NOW_TOMORROW, 7);
        testEntry8 = new CalendarEntryImpl(getTestEntryName(8), THIS_TIME_TWO_DAYS_AGO, THIS_TIME_YESTERDAY, 8);
        testEntry9 = null;
        copyEntry1 = testEntry1;
        copyEntry2 = testEntry2;
        copyEntry6 = testEntry8;
    }

    @Test
    public void testConstructor() {

        EntryType floating = EntryType.FLOATING;
        EntryType deadline = EntryType.DEADLINE;
        EntryType event = EntryType.EVENT;

        EntryType actualType1 = testEntry1.getType();
        EntryType actualType2 = testEntry2.getType();
        EntryType actualType3 = testEntry3.getType();
        EntryType actualType4 = testEntry4.getType();
        EntryType actualType5 = testEntry5.getType();
        EntryType actualType6 = testEntry6.getType();
        EntryType actualType7 = testEntry7.getType();
        EntryType actualType8 = testEntry8.getType();

        // test assignment of enum to entries
        assertEquals(floating, actualType1);
        assertEquals(deadline, actualType2);
        assertEquals(deadline, actualType3);
        assertEquals(deadline, actualType4);
        assertEquals(event, actualType5);
        assertEquals(event, actualType6);
        assertEquals(event, actualType7);
        assertEquals(event, actualType8);

        // test invalid construction of events
        try {
            testEntry9 = new CalendarEntryImpl(getTestEntryName(9), NOW, THIS_TIME_YESTERDAY, 9);
        } catch (Exception e) {

            String expectedErrorMessage = "Start date/time cannot be after end date/time!";

            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(expectedErrorMessage, e.getMessage());
        }
        // ensure that try block was not completed
        assertEquals(null, testEntry9);
    }

    @Test
    // test with null startTime (tasks) and non-null startTime (events)
    public void testGetStartTime() {

        LocalTime expectedStart1 = null;
        LocalTime expectedStart2 = null;
        LocalTime expectedStart3 = null;
        LocalTime expectedStart5 = THIS_TIME_YESTERDAY.toLocalTime();
        LocalTime expectedStart6 = TWO_HOURS_FROM_NOW.toLocalTime();
        LocalTime expectedStart7 = THIS_TIME_TOMORROW.toLocalTime();

        LocalTime actualStart1 = testEntry1.getStartTime();
        LocalTime actualStart2 = testEntry2.getStartTime();
        LocalTime actualStart3 = testEntry3.getStartTime();
        LocalTime actualStart5 = testEntry5.getStartTime();
        LocalTime actualStart6 = testEntry6.getStartTime();
        LocalTime actualStart7 = testEntry7.getStartTime();

        assertEquals(expectedStart1, actualStart1); // floating task
        assertEquals(expectedStart2, actualStart2); // deadline task
        assertEquals(expectedStart3, actualStart3); // deadline task
        assertEquals(expectedStart5, actualStart5); // event
        assertEquals(expectedStart6, actualStart6); // event
        assertEquals(expectedStart7, actualStart7); // event
    }

    @Test
    // test with null endTime (floating tasks) and non-null endTime (deadline
    // tasks, events)
    public void testGetEndTime() {

        LocalTime expectedEnd1 = null;
        LocalTime expectedEnd2 = TWO_HOURS_FROM_NOW.toLocalTime();
        LocalTime expectedEnd3 = THIS_TIME_TOMORROW.toLocalTime();
        LocalTime expectedEnd8 = THIS_TIME_YESTERDAY.toLocalTime();

        LocalTime actualEnd1 = testEntry1.getEndTime();
        LocalTime actualEnd2 = testEntry2.getEndTime();
        LocalTime actualEnd3 = testEntry3.getEndTime();
        LocalTime actualEnd8 = testEntry8.getEndTime();

        assertEquals(expectedEnd1, actualEnd1); // floating task
        assertEquals(expectedEnd2, actualEnd2); // deadline task
        assertEquals(expectedEnd3, actualEnd3); // deadline task
        assertEquals(expectedEnd8, actualEnd8); // event
    }

    @Test
    public void testIsToday() {

        assertFalse(testEntry1.isToday()); // false for floating tasks
        assertTrue(testEntry2.isToday()); // true for deadline tasks whose
                                          // deadline is today
        assertFalse(testEntry3.isToday()); // false for deadline tasks whose
                                           // deadline is not today
        assertFalse(testEntry4.isToday());
        assertTrue(testEntry5.isToday()); // true for ongoing events
        assertTrue(testEntry6.isToday()); // true for events that start today
        assertFalse(testEntry7.isToday()); // false for events not starting
                                           // today and not ongoing
        assertFalse(testEntry8.isToday()); // false for events that are over and
                                           // did not start today
    }

    @Test
    public void testIsOngoing() {

        assertTrue(testEntry1.isOngoing());
        assertTrue(testEntry2.isOngoing());
        assertFalse(testEntry4.isOngoing());
        assertTrue(testEntry5.isOngoing());
        assertFalse(testEntry6.isOngoing());
        assertFalse(testEntry8.isOngoing());
    }

    @Test
    public void testIsOver() {

        assertFalse(testEntry1.isOver());
        assertFalse(testEntry2.isOver());
        assertTrue(testEntry4.isOver());
        assertFalse(testEntry5.isOver());
        assertFalse(testEntry6.isOver());
        assertTrue(testEntry8.isOver());
    }

    @Test
    public void testEqualsCalendarEntry() {

        assertTrue(testEntry1.equals(copyEntry1));
        assertTrue(testEntry2.equals(copyEntry2));
        assertTrue(testEntry8.equals(copyEntry6));
    }

    @Test
    public void testCopy() {

        copyEntry1 = testEntry1.copy();
        copyEntry2 = testEntry2.copy();
        copyEntry6 = testEntry8.copy();

        assertTrue(testEntry1.equals(copyEntry1));
        assertTrue(testEntry2.equals(copyEntry2));
        assertTrue(testEntry8.equals(copyEntry6));
    }

}
