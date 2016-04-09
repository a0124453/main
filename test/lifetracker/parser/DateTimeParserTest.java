package lifetracker.parser;

import lifetracker.parser.datetime.DateTimeParser;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//@@author A0091173J
public class DateTimeParserTest {

    @Test
    public void isDateTime() throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();

        Assert.assertTrue(parser.isDateTime("23/4/2016 2:30pm"));
        Assert.assertTrue(parser.isDateTime("23/4/2016"));
        Assert.assertTrue(parser.isDateTime("2:30pm"));
        Assert.assertTrue(parser.isDateTime("tomorrow 2pm"));
        Assert.assertTrue(parser.isDateTime("next monday 4am"));
        Assert.assertTrue(parser.isDateTime("monday after 2/3"));

        Assert.assertFalse(parser.isDateTime("abcd"));
    }

    @Test
    public void parseSingleDateTime() throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();

        LocalDateTime expected;
        LocalDateTime actual;

        //Partition: Case where everything is specified
        actual = parser.parseSingleDateTime("today 2:30pm");
        expected = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 30));
        Assert.assertEquals(expected, actual);

        //Everything is specified but in a different format
        actual = parser.parseSingleDateTime("2/3 21:33");
        expected = LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR), 3, 2, 21, 33);
        Assert.assertEquals(expected, actual);

        //Boundary case: To make sure dates don't skip to next day when it's specified explicitly.
        actual = parser.parseSingleDateTime("2/3 00:00");
        expected = LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR), 3, 2, 0, 0);
        Assert.assertEquals(expected, actual);

        //Partition: When date is not specified
        actual = parser.parseSingleDateTime("10pm");
        expected = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));

        //Date is supposed to skip is time specified has already passed today.
        if (expected.isBefore(LocalDateTime.now())) {
            expected = expected.plusDays(1);
        }
        Assert.assertEquals(expected, actual);

        //Boundary case: date should always skip
        actual = parser.parseSingleDateTime("00:00");
        expected = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);
        Assert.assertEquals(expected, actual);

        //Partition: missing time
        actual = parser.parseSingleDateTime("day after tomorrow");
        expected = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.MIDNIGHT.minusMinutes(1));
        Assert.assertEquals(expected, actual);

        //Partition: Empty string
        actual = parser.parseSingleDateTime("");
        expected = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT.minusMinutes(1));
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void parseDoubleDateTime() throws Exception {

        DateTimeParser parser = DateTimeParser.getInstance();

        LocalDateTime expectedStart, expectedEnd;

        List<LocalDateTime> expectedDateTimeList = new ArrayList<>();
        List<LocalDateTime> actualDateTimeList;

        //Parition: When everything is specified
        actualDateTimeList = parser.parseDoubleDateTime("23/3/16 11:30am", "24/3/16 11.40pm");
        expectedStart = LocalDateTime.of(2016, 3, 23, 11, 30);
        expectedEnd = LocalDateTime.of(2016, 3, 24, 23, 40);
        expectedDateTimeList.add(expectedStart);
        expectedDateTimeList.add(expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);
    }

    @Test
    public void parseDoubleDateTimeMissingEnd() throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();

        LocalDateTime expectedStart, expectedEnd;

        List<LocalDateTime> expectedDateTimeList = new ArrayList<>();
        List<LocalDateTime> actualDateTimeList;

        //Partition: When end time is missing
        actualDateTimeList = parser.parseDoubleDateTime("23/3/16 11:30am", "24/3/16");
        expectedStart = LocalDateTime.of(2016, 3, 23, 11, 30);
        expectedEnd = LocalDateTime.of(2016, 3, 24, 11, 30);
        expectedDateTimeList.add(expectedStart);
        expectedDateTimeList.add(expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Boundary: When end date is the same
        actualDateTimeList = parser.parseDoubleDateTime("23/3/16 11:30am", "23/3/16");
        expectedEnd = LocalDateTime.of(2016, 3, 23, 12, 30);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Parition: When end date is missing
        actualDateTimeList = parser.parseDoubleDateTime("23/3/16 11:30am", "1300");
        expectedEnd = LocalDateTime.of(2016, 3, 23, 13, 0);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Boundary: When end time occurs before start time
        actualDateTimeList = parser.parseDoubleDateTime("23/3/16 11:30am", "1100");
        expectedEnd = LocalDateTime.of(2016, 3, 24, 11, 0);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Partition: When both end date and end time is missing
        actualDateTimeList = parser.parseDoubleDateTime("23/3/16 11:30am", "");
        expectedEnd = LocalDateTime.of(2016, 3, 23, 12, 30);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Boundary: When end time will spill into next day
        actualDateTimeList = parser.parseDoubleDateTime("23/3/16 11:59pm", "");
        expectedStart = LocalDateTime.of(2016, 3, 23, 23, 59);
        expectedEnd = LocalDateTime.of(2016, 3, 24, 0, 59);
        expectedDateTimeList.set(0, expectedStart);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);
    }

    @Test
    public void parseDoubleDateTimeMissingStart() throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();

        LocalDateTime expectedStart, expectedEnd;

        List<LocalDateTime> expectedDateTimeList = new ArrayList<>();
        List<LocalDateTime> actualDateTimeList;

        //Partition: When start date is missing
        actualDateTimeList = parser.parseDoubleDateTime("12am", "24/3/16 11.40pm");
        expectedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        expectedEnd = LocalDateTime.of(2016, 3, 24, 23, 40);
        expectedDateTimeList.add(expectedStart);
        expectedDateTimeList.add(expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Partition: When start time is missing
        actualDateTimeList = parser.parseDoubleDateTime("24/3/16", "24/3/16 11.40pm");
        expectedStart = LocalDateTime.of(LocalDate.of(2016, 3, 24), LocalTime.now().withNano(0));
        expectedDateTimeList.set(0, expectedStart);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Partition: Both start date and time are missing
        actualDateTimeList = parser.parseDoubleDateTime("", "24/3/16 11.40pm");
        expectedStart = LocalDateTime.now().withNano(0);
        expectedDateTimeList.set(0, expectedStart);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);
    }

    @Test
    public void parseDoubleDateTimeMissingBoth() throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();

        LocalDateTime expectedStart, expectedEnd;

        List<LocalDateTime> expectedDateTimeList = new ArrayList<>();
        List<LocalDateTime> actualDateTimeList;

        //Partition: When both dates are missing
        actualDateTimeList = parser.parseDoubleDateTime("11:58pm", "11:59pm");
        expectedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 58));
        expectedEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59));

        expectedDateTimeList.add(expectedStart);
        expectedDateTimeList.add(expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Boundary: When end time is before current time
        actualDateTimeList = parser.parseDoubleDateTime("12am", "12.01am");
        expectedStart = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0));
        expectedEnd = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 1));

        expectedDateTimeList.set(0, expectedStart);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Partition: When both are blanks
        actualDateTimeList = parser.parseDoubleDateTime("", "");
        expectedStart = LocalDateTime.now().withNano(0);
        expectedEnd = expectedStart.plusHours(1);

        expectedDateTimeList.set(0, expectedStart);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Partition: Start time and end date missing
        actualDateTimeList = parser.parseDoubleDateTime("today", "11:59pm");
        expectedStart = LocalDateTime.now().withNano(0);
        expectedEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT.minusMinutes(1));

        expectedDateTimeList.set(0, expectedStart);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Boundary: When end time is before now
        actualDateTimeList = parser.parseDoubleDateTime("today", "12am");
        expectedStart = LocalDateTime.now().withNano(0);
        expectedEnd = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);

        expectedDateTimeList.set(0, expectedStart);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Partition: Start date and end time missing
        actualDateTimeList = parser.parseDoubleDateTime("2pm", "tomorrow");
        expectedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        expectedEnd = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0));

        expectedDateTimeList.set(0, expectedStart);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);

        //Boundary: When start date defaults to same day as end date
        //Parser should advance time by 1 hr
        actualDateTimeList = parser.parseDoubleDateTime("2pm", "today");
        expectedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        expectedEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0));

        expectedDateTimeList.set(0, expectedStart);
        expectedDateTimeList.set(1, expectedEnd);

        Assert.assertEquals(expectedDateTimeList, actualDateTimeList);
    }
}