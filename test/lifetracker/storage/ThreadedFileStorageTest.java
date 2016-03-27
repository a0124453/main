package lifetracker.storage;

import lifetracker.calendar.CalendarList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ThreadedFileStorageTest {

    public static final String TEST_FILE_NAME = "test.dat";
    public static final String ALT_TEST_FILE_NAME = "alt_test.dat";

    public static final String jsonTestData = "{\\n  \"taskList\": {\\n    \"1\": {\\n      \"name\": \"floating\",\\n      \"entryType\": \"FLOATING\",\\n      \"id\": 1\\n    },\\n    \"2\": {\\n      \"name\": \"task\",\\n      \"endDateTime\": {\\n        \"date\": {\\n          \"year\": 999999999,\\n          \"month\": 12,\\n          \"day\": 31\\n        },\\n        \"time\": {\\n          \"hour\": 23,\\n          \"minute\": 59,\\n          \"second\": 59,\\n          \"nano\": 999999999\\n        }\\n      },\\n      \"entryType\": \"DEADLINE\",\\n      \"id\": 2\\n    },\\n    \"3\": {\\n      \"name\": \"recurring task\",\\n      \"endDateTime\": {\\n        \"date\": {\\n          \"year\": 2016,\\n          \"month\": 3,\\n          \"day\": 27\\n        },\\n        \"time\": {\\n          \"hour\": 11,\\n          \"minute\": 29,\\n          \"second\": 43,\\n          \"nano\": 322000000\\n        }\\n      },\\n      \"entryType\": \"DEADLINE\",\\n      \"id\": 3\\n    }\\n  },\\n  \"eventList\": {\\n    \"4\": {\\n      \"name\": \"event\",\\n      \"startDateTime\": {\\n        \"date\": {\\n          \"year\": -999999999,\\n          \"month\": 1,\\n          \"day\": 1\\n        },\\n        \"time\": {\\n          \"hour\": 0,\\n          \"minute\": 0,\\n          \"second\": 0,\\n          \"nano\": 0\\n        }\\n      },\\n      \"endDateTime\": {\\n        \"date\": {\\n          \"year\": 999999999,\\n          \"month\": 12,\\n          \"day\": 31\\n        },\\n        \"time\": {\\n          \"hour\": 23,\\n          \"minute\": 59,\\n          \"second\": 59,\\n          \"nano\": 999999999\\n        }\\n      },\\n      \"entryType\": \"EVENT\",\\n      \"id\": 4\\n    },\\n    \"5\": {\\n      \"name\": \"recurring event\",\\n      \"startDateTime\": {\\n        \"date\": {\\n          \"year\": -999999999,\\n          \"month\": 1,\\n          \"day\": 1\\n        },\\n        \"time\": {\\n          \"hour\": 0,\\n          \"minute\": 0,\\n          \"second\": 0,\\n          \"nano\": 0\\n        }\\n      },\\n      \"endDateTime\": {\\n        \"date\": {\\n          \"year\": 999999999,\\n          \"month\": 12,\\n          \"day\": 31\\n        },\\n        \"time\": {\\n          \"hour\": 23,\\n          \"minute\": 59,\\n          \"second\": 59,\\n          \"nano\": 999999999\\n        }\\n      },\\n      \"entryType\": \"EVENT\",\\n      \"id\": 5\\n    }\\n  }\\n}";

    public ThreadedFileStorage storage;

    @Before
    public void setUp() throws Exception {
        storage = new ThreadedFileStorage(TEST_FILE_NAME);
        deleteTestFiles();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestFiles();
    }

    @Test
    public void testSetStore() throws Exception {
        //Partition: Changing the save file
        String testString = "Line 1\nLine 2";

        storage.store(testString);

        storage.setStore(ALT_TEST_FILE_NAME);

        Assert.assertEquals(true, new File(ALT_TEST_FILE_NAME).exists());

        storage.store(testString);
        storage.close();

        String originalFileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);
        String altFileContent = new String(Files.readAllBytes(Paths.get(ALT_TEST_FILE_NAME)), StandardCharsets.UTF_8);

        Assert.assertEquals(originalFileContent, altFileContent);

        //Boundary: When file already exists
        storage = new ThreadedFileStorage(TEST_FILE_NAME);
        testString = "New Line 1\nNew Line 2";

        storage.store(testString);
        storage.setStore(ALT_TEST_FILE_NAME);
        storage.store(testString);
        storage.close();

        originalFileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);
        altFileContent = new String(Files.readAllBytes(Paths.get(ALT_TEST_FILE_NAME)), StandardCharsets.UTF_8);

        Assert.assertEquals(originalFileContent, altFileContent);
    }

    @Test
    public void testStore() throws Exception {
        CalendarList populatedCalendar = new StorageCalendarStub(true);

        storage.store(populatedCalendar);
        storage.close();

        List<String> actualFileContent = Files.readAllLines(Paths.get(TEST_FILE_NAME));

        Assert.assertEquals(expectedFileContent, actualFileContent);

    }

    @Test
    public void testStoreEmpty() throws Exception {
        CalendarList emptyCalendar = new StorageCalendarStub(false);

        storage.store(emptyCalendar);
        storage.close();

        List<String> emptyFileContent = new ArrayList<>();
        emptyFileContent.add("0");
        emptyFileContent.add("0");

        List<String> actualFileContent = Files.readAllLines(Paths.get(TEST_FILE_NAME));

        Assert.assertEquals(emptyFileContent, actualFileContent);
    }

    @Test
    public void testLoad() throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE_NAME))) {
            for (String line : expectedFileContent) {
                writer.write(line);
                writer.newLine();
            }
        }

        CalendarList readCalendar = storage.load(new StorageCalendarStub(false));

        Assert.assertEquals(testCalendarStub, readCalendar);
    }

    private void deleteTestFiles() {
        File testFile = new File(TEST_FILE_NAME);
        File altTestFile = new File(ALT_TEST_FILE_NAME);

        if (testFile.exists()) {
            testFile.delete();
        }

        if (altTestFile.exists()) {
            testFile.delete();
        }
    }
}