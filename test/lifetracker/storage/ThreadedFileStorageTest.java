package lifetracker.storage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ThreadedFileStorageTest {

    public static final String TEST_FILE_NAME = "test.dat";
    public static final String ALT_TEST_FILE_NAME = "alt_test.dat";
    private static final String LOG_FOLDER = "logs/";
    private static final String LOG_FILE = "lifetracker_test.log";

    public static final String jsonTestData = "{\\n  \"taskList\": {\\n    \"1\": {\\n      \"name\": \"floating\",\\n      \"entryType\": \"FLOATING\",\\n      \"id\": 1\\n    },\\n    \"2\": {\\n      \"name\": \"task\",\\n      \"endDateTime\": {\\n        \"date\": {\\n          \"year\": 999999999,\\n          \"month\": 12,\\n          \"day\": 31\\n        },\\n        \"time\": {\\n          \"hour\": 23,\\n          \"minute\": 59,\\n          \"second\": 59,\\n          \"nano\": 999999999\\n        }\\n      },\\n      \"entryType\": \"DEADLINE\",\\n      \"id\": 2\\n    },\\n    \"3\": {\\n      \"name\": \"recurring task\",\\n      \"endDateTime\": {\\n        \"date\": {\\n          \"year\": 2016,\\n          \"month\": 3,\\n          \"day\": 27\\n        },\\n        \"time\": {\\n          \"hour\": 11,\\n          \"minute\": 29,\\n          \"second\": 43,\\n          \"nano\": 322000000\\n        }\\n      },\\n      \"entryType\": \"DEADLINE\",\\n      \"id\": 3\\n    }\\n  },\\n  \"eventList\": {\\n    \"4\": {\\n      \"name\": \"event\",\\n      \"startDateTime\": {\\n        \"date\": {\\n          \"year\": -999999999,\\n          \"month\": 1,\\n          \"day\": 1\\n        },\\n        \"time\": {\\n          \"hour\": 0,\\n          \"minute\": 0,\\n          \"second\": 0,\\n          \"nano\": 0\\n        }\\n      },\\n      \"endDateTime\": {\\n        \"date\": {\\n          \"year\": 999999999,\\n          \"month\": 12,\\n          \"day\": 31\\n        },\\n        \"time\": {\\n          \"hour\": 23,\\n          \"minute\": 59,\\n          \"second\": 59,\\n          \"nano\": 999999999\\n        }\\n      },\\n      \"entryType\": \"EVENT\",\\n      \"id\": 4\\n    },\\n    \"5\": {\\n      \"name\": \"recurring event\",\\n      \"startDateTime\": {\\n        \"date\": {\\n          \"year\": -999999999,\\n          \"month\": 1,\\n          \"day\": 1\\n        },\\n        \"time\": {\\n          \"hour\": 0,\\n          \"minute\": 0,\\n          \"second\": 0,\\n          \"nano\": 0\\n        }\\n      },\\n      \"endDateTime\": {\\n        \"date\": {\\n          \"year\": 999999999,\\n          \"month\": 12,\\n          \"day\": 31\\n        },\\n        \"time\": {\\n          \"hour\": 23,\\n          \"minute\": 59,\\n          \"second\": 59,\\n          \"nano\": 999999999\\n        }\\n      },\\n      \"entryType\": \"EVENT\",\\n      \"id\": 5\\n    }\\n  }\\n}";

    public ThreadedFileStorage storage;

    @Before
    public void setUp() throws Exception {
        File logDir = new File(LOG_FOLDER);

        if (!logDir.exists()) {
            logDir.mkdir();
        }

        LogManager.getLogManager().reset();

        Logger globalLogger = Logger.getGlobal();
        globalLogger.addHandler(new FileHandler(LOG_FOLDER + LOG_FILE));

        storage = new ThreadedFileStorage(TEST_FILE_NAME);
        deleteTestFiles();
    }

    @After
    public void tearDown() throws Exception {
        storage.close();
        deleteTestFiles();
    }

    @Test
    public void testSetStore() throws Exception {
        //Partition: Changing the save file
        String testString = "Line 1\nLine 2";

        storage.store(testString);

        storage.setStoreAndStart(ALT_TEST_FILE_NAME);

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
        storage.setStoreAndStart(ALT_TEST_FILE_NAME);
        storage.store(testString);
        storage.close();

        originalFileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);
        altFileContent = new String(Files.readAllBytes(Paths.get(ALT_TEST_FILE_NAME)), StandardCharsets.UTF_8);

        Assert.assertEquals(originalFileContent, altFileContent);
    }

    @Test
    public void testStore() throws Exception {
        //Partition: Storing a string
        String testString = "Test string";
        storage.store(testString);
        storage.close();

        String actualFileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);

        Assert.assertEquals(testString, actualFileContent);

        //Boundary: Line breaks and empty lines
        storage = new ThreadedFileStorage(TEST_FILE_NAME);
        testString = "Line 1\n\nLine3";

        storage.store(testString);
        storage.close();

        actualFileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);

        Assert.assertEquals(testString, actualFileContent);

        //Boundary: Empty String
        storage = new ThreadedFileStorage(TEST_FILE_NAME);
        testString = "";

        storage.store(testString);
        storage.close();

        actualFileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);

        Assert.assertEquals(testString, actualFileContent);

        //Full JSON format
        storage = new ThreadedFileStorage(TEST_FILE_NAME);

        storage.store(jsonTestData);
        storage.close();

        actualFileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_NAME)), StandardCharsets.UTF_8);

        Assert.assertEquals(jsonTestData, actualFileContent);
    }

    @Test(expected = AssertionError.class)
    public void testNull() throws Exception {
        storage.store(null);
    }

    @Test
    public void testLoad() throws Exception {
        //Partition: Load a string
        String testString = "Some string";
        writeToTestFile(testString, TEST_FILE_NAME);
        Assert.assertEquals(testString, storage.load());

        //Boundary: Line breaks
        testString = "Line 1\nLine 2";
        writeToTestFile(testString, TEST_FILE_NAME);
        Assert.assertEquals(testString, storage.load());

        //Boundary: Empty string
        testString = "";
        writeToTestFile(testString, TEST_FILE_NAME);
        Assert.assertEquals(testString, storage.load());

        //Full json format
        writeToTestFile(jsonTestData, TEST_FILE_NAME);

        Assert.assertEquals(jsonTestData, storage.load());
    }

    private void writeToTestFile(String content, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
            writer.flush();
        }
    }

    private void deleteTestFiles() {
        File testFile = new File(TEST_FILE_NAME);
        File altTestFile = new File(ALT_TEST_FILE_NAME);

        if (testFile.exists()) {
            testFile.delete();
        }

        if (altTestFile.exists()) {
            altTestFile.delete();
        }
    }
}