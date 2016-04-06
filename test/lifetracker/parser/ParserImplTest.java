package lifetracker.parser;

import lifetracker.command.CommandFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//@@author A0091173J
public class ParserImplTest {

    private CommandFactory cmdFactory;
    private Parser parser;

    @Before
    public void setUp() throws Exception {
        cmdFactory = mock(CommandFactory.class);
        parser = new ParserImpl(cmdFactory);
    }

    @Test
    public void parseAddFloating() throws Exception {
        //Partition: Floating tasks
        parser.parse("add project meeting");
        verify(cmdFactory).addFloatingTask("project meeting");

        //Boundary: No command
        parser.parse("water plants");
        verify(cmdFactory).addFloatingTask("water plants");

        //Boundary: Name with special characters
        parser.parse("abc > < ! @ > #");
        verify(cmdFactory).addFloatingTask("abc > < ! @ > #");
    }

    @Test
    public void testParseAddDeadline() throws Exception {
        //Partition: Deadline tasks
        parser.parse("add do homework by tomorrow 2pm");
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0));
        verify(cmdFactory).addDeadlineTask("do homework", expectedDateTime);
        //Various datetime formats are tested in DateTimeParserTest

        //Boundary: Invalid end date
        parser.parse("drop by supermarket");
        verify(cmdFactory).addFloatingTask("drop by supermarket");

        //Boundary: 2 date times
        parser.parse("drop by 7/11 by tomorrow 2pm");
        verify(cmdFactory).addDeadlineTask("drop by 7/11", expectedDateTime);
    }

    @Test
    public void testParseAddRecurringDeadline() throws Exception {
        //Partition: Recurring deadlines
        parser.parse("finish readings by tomorrow 2pm every 2 week");
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0));
        Period expectedRecurringDuration = Period.ofWeeks(2);
        verify(cmdFactory).addRecurringDeadlineTask("finish readings", expectedDateTime, expectedRecurringDuration);

        //Boundary: Missing date time
        parser.parse("water plants every day");
        verify(cmdFactory).addRecurringDeadlineTask("water plants",
                LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT.minusMinutes(1)), Period.ofDays(1));

        //Boundary: Inverted options
        parser.parse("check out stall every 2 weeks by tomorrow 2pm");
        verify(cmdFactory).addRecurringDeadlineTask("check out stall", expectedDateTime, expectedRecurringDuration);

        //Boundary: Invalid durations
        parser.parse("check every single thing");
        verify(cmdFactory).addFloatingTask("check every single thing");
    }

    @Test
    public void testParseAddEvent() throws Exception {
        //Partition: Add events
        parser.parse("CS2103 Exam from 25/4/16 1pm to 3pm");
        verify(cmdFactory)
                .addEvent("CS2103 Exam", LocalDateTime.of(2016, 4, 25, 13, 0), LocalDateTime.of(2016, 4, 25, 15, 0));

        //Boundary: Inverted options
        parser.parse("Exam to 3pm from 25/4/16 1pm");
        verify(cmdFactory).addEvent("Exam", LocalDateTime.of(2016, 4, 25, 13, 0), LocalDateTime.of(2016, 4, 25, 15, 0));

        //Boundary: Invalid date time
        parser.parse("meeting from 2pm to hello");
        verify(cmdFactory).addFloatingTask("meeting from 2pm to hello");

        //Boundary: Missing to
        parser.parse("some talk from 22/4/16 10am");
        verify(cmdFactory)
                .addEvent("some talk", LocalDateTime.of(2016, 4, 22, 10, 0), LocalDateTime.of(2016, 4, 22, 11, 0));
    }

    @Test
    public void testAddRecurringEvents() throws Exception {
        //Partition: Recurring Events
        parser.parse("CS2103 Lecture from 15/1/16 4pm to 6pm every week");
        verify(cmdFactory).addRecurringEvent("CS2103 Lecture", LocalDateTime.of(2016, 1, 15, 16, 0),
                LocalDateTime.of(2016, 1, 15, 18, 0), Period.ofWeeks(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptions() throws Exception {
        parser.parse("imagination from today 2pm to 3pm by tomorrow");
    }

    @Test
    public void testEdit() throws Exception {
        //Partition: Edit task
        parser.parse("edit 1 > new name from tomorrow 3pm to 4pm every 2 days");
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0));

        verify(cmdFactory).editRecurringEvent(1, "new name", startTime, endTime, Period.ofDays(2), false);

        //Boundary: Missing options
        parser.parse("edit 2 > some other talk from tomorrow 3pm to 4pm");
        verify(cmdFactory).editEvent(2, "some other talk", startTime, endTime,true);

        //Boundary: Missing end date time
        parser.parse("edit 3 > run from tomorrow 3pm");
        verify(cmdFactory).editEvent(3, "run", startTime, endTime, true);

        //Boundary: Missing name
        parser.parse("edit 4 > from tomorrow 3pm to 4pm");
        verify(cmdFactory).editEvent(4, "", startTime, endTime, true);

        //Partition: Edit deadline task
        parser.parse("edit 5 > homework by tomorrow 4pm");
        verify(cmdFactory).editDeadline(5, "homework", endTime, true);

        //Boundary: Invalid deadline date time
        parser.parse("edit 6 > drop by school from home");
        verify(cmdFactory).editGenericTask(6, "drop by school from home", false);

        //Partition: Name only
        parser.parse("edit 7 > sleep");
        verify(cmdFactory).editGenericTask(7, "sleep", false);
    }

    @Test
    public void testSearch() throws Exception {
        //Partition: find all non-archived
        parser.parse("find");
        verify(cmdFactory).find();

        //Partition with search term
        parser.parse("search something");
        verify(cmdFactory).find("something");

        parser.parse("find something else");
        verify(cmdFactory).find("something else");

        //Boundary: with separators
        parser.parse("list something in the water > ?? > !!");
        verify(cmdFactory).find("something in the water > ?? > !!");
    }
}