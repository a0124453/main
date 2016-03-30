package lifetracker.parser;

import lifetracker.command.CommandFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

        //Boundary: No command
        parser.parse("email client by tomorrow 2pm");
        verify(cmdFactory).addDeadlineTask("email client", expectedDateTime);

        //Boundary: Invalid end date
        parser.parse("drop by supermarket");
        verify(cmdFactory).addFloatingTask("drop by supermarket");

        //Boundary: 2 date times
        parser.parse("drop by 7/11 by tomorrow 2pm");
        verify(cmdFactory).addDeadlineTask("drop by 7/11", expectedDateTime);
    }
}