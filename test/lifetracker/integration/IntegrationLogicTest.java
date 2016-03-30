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
        File testFile = new File(DEFAULT_FILENAME);

        testFile.delete();

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
    }

    @Test
    public void testAdd() throws Exception {
        ExecuteResult actual;
        ExecuteResult expected = new CommandLineResult();

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

    }

    public void assertExecuteResult(ExecuteResult expected, ExecuteResult actual) {
        Assert.assertEquals(expected.getTaskList(), actual.getTaskList());
        Assert.assertEquals(expected.getTaskList(), actual.getTaskList());
        Assert.assertEquals(expected.getComment(), actual.getComment());
    }
}
