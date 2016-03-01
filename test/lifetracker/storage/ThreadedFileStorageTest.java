package lifetracker.storage;

import lifetracker.calendar.CalendarList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class ThreadedFileStorageTest {

    public static final String TEST_FILE_NAME = "test.dat";
    public static final String ALT_TEST_FILE_NAME = "alt_test.dat";

    public ThreadedFileStorage storage;

    public CalendarList testCalendarStub;

    @Before
    public void setUp() throws Exception {
        storage = new ThreadedFileStorage(TEST_FILE_NAME);
        testCalendarStub = new StorageCalendarStub();
    }

    @After
    public void tearDown() throws Exception {
        File testFile = new File(TEST_FILE_NAME);
        File altTestFile = new File(ALT_TEST_FILE_NAME);

        if (testFile.exists()) {
            testFile.delete();
        }

        if (altTestFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testSetStore() throws Exception {
        storage.setStore(ALT_TEST_FILE_NAME);

        Assert.assertEquals(true, new File(ALT_TEST_FILE_NAME).exists());
    }

    @Test
    public void testStore() throws Exception {

    }

    @Test
    public void testLoad() throws Exception {

    }
}