package lifetracker.integration;

import lifetracker.command.CommandFactoryImpl;
import lifetracker.logic.ExecuteResultImpl;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

//@@author A0091173J
public class IntegrationLogicTest {

    private static final String MESSAGE_ADD = "\"%1$s\" is added.";
    private static final String MESSAGE_DELETE = "%1$d is deleted.";

    private static final String CONFIG_FILE = "config.properties";
    private static final String SAVE_FILE_PROPERTY = "savefile";

    private static final String ALT_FILENAME_EXTENSION = ".orig";

    private static final String LOG_FOLDER = "logs/";
    private static final String LOG_FILE = "lifetracker_test.log";

    private static String storageFileName;
    private Storage storage;
    private Logic logic;

    @BeforeClass
    public static void setUpTestFile() throws Exception {
        File logDir = new File(LOG_FOLDER);

        if (!logDir.exists()) {
            logDir.mkdir();
        }

        LogManager.getLogManager().reset();

        Logger globalLogger = Logger.getGlobal();
        globalLogger.addHandler(new FileHandler(LOG_FOLDER + LOG_FILE));

        File configFile = new File(CONFIG_FILE);
        if(!configFile.exists()){
            configFile.createNewFile();
        }

        Properties properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream(CONFIG_FILE)));
        storageFileName = properties.getProperty(SAVE_FILE_PROPERTY, "lifetracker.dat");

        File origFile = new File(storageFileName);

        if (origFile.exists()) {
            origFile.renameTo(new File(storageFileName + ALT_FILENAME_EXTENSION));
        }
    }

    @AfterClass
    public static void tearDownTestFile() throws Exception {
        File origFile = new File(storageFileName + ALT_FILENAME_EXTENSION);

        if (origFile.exists()) {
            origFile.renameTo(new File(storageFileName));
        }
    }

    @Before
    public void setUp() throws Exception {
        storage = new ThreadedFileStorage();
        logic = new LogicImpl(new ParserImpl(new CommandFactoryImpl()), storage);
    }

    @After
    public void tearDown() throws Exception {
        storage.close();
        File testFile = new File(storageFileName);
        testFile.delete();
    }

    @Test
    public void testAddFloating() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        //Partition: Add floating task
        actual = logic.executeCommand("add floating");
        expected.addTaskLine(1, "floating", null, false, true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "floating"));
        assertExecuteResult(expected, actual);

        //Boundary: no command
        actual = logic.executeCommand("free floating");
        expected.addTaskLine(2, "free floating", null, false, true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "free floating"));
        assertExecuteResult(expected, actual);

        //Boundary: reserved words
        actual = logic.executeCommand("add add");
        expected.addTaskLine(3, "add", null, false, true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "add"));
        assertExecuteResult(expected, actual);

        //Boundary: Empty name
        actual = logic.executeCommand("add");
        ExecuteResult error = new ExecuteResultImpl();
        error.setType(ExecuteResult.CommandType.ERROR);
        error.setComment("Invalid Command: Task/Event's name cannot be empty!");
        assertExecuteResult(error, actual);

    }

    @Test
    public void testAddDeadLine() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        //Partition: Add deadline tasks
        actual = logic.executeCommand("meeting by 12-5-15 3.30pm");
        expected.addTaskLine(1, "meeting", LocalDateTime.of(LocalDate.of(2015, 5, 12), LocalTime.of(15, 30)), true,
                true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "meeting"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing date
        actual = logic.executeCommand("assignment by 1534");
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 34));
        if (expectedDateTime.isBefore(LocalDateTime.now())) {
            expectedDateTime = expectedDateTime.plusDays(1);
        }
        expected.addTaskLine(2, "assignment", expectedDateTime, false, true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "assignment"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing Time
        actual = logic.executeCommand("code review by tomorrow");
        expectedDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT.minusMinutes(1));
        expected.addTaskLine(3, "code review", expectedDateTime, false, true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "code review"));
        assertExecuteResult(expected, actual);

        //Boundary: Invalid identifier
        actual = logic.executeCommand("meeting by boss");
        expected.addTaskLine(4, "meeting by boss", null, false, true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "meeting by boss"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing name
        actual = logic.executeCommand("by 2pm");
        ExecuteResult error = new ExecuteResultImpl();
        error.setType(ExecuteResult.CommandType.ERROR);
        error.setComment("Invalid Command: Task/Event's name cannot be empty!");
        assertExecuteResult(error, actual);
    }

    @Test
    public void testAddRecurringDeadlineTask() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        //Partition: Add recurring deadline tasks
        actual = logic.executeCommand("add report by 14/1/15 2pm every 2 days");
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.of(2015,1,14), LocalTime.of(14, 0));
        expected.addTaskLine(1, "report", expectedDateTime, true, true, Period.ofDays(2), -1, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "report"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing date time
        actual = logic.executeCommand("pump bicycle wheels every 2 weeks");
        expectedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT.minusMinutes(1));
        expected.addTaskLine(2, "pump bicycle wheels", expectedDateTime, false, true, Period.ofWeeks(2), -1, null,
                false);
        expected.setComment(String.format(MESSAGE_ADD, "pump bicycle wheels"));
        assertExecuteResult(expected, actual);

        //Boundary: Invalid recurring period identifier
        actual = logic.executeCommand("meeting every now and then");
        expected.addTaskLine(3, "meeting every now and then", null, false, true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "meeting every now and then"));
        assertExecuteResult(expected, actual);
    }

    @Test
    public void testAddEvents() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        LocalDateTime expectedStartDateTime;
        LocalDateTime expectedEndDateTime;

        //Partition: Add events
        actual = logic.executeCommand("add interview from 22/3/16 2.30pm to 22/3/16 4pm");
        expectedStartDateTime = LocalDateTime.of(2016, 3, 22, 14, 30);
        expectedEndDateTime = LocalDateTime.of(2016, 3, 22, 16, 0);
        expected.addEventLine(1, "interview", expectedStartDateTime, expectedEndDateTime, false, true, null, -1, null,
                false);
        expected.setComment(String.format(MESSAGE_ADD, "interview"));
        assertExecuteResult(expected, actual);
    }

    @Test
    public void testAddRecurringEvents() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        LocalDateTime expectedStartDateTime;
        LocalDateTime expectedEndDateTime;
        Period expectedRecurringPeriod;

        //Partition: Add recurring events
        actual = logic.executeCommand("add tutorial from 24/3/16 3pm to 4pm every week");
        expectedStartDateTime = LocalDateTime.of(2016, 3, 24, 15, 0);
        expectedEndDateTime = expectedStartDateTime.plusHours(1);
        expectedRecurringPeriod = Period.ofWeeks(1);
        while (expectedEndDateTime.isBefore(LocalDateTime.now())) {
            expectedStartDateTime = expectedStartDateTime.plus(expectedRecurringPeriod);
            expectedEndDateTime = expectedEndDateTime.plus(expectedRecurringPeriod);
        }
        expected.addEventLine(1, "tutorial", expectedStartDateTime, expectedEndDateTime, false, true,
                expectedRecurringPeriod, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "tutorial"));
        assertExecuteResult(expected, actual);
    }

    @Test
    public void testDeleteEntries() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        logic.executeCommand("add floating task 1");
        logic.executeCommand("add floating task 2");

        //Partition: Delete existing entry
        actual = logic.executeCommand("delete 1");
        expected.addTaskLine(2, "floating task 2", null, false, true, null, 0, null, false);
        expected.setComment(String.format(MESSAGE_DELETE, 1));
        assertExecuteResult(expected, actual);

        //Boundary: Delete non-existent entry
        actual = logic.executeCommand("delete 100");
        ExecuteResult error = new ExecuteResultImpl();
        error.setType(ExecuteResult.CommandType.ERROR);
        error.setComment("Invalid Command: Entry 100 is not found!");
        assertExecuteResult(error, actual);

        //Boundary: Invalid ID
        actual = logic.executeCommand("delete");
        error.setComment("Invalid Command: \"\" is not a valid ID!");
        assertExecuteResult(error, actual);
    }

    @Test
    public void testSearch() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        logic.executeCommand("abcd efg");
        logic.executeCommand("xyzt");
        logic.executeCommand("xyzt from 22/3/16 2pm to 3pm");

        //Partition: keyword search
        actual = logic.executeCommand("search xyzt");
        expected.addTaskLine(2, "xyzt", null, false, true, null, 0, null, false);
        expected.addEventLine(3, "xyzt", LocalDateTime.of(2016, 3, 22, 14, 0), LocalDateTime.of(2016, 3, 22, 15, 0),
                false, false, null, 0, null, false);
        expected.setComment("Displaying entries with: \"xyzt\".");
        assertExecuteResult(expected, actual);

        //Boundary: With other keywords
        actual = logic.executeCommand("find xyzt");
        assertExecuteResult(expected, actual);

        actual = logic.executeCommand("list xyzt");
        assertExecuteResult(expected, actual);

        //Boundary: Case insensitivity
        actual = logic.executeCommand("search XYZT");
        expected.setComment("Displaying entries with: \"XYZT\".");
        assertExecuteResult(expected, actual);

        //Partition: Search all
        actual = logic.executeCommand("find");
        expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        expected.addTaskLine(1, "abcd efg", null, false, true, null, 0, null, false);
        expected.addTaskLine(2, "xyzt", null, false, true, null, 0, null, false);
        expected.addEventLine(3, "xyzt", LocalDateTime.of(2016, 3, 22, 14, 0), LocalDateTime.of(2016, 3, 22, 15, 0),
                false, false, null, 0, null, false);
        expected.setComment("Displaying all entries.");
        assertExecuteResult(expected, actual);

        //Boundary: With other keywords
        actual = logic.executeCommand("search");
        assertExecuteResult(expected, actual);

        actual = logic.executeCommand("list");
        assertExecuteResult(expected, actual);
    }

    public void assertExecuteResult(ExecuteResult expected, ExecuteResult actual) {
        Assert.assertEquals(expected.getTaskList(), actual.getTaskList());
        Assert.assertEquals(expected.getTaskList(), actual.getTaskList());
        Assert.assertEquals(expected.getComment(), actual.getComment());
        Assert.assertEquals(expected.getType(), actual.getType());
    }
}
