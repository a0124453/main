package lifetracker.parser;

import lifetracker.command.CommandFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;

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
        TemporalAmount expectedRecurringDuration = Period.ofWeeks(2);
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
}