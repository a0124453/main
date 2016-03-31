package lifetracker.integration;

import lifetracker.command.CommandFactoryImpl;
import lifetracker.logic.CommandLineResult;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;
import lifetracker.logic.LogicImpl;
import lifetracker.parser.ParserImpl;
import lifetracker.storage.Storage;
import lifetracker.storage.ThreadedFileStorage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//@@author A0091173J
public class IntegrationLogicTest {

    private static final String MESSAGE_ADD = "\"%1$s\" is added.";

    private static final String DEFAULT_FILENAME = "lifetracker.dat";
    private static final String ALT_FILENAME = "lifetracker.dat.orig";

    private Storage storage;
    private Logic logic;

    @BeforeClass
    public static void setUpTestFile() throws Exception {
        File origFile = new File(DEFAULT_FILENAME);

        if (origFile.exists()) {
            origFile.renameTo(new File(ALT_FILENAME));
        }
    }

    @AfterClass
    public static void tearDownTestFile() throws Exception {
        File origFile = new File(ALT_FILENAME);

        if (origFile.exists()) {
            origFile.renameTo(new File(DEFAULT_FILENAME));
        }
    }

    @Before
    public void setUp() throws Exception {
        storage = new ThreadedFileStorage(DEFAULT_FILENAME);

        logic = new LogicImpl(new ParserImpl(new CommandFactoryImpl()), storage);

    }

    @After
    public void tearDown() throws Exception {
        storage.close();
        File testFile = new File(DEFAULT_FILENAME);
        testFile.delete();
    }

    @Test
    public void testAddFloating() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new CommandLineResult();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        //Partition: floating task
        actual = logic.executeCommand("add floating");
        expected.addTaskLine(1, "floating", true, null, null);
        expected.setComment(String.format(MESSAGE_ADD, "floating"));
        assertExecuteResult(expected, actual);

        //Boundary: no command
        actual = logic.executeCommand("free floating");
        expected.addTaskLine(2, "free floating", true, null, null);
        expected.setComment(String.format(MESSAGE_ADD, "free floating"));
        assertExecuteResult(expected, actual);

        //Boundary: reserved words
        actual = logic.executeCommand("add add");
        expected.addTaskLine(3, "add", true, null, null);
        expected.setComment(String.format(MESSAGE_ADD, "add"));
        assertExecuteResult(expected, actual);

        //Boundary: Empty name
        actual = logic.executeCommand("add");
        ExecuteResult error = new CommandLineResult();
        error.setType(ExecuteResult.CommandType.ERROR);
        error.setComment("Invalid Command: Task/Event's name cannot be empty!");
        assertExecuteResult(error, actual);

    }

    @Test
    public void testAddDeadLine() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new CommandLineResult();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        //Partition: Deadline tasks
        actual = logic.executeCommand("meeting by 12-5-16 3.30pm");
        expected.addTaskLine(1, "meeting", true, LocalDateTime.of(LocalDate.of(2016,5, 12), LocalTime.of(15, 30)), null);
        expected.setComment(String.format(MESSAGE_ADD, "meeting"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing date
        actual = logic.executeCommand("assignment by 1534");
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(15,34));
        if(expectedDateTime.isBefore(LocalDateTime.now())){
            expectedDateTime = expectedDateTime.plusDays(1);
        }
        expected.addTaskLine(2, "assignment", true, expectedDateTime, null);
        expected.setComment(String.format(MESSAGE_ADD, "assignment"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing Time
        actual = logic.executeCommand("code review by tomorrow");
        expectedDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT.minusMinutes(1));
        expected.addTaskLine(3,"code review", true,expectedDateTime, null);
        expected.setComment(String.format(MESSAGE_ADD, "code review"));
        assertExecuteResult(expected,actual);

        //Boundary: Invalid identifier
        actual = logic.executeCommand("meeting by boss");
        expected.addTaskLine(4, "meeting by boss", true, null, null);
        expected.setComment(String.format(MESSAGE_ADD, "meeting by boss"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing name
        actual = logic.executeCommand("by 2pm");
        ExecuteResult error = new CommandLineResult();
        error.setType(ExecuteResult.CommandType.ERROR);
        error.setComment("Invalid Command: Task/Event's name cannot be empty!");
        assertExecuteResult(error, actual);
    }

    public void assertExecuteResult(ExecuteResult expected, ExecuteResult actual) {
        Assert.assertEquals(expected.getTaskList(), actual.getTaskList());
        Assert.assertEquals(expected.getTaskList(), actual.getTaskList());
        Assert.assertEquals(expected.getComment(), actual.getComment());
        Assert.assertEquals(expected.getType(), actual.getType());
    }
}
