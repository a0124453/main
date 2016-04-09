package lifetracker.calendar;

import static org.junit.Assert.fail;

import java.time.LocalDateTime;

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
    private static final LocalDateTime TWO_HOURS_FROM_NOW = LocalDateTime.now().plusHours(2);
    private static final LocalDateTime TWO_HOURS_AGO = LocalDateTime.now().minusHours(2);
    private static final LocalDateTime THIS_TIME_TOMORROW = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime TWO_HOURS_FROM_NOW_TOMORROW = THIS_TIME_TOMORROW.plusHours(2);
    private static final LocalDateTime THIS_TIME_YESTERDAY = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime THIS_TIME_TWO_DAYS_AGO = LocalDateTime.now().minusDays(2);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
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
