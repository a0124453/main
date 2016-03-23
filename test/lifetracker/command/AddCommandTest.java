package lifetracker.command;

import lifetracker.calendar.CalendarList;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@@author A0091173J
public class AddCommandTest {

    @Test
    public void testUnexecutedComment() throws Exception {
        AddCommand addCommand = new AddCommand("test");
        Assert.assertEquals(addCommand.getComment(), CommandObject.MESSAGE_ERROR);
    }

    @Test
    public void testExecute() throws Exception {

        //Floating task
        AddCommand addCommand = new AddCommand("floating");

        CalendarList mockedCalendar = mock(CalendarList.class);

        addCommand.execute(mockedCalendar);

        verify(mockedCalendar).add("floating");
        Assert.assertEquals("\"floating\" is added.", addCommand.getComment());

        //Deadline task
        addCommand = new AddCommand("task", LocalDateTime.MIN);

        mockedCalendar = mock(CalendarList.class);

        addCommand.execute(mockedCalendar);

        verify(mockedCalendar).add("task", LocalDateTime.MIN);
        Assert.assertEquals("\"task\" is added.", addCommand.getComment());

        //Event
        addCommand = new AddCommand("event", LocalDateTime.MIN, LocalDateTime.MAX);

        mockedCalendar = mock(CalendarList.class);

        addCommand.execute(mockedCalendar);

        verify(mockedCalendar).add("event", LocalDateTime.MIN, LocalDateTime.MAX);
        Assert.assertEquals("\"event\" is added.", addCommand.getComment());
    }

    @Test
    public void testUndo() throws Exception {

        AddCommand addCommand = new AddCommand("floating");
        CalendarList calendar = mock(CalendarList.class);
        when(calendar.add("floating")).thenReturn(1);

        addCommand.execute(calendar);
        addCommand.undo(calendar);

        verify(calendar).delete(1);
        Assert.assertEquals("1: \"floating\" removed.", addCommand.getComment());

        addCommand = new AddCommand("task", LocalDateTime.MIN);
        calendar = mock(CalendarList.class);
        when(calendar.add("task", LocalDateTime.MIN)).thenReturn(2);

        addCommand.execute(calendar);
        addCommand.undo(calendar);

        verify(calendar).delete(2);
        Assert.assertEquals("2: \"task\" removed.", addCommand.getComment());

        addCommand = new AddCommand("event", LocalDateTime.MIN, LocalDateTime.MAX);
        calendar = mock(CalendarList.class);
        when(calendar.add("event", LocalDateTime.MIN, LocalDateTime.MAX)).thenReturn(3);

        addCommand.execute(calendar);
        addCommand.undo(calendar);

        verify(calendar).delete(3);
        Assert.assertEquals("3: \"event\" removed.", addCommand.getComment());
    }
}