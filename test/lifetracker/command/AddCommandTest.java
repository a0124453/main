package lifetracker.command;

import lifetracker.calendar.CalendarList;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//@@author A0091173J
public class AddCommandTest {

    @Test
    public void testExecute() throws Exception {

        //Floating task
        AddCommand addCommand = new AddCommand("floating");

        CalendarList mockedCalendar = mock(CalendarList.class);

        addCommand.execute(mockedCalendar);

        verify(mockedCalendar).add("floating");

        //Deadline task
        addCommand = new AddCommand("task", LocalDateTime.MIN);

        mockedCalendar = mock(CalendarList.class);

        addCommand.execute(mockedCalendar);

        verify(mockedCalendar).add("task", LocalDateTime.MIN);

        //Event
        addCommand = new AddCommand("event", LocalDateTime.MIN, LocalDateTime.MAX);

        mockedCalendar = mock(CalendarList.class);

        addCommand.execute(mockedCalendar);

        verify(mockedCalendar).add("event", LocalDateTime.MIN, LocalDateTime.MAX);
    }
}