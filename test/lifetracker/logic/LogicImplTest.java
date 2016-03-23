package lifetracker.logic;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

public class LogicImplTest {
    
    private static Parser parser = new LogicParserStub();
    private static Storage storage = new LogicStorageStub();
    private static LogicImpl logicTest;
    
    private static ExecuteResult expected1 = new CommandLineResult();
    private static ExecuteResult expected2 = new CommandLineResult();
    private static ExecuteResult expected3 = new CommandLineResult();
    
    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        logicTest = new LogicImpl(parser, storage);
        
        expected1.setComment("\"first meeting\" is added.");
        expected1.setType("add first meeting");
        
        expected2.setComment("\"second meeting\" is added.");
        expected2.setType("add second meeting by 2016-09-01 23:59:59");
        
        expected3.setComment("\"third meeting\" is added.");
        expected3.setType("add third meeting from 2016-10-01 23:59:59 to 2016-11-01 23:59:59");
    }

    @Test
    public void testAdd() {
        assertEquals(expected1.getComment(), logicTest.executeCommand("add first meeting").getComment());
        assertEquals(expected1.getEventList(), logicTest.executeCommand("add first meeting").getEventList());
        assertEquals(expected1.getTaskList(), logicTest.executeCommand("add first meeting").getTaskList());
        assertEquals(expected1.getType(), logicTest.executeCommand("add first meeting").getType());
        
        assertEquals(expected2.getComment(), logicTest.executeCommand("add second meeting by 2016-09-01 23:59:59").getComment());
        assertEquals(expected2.getEventList(), logicTest.executeCommand("add second meeting by 2016-09-01 23:59:59").getEventList());
        assertEquals(expected2.getTaskList(), logicTest.executeCommand("add second meeting by 2016-09-01 23:59:59").getTaskList());
        assertEquals(expected2.getType(), logicTest.executeCommand("add second meeting by 2016-09-01 23:59:59").getType());
        
        assertEquals(expected3.getComment(), logicTest.executeCommand("add third meeting from 2016-10-01 23:59:59 to 2016-11-01 23:59:59").getComment());
        assertEquals(expected3.getEventList(), logicTest.executeCommand("add third meeting from 2016-10-01 23:59:59 to 2016-11-01 23:59:59").getEventList());
        assertEquals(expected3.getTaskList(), logicTest.executeCommand("add third meeting from 2016-10-01 23:59:59 to 2016-11-01 23:59:59").getTaskList());
        assertEquals(expected3.getType(), logicTest.executeCommand("add third meeting from 2016-10-01 23:59:59 to 2016-11-01 23:59:59").getType());
    }
}
