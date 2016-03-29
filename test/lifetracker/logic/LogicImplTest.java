package lifetracker.logic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicImplTest {
    
    private static final String ERROR_INVALID_COMMAND = "Error: Command was not a valid command!";
    
    private static Parser parser = mock(Parser.class);
    private static Storage storage = mock(Storage.class);
    private static LogicImpl logicTest;
    
    private static ExecuteResult expected1 = new CommandLineResult();
    private static ExecuteResult expected2 = new CommandLineResult();
    private static ExecuteResult expected3 = new CommandLineResult();
    
    private static ExecuteResult error = new CommandLineResult();
    
    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        when(storage.load()).thenReturn("");
        
        logicTest = new LogicImpl(parser, storage);
        
        expected1.setComment("\"first meeting\" is added.");
        expected1.setType("add first meeting");
        
        expected2.setComment("\"second meeting\" is added.");
        expected2.setType("add second meeting by 2016-12-31 23:59:59");
        
        expected3.setComment("\"third meeting\" is added.");
        expected3.setType("add third meeting from 2017-01-01 00:00:00 to 2017-01-01 23:59:59");
        
        error.setComment(ERROR_INVALID_COMMAND);
        error.setType("ERROR");
    }

    @Test
    public void testAdd() {
        //test adding valid floating task
        assertEquals(expected1.getComment(), logicTest.executeCommand("add first meeting").getComment());
        assertEquals(expected1.getEventList(), logicTest.executeCommand("add first meeting").getEventList());
        assertEquals(expected1.getTaskList(), logicTest.executeCommand("add first meeting").getTaskList());
        assertEquals(expected1.getType(), logicTest.executeCommand("add first meeting").getType());
        
        //test adding valid deadline task
        //This is the upper bound of the boundary case for the valid partition
        assertEquals(expected2.getComment(), logicTest.executeCommand("add second meeting by 2016-12-31 23:59:59").getComment());
        assertEquals(expected2.getEventList(), logicTest.executeCommand("add second meeting by 2016-12-31 23:59:59").getEventList());
        assertEquals(expected2.getTaskList(), logicTest.executeCommand("add second meeting by 2016-12-31 23:59:59").getTaskList());
        assertEquals(expected2.getType(), logicTest.executeCommand("add second meeting by 2016-12-31 23:59:59").getType());
        
        //test adding valid event
        //This is the lower bound of the boundary case for the valid partition
        assertEquals(expected3.getComment(), logicTest.executeCommand("add third meeting from 2017-01-01 00:00:00 to 2017-01-01 23:59:59").getComment());
        assertEquals(expected3.getEventList(), logicTest.executeCommand("add third meeting from 2017-01-01 00:00:00 to 2017-01-01 23:59:59").getEventList());
        assertEquals(expected3.getTaskList(), logicTest.executeCommand("add third meeting from 2017-01-01 00:00:00 to 2017-01-01 23:59:59").getTaskList());
        assertEquals(expected3.getType(), logicTest.executeCommand("add third meeting from 2017-01-01 00:00:00 to 2017-01-01 23:59:59").getType());
        
        /*----------test adding error event----------*/
        
        //test error month
        //This is the boundary case for the invalid partition
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-00-31 23:59:59").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-00-31 23:59:59").getType());
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-13-31 23:59:59").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-13-31 23:59:59").getType());
        
        //test error day
        //This is the boundary case for the invalid partition
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-12-00 23:59:59").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-12-00 23:59:59").getType());
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-12-32 23:59:59").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-12-32 23:59:59").getType());
        
        //test error hour
        //This is the boundary case for the invalid partition
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-12-31 -1:59:59").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-12-31 -1:59:59").getType());
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-12-31 24:59:59").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-12-31 24:59:59").getType());
        
        //test error minute
        //This is the boundary case for the invalid partition
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-12-31 23:-1:59").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-12-31 23:-1:59").getType());
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-12-31 23:60:59").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-12-31 23:60:59").getType());
        
        //test error second
        //This is the boundary case for the invalid partition
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-12-31 23:59:-1").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-12-31 23:59:-1").getType());
        assertEquals(error.getComment(), logicTest.executeCommand("add error meeting by 2016-12-31 23:59:60").getComment());
        assertEquals(error.getType(), logicTest.executeCommand("add error meeting by 2016-12-31 23:59:60").getType());
    }
}
