package lifetracker.calendar;

import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CalendarListTest {

    private static final String NAME_FLOATING_TASK = "floatingTask%d";
    private static final String NAME_DEADLINE_TASK = "deadlineTask%d";
    private static final String NAME_EVENT = "event%d";
    private static final String NAME_RECURRING_TASK = "recurringTask%d";
    private static final String NAME_RECURRING_EVENT = "recurringEvent%d";

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime TWO_HOURS_FROM_NOW = NOW.plusHours(2);
    private static final LocalDateTime TWO_HOURS_AGO = NOW.minusHours(2);
    private static final LocalDateTime THIS_TIME_TOMORROW = NOW.plusDays(1);
    private static final LocalDateTime TWO_HOURS_FROM_NOW_TOMORROW = THIS_TIME_TOMORROW.plusHours(2);
    private static final LocalDateTime THIS_TIME_YESTERDAY = NOW.minusDays(1);
    private static final LocalDateTime THIS_TIME_TWO_DAYS_AGO = NOW.minusDays(2);

    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate ONE_WEEK_FROM_TODAY = TODAY.plusWeeks(1);

    private static final Period EVERY_DAY = Period.ofDays(1);
    private static final Period EVERY_TWO_DAYS = Period.ofDays(2);
    private static final Period EVERY_WEEK = Period.ofWeeks(1);
    private static final Period EVERY_MONTH = Period.ofMonths(1);

    private static final int TWO_OCCURRENCES = 2;
    private static final int THREE_OCCURRENCES = 3;

    private static int floatingTaskCount = 0;
    private static int deadlineTaskCount = 0;
    private static int eventCount = 0;
    private static int recurringTaskCount = 0;
    private static int recurringEntryCount = 0;

    private static List<CalendarEntry> expectedTaskList = new ArrayList<>();
    private static List<CalendarEntry> expectedEventList = new ArrayList<>();
    private static List<CalendarEntry> expectedArchivedTaskList = new ArrayList<>();
    private static List<CalendarEntry> expectedArchivedEventList = new ArrayList<>();

    private static CalendarList testCalendar = new CalendarListImpl();

    private static String getTestEntryName(String name, int num) {
        return String.format(name, num);
    }

    private static void resetAllCounts() {
        floatingTaskCount = 0;
        deadlineTaskCount = 0;
        eventCount = 0;
        recurringTaskCount = 0;
        recurringEntryCount = 0;
    }

    private static void resetTestCalendar() {
        testCalendar = new CalendarListImpl();
        resetAllCounts();
        testCalendar.add(getTestEntryName(NAME_FLOATING_TASK, floatingTaskCount++));
        testCalendar.add(getTestEntryName(NAME_FLOATING_TASK, floatingTaskCount++));
        testCalendar.add(getTestEntryName(NAME_DEADLINE_TASK, deadlineTaskCount++), TWO_HOURS_FROM_NOW);
        testCalendar.add(getTestEntryName(NAME_DEADLINE_TASK, deadlineTaskCount++), THIS_TIME_TWO_DAYS_AGO);
        testCalendar.add(getTestEntryName(NAME_RECURRING_TASK, recurringTaskCount++), TWO_HOURS_FROM_NOW, EVERY_DAY);
        testCalendar.add(getTestEntryName(NAME_RECURRING_TASK, recurringTaskCount++), TWO_HOURS_FROM_NOW, EVERY_DAY,
                TWO_OCCURRENCES);
        testCalendar.add(getTestEntryName(NAME_RECURRING_TASK, recurringTaskCount++), TWO_HOURS_FROM_NOW, EVERY_DAY,
                ONE_WEEK_FROM_TODAY);
        testCalendar.add(getTestEntryName(NAME_EVENT, eventCount++), THIS_TIME_TOMORROW, TWO_HOURS_FROM_NOW_TOMORROW);
        testCalendar.add(getTestEntryName(NAME_EVENT, eventCount++), TWO_HOURS_AGO, TWO_HOURS_FROM_NOW_TOMORROW);
        testCalendar.add(getTestEntryName(NAME_RECURRING_EVENT, eventCount++), THIS_TIME_TOMORROW,
                TWO_HOURS_FROM_NOW_TOMORROW, EVERY_WEEK);
        testCalendar.add(getTestEntryName(NAME_RECURRING_EVENT, eventCount++), TWO_HOURS_AGO, TWO_HOURS_FROM_NOW,
                EVERY_WEEK, THREE_OCCURRENCES);
        testCalendar.add(getTestEntryName(NAME_RECURRING_EVENT, eventCount++), TWO_HOURS_AGO, TWO_HOURS_FROM_NOW,
                EVERY_TWO_DAYS, ONE_WEEK_FROM_TODAY);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        resetTestCalendar();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        resetTestCalendar();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetTaskList() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetEventList() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetArchivedTaskList() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetArchivedEventList() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddString() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddStringLocalDateTime() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddStringLocalDateTimePeriod() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddStringLocalDateTimePeriodInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddStringLocalDateTimePeriodLocalDate() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddStringLocalDateTimeLocalDateTime() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddStringLocalDateTimeLocalDateTimePeriod() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddStringLocalDateTimeLocalDateTimePeriodInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddStringLocalDateTimeLocalDateTimePeriodLocalDate() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddCalendarEntry() {
        fail("Not yet implemented");
    }

    @Test
    public void testDelete() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToGeneric() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToDeadline() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToRecurringTaskIntStringLocalDateTimePeriodBooleanBoolean() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToRecurringTaskIntStringLocalDateTimePeriodIntBoolean() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToRecurringTaskIntStringLocalDateTimePeriodLocalDateBoolean() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToRecurringEventIntStringLocalDateTimeLocalDateTimePeriodBoolean() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToRecurringEventIntStringLocalDateTimeLocalDateTimePeriodInt() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateToRecurringEventIntStringLocalDateTimeLocalDateTimePeriodLocalDate() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdate() {
        fail("Not yet implemented");
    }

    @Test
    public void testMark() {
        fail("Not yet implemented");
    }

    @Test
    public void testGet() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindByName() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindArchivedByName() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindAllByName() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindToday() {
        fail("Not yet implemented");
    }

}
