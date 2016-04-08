package lifetracker.command;

import lifetracker.calendar.CalendarListImpl;
import org.junit.Assert;
import org.junit.Test;

//@@author A0114240B
public class DeleteCommandTest {
    
    @Test
    public void testUnexecutedComment() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(1);
        Assert.assertEquals(deleteCommand.getComment(), CommandObject.MESSAGE_ERROR);
    }
    
    @Test
    public void testExecute() throws Exception {

        CalendarListImpl mockedCalendar = new CalendarListImpl();
        
        mockedCalendar.add("task");
        
        DeleteCommand deleteCommand = new DeleteCommand(1);
        deleteCommand.execute(mockedCalendar);
        
        Assert.assertEquals("1 is deleted.", deleteCommand.getComment());
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFailExecute() throws IllegalArgumentException {

        CalendarListImpl mockedCalendar = new CalendarListImpl();       
        DeleteCommand deleteCommand = new DeleteCommand(1);
        deleteCommand.execute(mockedCalendar);

    }
    
    
}
