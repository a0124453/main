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
    public static final LocalDateTime TWO_HOURS_LATER = LocalDateTime.now().plusHours(2);
    public static final LocalDateTime THIS_TIME_TOMORROW = LocalDateTime.now().plusDays(1);
    public static final LocalDateTime THIS_TIME_YESTERDAY = LocalDateTime.now().minusDays(1);
    public static final LocalDateTime THIS_TIME_TWO_DAYS_AGO = LocalDateTime.now().minusDays(2);
    public static final String TEST_NAME = "entry%d";
    static CalendarEntry testEntry1; // floating
    static CalendarEntry testEntry2; // ongoing deadline task due today
    static CalendarEntry testEntry3; // overdue deadline task
    static CalendarEntry testEntry4; // ongoing event
    static CalendarEntry testEntry5; // upcoming event starting today
    static CalendarEntry testEntry6; // event over
    static CalendarEntry copyEntry1;
    static CalendarEntry copyEntry2;
    static CalendarEntry copyEntry6;

    static String getTestEntryName(int k) {
        return String.format(TEST_NAME, k);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        testEntry1 = new CalendarEntryImpl(getTestEntryName(1), null, null, 1);
        testEntry2 = new CalendarEntryImpl(getTestEntryName(2), null, TWO_HOURS_LATER, 2);
        testEntry3 = new CalendarEntryImpl(getTestEntryName(3), null, THIS_TIME_YESTERDAY, 3);
        testEntry4 = new CalendarEntryImpl(getTestEntryName(4), THIS_TIME_YESTERDAY, THIS_TIME_TOMORROW, 4);
        testEntry5 = new CalendarEntryImpl(getTestEntryName(5), TWO_HOURS_LATER, THIS_TIME_TOMORROW, 5);
        testEntry6 = new CalendarEntryImpl(getTestEntryName(6), THIS_TIME_TWO_DAYS_AGO, THIS_TIME_YESTERDAY, 6);
        copyEntry1 = testEntry1;
        copyEntry2 = testEntry2;
        copyEntry6 = testEntry6;
    }

    @Test
    public void testConstructor() {

        EntryType expectedType1 = EntryType.FLOATING;
        EntryType expectedType2 = EntryType.DEADLINE;
        EntryType expectedType4 = EntryType.EVENT;

        EntryType actualType1 = testEntry1.getType();
        EntryType actualType2 = testEntry2.getType();
        EntryType actualType4 = testEntry4.getType();

        assertEquals(expectedType1, actualType1);
        assertEquals(expectedType2, actualType2);
        assertEquals(expectedType4, actualType4);
    }

    @Test
    public void testGetStartTime() {

        LocalTime expectedStart1 = null;
        LocalTime expectedStart2 = null;
        LocalTime expectedStart4 = THIS_TIME_YESTERDAY.toLocalTime();

        LocalTime actualStart1 = testEntry1.getStartTime();
        LocalTime actualStart2 = testEntry2.getStartTime();
        LocalTime actualStart4 = testEntry4.getStartTime();

        assertEquals(expectedStart1, actualStart1);
        assertEquals(expectedStart2, actualStart2);
        assertEquals(expectedStart4, actualStart4);
    }

    @Test
    public void testGetEndTime() {

        LocalTime expectedEnd1 = null;
        LocalTime expectedEnd2 = TWO_HOURS_LATER.toLocalTime();
        LocalTime expectedEnd3 = THIS_TIME_TOMORROW.toLocalTime();

        LocalTime actualEnd1 = testEntry1.getEndTime();
        LocalTime actualEnd2 = testEntry2.getEndTime();
        LocalTime actualEnd3 = testEntry3.getEndTime();

        assertEquals(expectedEnd1, actualEnd1);
        assertEquals(expectedEnd2, actualEnd2);
        assertEquals(expectedEnd3, actualEnd3);
    }

    @Test
    public void testIsToday() {

        assertFalse(testEntry1.isToday());
        assertTrue(testEntry2.isToday());
        assertFalse(testEntry3.isToday());
        assertTrue(testEntry4.isToday());
        assertTrue(testEntry5.isToday());
    }

    @Test
    public void testIsOngoing() {

        assertTrue(testEntry1.isOngoing());
        assertTrue(testEntry2.isOngoing());
        assertFalse(testEntry3.isOngoing());
        assertTrue(testEntry4.isOngoing());
        assertFalse(testEntry5.isOngoing());
        assertFalse(testEntry6.isOngoing());
    }

    @Test
    public void testIsOver() {

        assertFalse(testEntry1.isOver());
        assertFalse(testEntry2.isOver());
        assertTrue(testEntry3.isOver());
        assertFalse(testEntry4.isOver());
        assertFalse(testEntry5.isOver());
        assertTrue(testEntry6.isOver());
    }

    @Test
    public void testEqualsCalendarEntry() {

        assertTrue(testEntry1.equals(copyEntry1));
        assertTrue(testEntry2.equals(copyEntry2));
        assertTrue(testEntry6.equals(copyEntry6));
    }

    @Test
    public void testCopy() {

        copyEntry1 = testEntry1.copy();
        copyEntry2 = testEntry2.copy();
        copyEntry6 = testEntry6.copy();

        assertTrue(testEntry1.equals(copyEntry1));
        assertTrue(testEntry2.equals(copyEntry2));
        assertTrue(testEntry6.equals(copyEntry6));
    }

}
