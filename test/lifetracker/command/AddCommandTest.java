package lifetracker.command;

import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarListImpl;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//@@author A0091173J
public class AddCommandTest {

    CalendarList mockedCalendar;

    @Before
    public void setUp() throws Exception {
        mockedCalendar = mock(CalendarListImpl.class);
    }

    @Test
    public void testExecute() throws Exception {

        AddCommand addFloating = new AddCommand("floating");

        addFloating.execute(mockedCalendar);

        verify(mockedCalendar).add("floating");

    }
}