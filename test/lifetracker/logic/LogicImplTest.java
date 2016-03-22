package lifetracker.logic;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import lifetracker.parser.Parser;
import lifetracker.storage.Storage;

public class LogicImplTest {
    
    private static Parser parser = new LogicParserStub();
    private static Storage storage = new LogicStorageStub();
    private static LogicImpl logicTest = new LogicImpl(parser, storage);
    
    private static ExecuteResult expected1 = new CommandLineResult();
    private static ExecuteResult expected2 = new CommandLineResult();
    private static ExecuteResult expected3 = new CommandLineResult();
    
    @BeforeClass
    public static void setUpBeforeClass() {
        expected1.setComment("\"first meeting\" is added.");
        expected1.setType("add first meeting");
        
        expected2.setComment("\"second meeting\" is added.");
        expected2.setType("add second meeting by 2016-09-01 23:59:59");
        
        expected3.setComment("\"third meeting\" is added.");
        expected3.setType("add third meeting from 2016-10-01 23:59:59 to 2016-11-01 23:59:59");
    }

    @Test
    public void testAdd() {
        assertEquals(expected1, logicTest.executeCommand("add first meeting"));
        assertEquals(expected2, logicTest.executeCommand("add second meeting by 2016-09-01 23:59:59"));
        assertEquals(expected3, logicTest.executeCommand("add third meeting from 2016-10-01 23:59:59 to 2016-11-01 23:59:59"));
    }
}
