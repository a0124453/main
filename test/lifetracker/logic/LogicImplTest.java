package lifetracker.logic;

import lifetracker.calendar.CalendarList;
import lifetracker.command.AddCommand;
import lifetracker.logic.ExecuteResult.CommandType;
import lifetracker.parser.Parser;
import lifetracker.storage.Storage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@@author A0149467N

public class LogicImplTest {

    //test command
    private static final String COMMAND_SAVE = "saveat ";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_ADD_FIRST = "add first meeting";
    private static final String COMMAND_ADD_SECOND = "add second meeting by 2016-12-31 23:59:59";
    private static final String COMMAND_ADD_THIRD = "add third meeting from 2017-01-01 00:00:00 to 2017-01-01 23:59:59";
    private static final String COMMAND_ERROR_MONTH1 = "add error meeting by 2016-00-31 23:59:59";
    private static final String COMMAND_ERROR_MONTH2 = "add error meeting by 2016-13-31 23:59:59";
    private static final String COMMAND_ERROR_DAY1 = "add error meeting by 2016-12-00 23:59:59";
    private static final String COMMAND_ERROR_DAY2 = "add error meeting by 2016-12-32 23:59:59";
    private static final String COMMAND_ERROR_HOUR1 = "add error meeting by 2016-12-31 -1:59:59";
    private static final String COMMAND_ERROR_HOUR2 = "add error meeting by 2016-12-31 24:59:59";
    private static final String COMMAND_ERROR_MINUTE1 = "add error meeting by 2016-12-31 23:-1:59";
    private static final String COMMAND_ERROR_MINUTE2 = "add error meeting by 2016-12-31 23:60:59";
    private static final String COMMAND_ERROR_SECOND1 = "add error meeting by 2016-12-31 23:59:-1";
    private static final String COMMAND_ERROR_SECOND2 = "add error meeting by 2016-12-31 23:59:60";
    
    //expected comment after execution
    private static final String COMMENT_SAVE = "Calendar is saved at ";
    private static final String COMMENT_ADD_FIRST = "\"first meeting\" is added.";
    private static final String COMMENT_ADD_SECOND = "\"second meeting\" is added.";
    private static final String COMMENT_ADD_THIRD = "\"third meeting\" is added.";

    //error comment after execution
    private static final String COMMENT_INVALID_COMMAND = "Invalid Command: null";
    
    //test store constant
    private static final String DEFAULT_CONFIG_FILENAME = "config.properties";
    private static final String ALT_CONFIG_FILENAME = "config.properties.orig";
    private static final String DEFAULT_TEST_STORE = "lifetracker.dat";
    private static final String ALT_TEST_STORE = "lifetracker.dat.orig";
    private static final String TEST_SAVEAT_FILE = "location";

    private static Parser parser = mock(Parser.class);
    private static Storage storage = mock(Storage.class);
    private static LogicImpl logicTest;

    private static ExecuteResult expected1 = new ExecuteResultImpl();
    private static ExecuteResult expected2 = new ExecuteResultImpl();
    private static ExecuteResult expected3 = new ExecuteResultImpl();

    private static ExecuteResult saveat = new ExecuteResultImpl();
    private static ExecuteResult exit = new ExecuteResultImpl();
    private static ExecuteResult help = new ExecuteResultImpl();
    private static ExecuteResult error = new ExecuteResultImpl();

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        setUpStore();

        logicTest = new LogicImpl(parser, storage);

        saveat.setComment(COMMENT_SAVE + TEST_SAVEAT_FILE);
        saveat.setType(CommandType.SAVE);
        
        exit.setType(CommandType.EXIT);
        help.setType(CommandType.HELP);
        
        expected1.setComment(COMMENT_ADD_FIRST);
        expected1.setType(CommandType.DISPLAY);

        expected2.setComment(COMMENT_ADD_SECOND);
        expected2.setType(CommandType.DISPLAY);

        expected3.setComment(COMMENT_ADD_THIRD);
        expected3.setType(CommandType.DISPLAY);

        error.setComment(COMMENT_INVALID_COMMAND);
        error.setType(CommandType.ERROR);
    }

    private static void setUpStore() throws IOException {
        File origFile = new File(DEFAULT_CONFIG_FILENAME);

        if (origFile.exists()) {
            origFile.renameTo(new File(ALT_CONFIG_FILENAME));
        }

        origFile = new File(DEFAULT_TEST_STORE);

        if (origFile.exists()) {
            origFile.renameTo(new File(ALT_TEST_STORE));
        }

        when(storage.load()).thenReturn("");
    }

    @AfterClass
    public static void tearDown() {
        File testStore = new File(TEST_SAVEAT_FILE);

        if (testStore.exists()) {
            testStore.delete();
        }

        testStore = new File(DEFAULT_CONFIG_FILENAME);
        if(testStore.exists()){
            testStore.delete();
        }

        testStore = new File(DEFAULT_TEST_STORE);
        if(testStore.exists()){
            testStore.delete();
        }

        File origFile = new File(ALT_CONFIG_FILENAME);

        if (origFile.exists()) {
            origFile.renameTo(new File(DEFAULT_CONFIG_FILENAME));
        }

        origFile = new File(ALT_TEST_STORE);

        if (origFile.exists()) {
            origFile.renameTo(new File(DEFAULT_TEST_STORE));
        }

    }

    @Test
    public void testSaveat() {
        assertEquals(saveat.getComment(), logicTest.executeCommand(COMMAND_SAVE + TEST_SAVEAT_FILE).getComment());
        assertEquals(saveat.getEventList(), logicTest.executeCommand(COMMAND_SAVE + TEST_SAVEAT_FILE).getEventList());
        assertEquals(saveat.getTaskList(), logicTest.executeCommand(COMMAND_SAVE + TEST_SAVEAT_FILE).getTaskList());
        assertEquals(saveat.getType(), logicTest.executeCommand(COMMAND_SAVE + TEST_SAVEAT_FILE).getType());
    }
    
    @Test
    public void testExit() {
        assertEquals(exit.getComment(), logicTest.executeCommand(COMMAND_EXIT).getComment());
        assertEquals(exit.getEventList(), logicTest.executeCommand(COMMAND_EXIT).getEventList());
        assertEquals(exit.getTaskList(), logicTest.executeCommand(COMMAND_EXIT).getTaskList());
        assertEquals(exit.getType(), logicTest.executeCommand(COMMAND_EXIT).getType());
    }
    
    @Test
    public void testHelp() {
        assertEquals(help.getComment(), logicTest.executeCommand(COMMAND_HELP).getComment());
        assertEquals(help.getEventList(), logicTest.executeCommand(COMMAND_HELP).getEventList());
        assertEquals(help.getTaskList(), logicTest.executeCommand(COMMAND_HELP).getTaskList());
        assertEquals(help.getType(), logicTest.executeCommand(COMMAND_HELP).getType());
    }

    @Test
    public void testAddFloating() {
        AddCommand object = mock(AddCommand.class);
        CalendarList list = mock(CalendarList.class);

        when(object.getComment()).thenReturn(COMMENT_ADD_FIRST);
        when(object.execute(any(CalendarList.class))).thenReturn(list);
        when(parser.parse(COMMAND_ADD_FIRST)).thenReturn(object);

        ExecuteResult actual = logicTest.executeCommand(COMMAND_ADD_FIRST);
        verify(parser).parse(COMMAND_ADD_FIRST);

        assertEquals(expected1.getComment(), actual.getComment());
        assertEquals(expected1.getEventList(), actual.getEventList());
        assertEquals(expected1.getTaskList(), actual.getTaskList());
        assertEquals(expected1.getType(), actual.getType());
    }

    //This is the upper bound of the boundary case for the valid partition
    @Test
    public void testAddDeadline() {
        AddCommand object = mock(AddCommand.class);
        CalendarList list = mock(CalendarList.class);

        when(object.getComment()).thenReturn(COMMENT_ADD_SECOND);
        when(object.execute(any(CalendarList.class))).thenReturn(list);
        when(parser.parse(COMMAND_ADD_SECOND)).thenReturn(object);

        ExecuteResult actual = logicTest.executeCommand(COMMAND_ADD_SECOND);
        verify(parser).parse(COMMAND_ADD_SECOND);

        assertEquals(expected2.getComment(), actual.getComment());
        assertEquals(expected2.getEventList(), actual.getEventList());
        assertEquals(expected2.getTaskList(), actual.getTaskList());
        assertEquals(expected2.getType(), actual.getType());
    }

    //This is the lower bound of the boundary case for the valid partition
    @Test
    public void testAddEvent() {
        AddCommand object = mock(AddCommand.class);
        CalendarList list = mock(CalendarList.class);

        when(object.getComment()).thenReturn(COMMENT_ADD_THIRD);
        when(object.execute(any(CalendarList.class))).thenReturn(list);
        when(parser.parse(COMMAND_ADD_THIRD)).thenReturn(object);

        ExecuteResult actual = logicTest
                .executeCommand(COMMAND_ADD_THIRD);
        verify(parser).parse(COMMAND_ADD_THIRD);

        assertEquals(expected3.getComment(), actual.getComment());
        assertEquals(expected3.getEventList(), actual.getEventList());
        assertEquals(expected3.getTaskList(), actual.getTaskList());
        assertEquals(expected3.getType(), actual.getType());
    }

    //test error month
    //This is the boundary case for the invalid partition
    @Test
    public void testAddErrorMonth() {
        when(parser.parse(COMMAND_ERROR_MONTH1)).thenThrow(new IllegalArgumentException());
        when(parser.parse(COMMAND_ERROR_MONTH2)).thenThrow(new IllegalArgumentException());
        
        ExecuteResult actual1 = logicTest.executeCommand(COMMAND_ERROR_MONTH1);
        verify(parser).parse(COMMAND_ERROR_MONTH1);
        assertEquals(error.getComment(), actual1.getComment());
        assertEquals(error.getType(), actual1.getType());

        ExecuteResult actual2 = logicTest.executeCommand(COMMAND_ERROR_MONTH2);
        verify(parser).parse(COMMAND_ERROR_MONTH2);
        assertEquals(error.getComment(), actual2.getComment());
        assertEquals(error.getType(), actual2.getType());
    }
    
    //test error day
    //This is the boundary case for the invalid partition
    @Test
    public void testAddErrorDay() {
        when(parser.parse(COMMAND_ERROR_DAY1)).thenThrow(new IllegalArgumentException());
        when(parser.parse(COMMAND_ERROR_DAY2)).thenThrow(new IllegalArgumentException());
        
        ExecuteResult actual3 = logicTest.executeCommand(COMMAND_ERROR_DAY1);
        verify(parser).parse(COMMAND_ERROR_DAY1);
        assertEquals(error.getComment(), actual3.getComment());
        assertEquals(error.getType(), actual3.getType());

        ExecuteResult actual4 = logicTest.executeCommand(COMMAND_ERROR_DAY2);
        verify(parser).parse(COMMAND_ERROR_DAY2);
        assertEquals(error.getComment(), actual4.getComment());
        assertEquals(error.getType(), actual4.getType());
    }
    
    //test error hour
    //This is the boundary case for the invalid partition
    @Test
    public void testAddErrorHour() {
        when(parser.parse(COMMAND_ERROR_HOUR1)).thenThrow(new IllegalArgumentException());
        when(parser.parse(COMMAND_ERROR_HOUR2)).thenThrow(new IllegalArgumentException());

        ExecuteResult actual5 = logicTest.executeCommand(COMMAND_ERROR_HOUR1);
        verify(parser).parse(COMMAND_ERROR_HOUR1);
        assertEquals(error.getComment(), actual5.getComment());
        assertEquals(error.getType(), actual5.getType());

        ExecuteResult actual6 = logicTest.executeCommand(COMMAND_ERROR_HOUR2);
        verify(parser).parse(COMMAND_ERROR_HOUR2);
        assertEquals(error.getComment(), actual6.getComment());
        assertEquals(error.getType(), actual6.getType());
    }
    
    //test error minute
    //This is the boundary case for the invalid partition
    @Test
    public void testAddErrorMinute() {
        when(parser.parse(COMMAND_ERROR_MINUTE1)).thenThrow(new IllegalArgumentException());
        when(parser.parse(COMMAND_ERROR_MINUTE2)).thenThrow(new IllegalArgumentException());

        ExecuteResult actual7 = logicTest.executeCommand(COMMAND_ERROR_MINUTE1);
        verify(parser).parse(COMMAND_ERROR_MINUTE1);
        assertEquals(error.getComment(), actual7.getComment());
        assertEquals(error.getType(), actual7.getType());

        ExecuteResult actual8 = logicTest.executeCommand(COMMAND_ERROR_MINUTE2);
        verify(parser).parse(COMMAND_ERROR_MINUTE2);
        assertEquals(error.getComment(), actual8.getComment());
        assertEquals(error.getType(), actual8.getType());
    }
    
    //test error second
    //This is the boundary case for the invalid partition
    @Test
    public void testAddErrorSecond() {
        when(parser.parse(COMMAND_ERROR_SECOND1)).thenThrow(new IllegalArgumentException());
        when(parser.parse(COMMAND_ERROR_SECOND2)).thenThrow(new IllegalArgumentException());
        
        ExecuteResult actual9 = logicTest.executeCommand(COMMAND_ERROR_SECOND1);
        verify(parser).parse(COMMAND_ERROR_SECOND1);
        assertEquals(error.getComment(), actual9.getComment());
        assertEquals(error.getType(), actual9.getType());

        ExecuteResult actual10 = logicTest.executeCommand(COMMAND_ERROR_SECOND2);
        verify(parser).parse(COMMAND_ERROR_SECOND2);
        assertEquals(error.getComment(), actual10.getComment());
        assertEquals(error.getType(), actual10.getType());
    }
}
