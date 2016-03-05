package lifetracker.storage;

import lifetracker.calendar.CalendarList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ThreadedFileStorageTest {

    public static final String TEST_FILE_NAME = "test.dat";
    public static final String ALT_TEST_FILE_NAME = "alt_test.dat";

    public ThreadedFileStorage storage;

    public CalendarList testCalendarStub;

    @Before
    public void setUp() throws Exception {
        storage = new ThreadedFileStorage(TEST_FILE_NAME);
        testCalendarStub = new StorageCalendarStub(true);
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

        storage.store(testCalendarStub);

        storage.setStore(ALT_TEST_FILE_NAME);

        Assert.assertEquals(true, new File(ALT_TEST_FILE_NAME).exists());

        storage.store(testCalendarStub);
        storage.flush();

        List<String> originalFileContent = Files.readAllLines(Paths.get(TEST_FILE_NAME));
        List<String> altFileContent = Files.readAllLines(Paths.get(ALT_TEST_FILE_NAME));

        Assert.assertEquals(originalFileContent, altFileContent);
    }

    @Test
    public void testStore() throws Exception {
        CalendarList emptyCalendar = new StorageCalendarStub(false);

        storage.store(emptyCalendar);
        storage.flush();

        List<String> expectedFileContent = new ArrayList<>();
        expectedEmptyFileContent.add("0");
        expectedEmptyFileContent.add("0");

        List<String> actualFileContent = Files.readAllLines(Paths.get(TEST_FILE_NAME));

        Assert.assertEquals(expectedFileContent, actualFileContent);

        CalendarList populatedCalendar = new StorageCalendarStub(true);

        storage.store(populatedCalendar);
        storage.flush();

        expectedFileContent.clear();
        expectedFileContent.add("2");
        expectedFileContent.add("Test Task 1");
        expectedFileContent.add("Test Task 2 2007-12-03T10:15:30 2016-03-14T23:59:59");
        expectedFileContent.add("2");
        expectedFileContent.add("Test Event 1 2016-03-14T23:59:59 2016-03-15T23:59:59");
        expectedFileContent.add("Test Event 2 2016-03-14T11:59:59 2016-03-14T23:59:59");

        actualFileContent = Files.readAllLines(Paths.get(TEST_FILE_NAME));

        Assert.assertEquals(expectedFileContent, actualFileContent);
    }

    @Test
    public void testLoad() throws Exception {

    }
}