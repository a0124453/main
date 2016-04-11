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
        if (!configFile.exists()) {
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
        expected.addTaskLine(1, "floating", null, false, true, null, 0, null, true);
        expected.setComment(String.format(MESSAGE_ADD, "floating"));
        assertExecuteResult(expected, actual);

        //Boundary: no command
        actual = logic.executeCommand("free floating");

        expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        expected.addTaskLine(1, "floating", null, false, true, null, 0, null, false);
        expected.addTaskLine(2, "free floating", null, false, true, null, 0, null, true);
        expected.setComment(String.format(MESSAGE_ADD, "free floating"));
        assertExecuteResult(expected, actual);

        //Boundary: reserved words
        actual = logic.executeCommand("add add");

        expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        expected.addTaskLine(1, "floating", null, false, true, null, 0, null, false);
        expected.addTaskLine(2, "free floating", null, false, true, null, 0, null, false);
        expected.addTaskLine(3, "add", null, false, true, null, 0, null, true);
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
        LocalDateTime task1ExpectedDateTime = LocalDateTime.of(LocalDate.of(2015, 5, 12), LocalTime.of(15, 30));
        expected.addTaskLine(1, "meeting", task1ExpectedDateTime, true, true, null, 0, null, true);
        expected.setComment(String.format(MESSAGE_ADD, "meeting"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing date
        actual = logic.executeCommand("assignment by 1534");
        LocalDateTime task2ExpectedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 34));
        if (task2ExpectedDateTime.isBefore(LocalDateTime.now())) {
            task2ExpectedDateTime = task2ExpectedDateTime.plusDays(1);
        }

        expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        expected.addTaskLine(1, "meeting", task1ExpectedDateTime, true, true, null, 0, null, false);
        expected.addTaskLine(2, "assignment", task2ExpectedDateTime, false, true, null, 0, null, true);
        expected.setComment(String.format(MESSAGE_ADD, "assignment"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing Time
        actual = logic.executeCommand("code review by tomorrow");
        LocalDateTime task3ExpectedDateTime = LocalDateTime
                .of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT.minusMinutes(1));

        expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        expected.addTaskLine(1, "meeting", task1ExpectedDateTime, true, true, null, 0, null, false);
        expected.addTaskLine(2, "assignment", task2ExpectedDateTime, false, true, null, 0, null, false);
        expected.addTaskLine(3, "code review", task3ExpectedDateTime, false, true, null, 0, null, true);
        expected.setComment(String.format(MESSAGE_ADD, "code review"));
        assertExecuteResult(expected, actual);

        //Boundary: Invalid identifier
        actual = logic.executeCommand("meeting by boss");
        expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        expected.addTaskLine(1, "meeting", task1ExpectedDateTime, true, true, null, 0, null, false);
        expected.addTaskLine(2, "assignment", task2ExpectedDateTime, false, true, null, 0, null, false);
        expected.addTaskLine(3, "code review", task3ExpectedDateTime, false, true, null, 0, null, false);
        expected.addTaskLine(4, "meeting by boss", null, false, true, null, 0, null, true);
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
        LocalDateTime task1ExpectedDeadline = LocalDateTime.of(LocalDate.of(2015, 1, 14), LocalTime.of(14, 0));
        expected.addTaskLine(1, "report", task1ExpectedDeadline, true, true, Period.ofDays(2), -1, null, true);
        expected.setComment(String.format(MESSAGE_ADD, "report"));
        assertExecuteResult(expected, actual);

        //Boundary: Missing date time
        actual = logic.executeCommand("pump bicycle wheels every 2 weeks");
        LocalDateTime task2ExpectedDeadline = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT.minusMinutes(1));
        expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        expected.addTaskLine(1, "report", task1ExpectedDeadline, true, true, Period.ofDays(2), -1, null, false);
        expected.addTaskLine(2, "pump bicycle wheels", task2ExpectedDeadline, false, true, Period.ofWeeks(2), -1, null,
                true);
        expected.setComment(String.format(MESSAGE_ADD, "pump bicycle wheels"));
        assertExecuteResult(expected, actual);

        //Boundary: Invalid recurring period identifier
        actual = logic.executeCommand("meeting every now and then");

        expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);
        expected.addTaskLine(1, "report", task1ExpectedDeadline, true, true, Period.ofDays(2), -1, null, false);
        expected.addTaskLine(2, "pump bicycle wheels", task2ExpectedDeadline, false, true, Period.ofWeeks(2), -1, null,
                false);
        expected.addTaskLine(3, "meeting every now and then", null, false, true, null, 0, null, true);
        expected.setComment(String.format(MESSAGE_ADD, "meeting every now and then"));
        assertExecuteResult(expected, actual);
    }

    @Test
    public void testAddEvents() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        //Partition: Add events
        actual = logic.executeCommand("add interview from 22/3/16 2.30pm to 22/3/16 4pm");
        LocalDateTime expectedStartDateTime1 = LocalDateTime.of(2016, 3, 22, 14, 30);
        LocalDateTime expectedEndDateTime1 = LocalDateTime.of(2016, 3, 22, 16, 0);
        expected.addEventLine(1, "interview", expectedStartDateTime1, expectedEndDateTime1, false, true, null, -1, null,
                false);
        expected.setComment(String.format(MESSAGE_ADD, "interview"));
        assertExecuteResult(expected, actual);
        
        //@@author A0114240B
        //Partition: Add events with missing end date parameters
        actual = logic.executeCommand("dinner from 23/3/16 2.30pm");
        LocalDateTime expectedStartDateTime2 = LocalDateTime.of(2016, 3, 23, 14, 30);
        LocalDateTime expectedEndDateTime2 = LocalDateTime.of(2016, 3, 23, 15, 30);
        expected.addEventLine(1, "interview", expectedStartDateTime1, expectedEndDateTime1, false, true, null, -1, null,
                false);
        expected.addEventLine(2, "dinner", expectedStartDateTime2, expectedEndDateTime2, false, true, null, -1, null,
                false);
        expected.setComment(String.format(MESSAGE_ADD, "dinner"));
        assertExecuteResult(expected, actual);
        actual = logic.executeCommand("chalet from 23/3/16 2.30pm to 24/3/16");
        LocalDateTime expectedStartDateTime3 = LocalDateTime.of(2016, 3, 23, 14, 30);
        LocalDateTime expectedEndDateTime3 = LocalDateTime.of(2016, 3, 24, 14, 30);
        expected.addEventLine(1, "interview", expectedStartDateTime1, expectedEndDateTime1, false, true, null, -1, null,
                false);
        expected.addEventLine(2, "dinner", expectedStartDateTime2, expectedEndDateTime2, false, true, null, -1, null,
                false);
        expected.addEventLine(3, "chalet", expectedStartDateTime3, expectedEndDateTime3, false, true, null, -1, null,
                false);
        expected.setComment(String.format(MESSAGE_ADD, "chalet"));
        assertExecuteResult(expected, actual);
        actual = logic.executeCommand("class from 23/3/16 2.30pm to 7pm");
        LocalDateTime expectedStartDateTime4 = LocalDateTime.of(2016, 3, 23, 14, 30);
        LocalDateTime expectedEndDateTime4 = LocalDateTime.of(2016, 3, 23, 19, 0);
        expected.addEventLine(1, "interview", expectedStartDateTime1, expectedEndDateTime1, false, true, null, -1, null,
                false);
        expected.addEventLine(2, "dinner", expectedStartDateTime2, expectedEndDateTime2, false, true, null, -1, null,
                false);
        expected.addEventLine(3, "chalet", expectedStartDateTime3, expectedEndDateTime3, false, true, null, -1, null,
                false);
        expected.addEventLine(4, "class", expectedStartDateTime4, expectedEndDateTime4, false, true, null, -1, null,
                false);
        expected.setComment(String.format(MESSAGE_ADD, "class"));
        assertExecuteResult(expected, actual);
        
        //Partion: Add events with missing start parameters
        actual = logic.executeCommand("swim from 23/3/16 to 7pm");
        LocalDateTime expectedStartDateTime5 = LocalDateTime.of(2016, 3, 23, 18, 00);
        LocalDateTime expectedEndDateTime5 = LocalDateTime.of(2016, 3, 23, 19, 0);
        expected.addEventLine(1, "interview", expectedStartDateTime1, expectedEndDateTime1, false, true, null, -1, null,
                false);
        expected.addEventLine(2, "dinner", expectedStartDateTime2, expectedEndDateTime2, false, true, null, -1, null,
                false);
        expected.addEventLine(3, "chalet", expectedStartDateTime3, expectedEndDateTime3, false, true, null, -1, null,
                false);
        expected.addEventLine(4, "class", expectedStartDateTime4, expectedEndDateTime4, false, true, null, -1, null,
                false);
        expected.addEventLine(5, "swim", expectedStartDateTime5, expectedEndDateTime5, false, true, null, -1, null,
                false);
        expected.setComment(String.format(MESSAGE_ADD, "swim"));
        assertExecuteResult(expected, actual);
        
    }

    //@@author A0091173J
    @Test
    public void testAddRecurringEvents() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new ExecuteResultImpl();
        expected.setType(ExecuteResult.CommandType.DISPLAY);

        //Partition: Add recurring events
        actual = logic.executeCommand("add tutorial from 24/3/16 3pm to 4pm every week");
        LocalDateTime expectedStartDateTime1 = LocalDateTime.of(2016, 3, 24, 15, 0);
        LocalDateTime expectedEndDateTime1 = expectedStartDateTime1.plusHours(1);
        Period expectedRecurringPeriod1 = Period.ofWeeks(1);
        while (expectedEndDateTime1.isBefore(LocalDateTime.now())) {
            expectedStartDateTime1 = expectedStartDateTime1.plus(expectedRecurringPeriod1);
            expectedEndDateTime1 = expectedEndDateTime1.plus(expectedRecurringPeriod1);
        }
        expected.addEventLine(1, "tutorial", expectedStartDateTime1, expectedEndDateTime1, false, true,
                expectedRecurringPeriod1, 0, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "tutorial"));
        assertExecuteResult(expected, actual);
        
        //@@author A0114240B
        //Partition: Add recurring events with limitOccur
        actual = logic.executeCommand("add lesson from 24/3/16 3pm to 4pm every week for 5");
        LocalDateTime expectedStartDateTime2 = LocalDateTime.of(2016, 3, 24, 15, 0);
        LocalDateTime expectedEndDateTime2 = expectedStartDateTime2.plusHours(1);
        Period expectedRecurringPeriod2 = Period.ofWeeks(1);
        while (expectedEndDateTime2.isBefore(LocalDateTime.now())) {
            expectedStartDateTime2 = expectedStartDateTime2.plus(expectedRecurringPeriod2);
            expectedEndDateTime2 = expectedEndDateTime2.plus(expectedRecurringPeriod2);
        }
        expected.addEventLine(1, "tutorial", expectedStartDateTime2, expectedEndDateTime1, false, true,
                expectedRecurringPeriod1, 0, null, false);
        expected.addEventLine(2, "lesson", expectedStartDateTime2, expectedEndDateTime1, false, true,
                expectedRecurringPeriod1, 5, null, false);
        expected.setComment(String.format(MESSAGE_ADD, "lesson"));
        assertExecuteResult(expected, actual);
        
        //Partition: Add recurring events with limit date
        actual = logic.executeCommand("add work from 24/3/16 3pm to 4pm every week until 25/3/16");
        LocalDateTime expectedStartDateTime3 = LocalDateTime.of(2016, 3, 24, 15, 0);
        LocalDateTime expectedEndDateTime3 = expectedStartDateTime3.plusHours(1);
        LocalDate limitDate = LocalDate.of(2016, 3, 25);
        Period expectedRecurringPeriod3 = Period.ofWeeks(1);
        while (expectedEndDateTime3.isBefore(LocalDateTime.now())) {
            expectedStartDateTime3 = expectedStartDateTime3.plus(expectedRecurringPeriod3);
            expectedEndDateTime3 = expectedEndDateTime3.plus(expectedRecurringPeriod3);
        }
        expected.addEventLine(1, "tutorial", expectedStartDateTime1, expectedEndDateTime2, false, true,
                expectedRecurringPeriod1, 0, null, false);
        expected.addEventLine(2, "lesson", expectedStartDateTime2, expectedEndDateTime2, false, true,
                expectedRecurringPeriod1, 5, null, false);
        expected.addEventLine(2, "work", expectedStartDateTime2, expectedEndDateTime2, false, true,
                expectedRecurringPeriod1, 0, limitDate, false);
        expected.setComment(String.format(MESSAGE_ADD, "work"));
        assertExecuteResult(expected, actual);
    }

    //@@author A0091173J
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
        expected.setComment("Displaying entries.");
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
