package lifetracker.parser;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

//@@author A0091173J
public class DateTimeParserTest {

    @Test
    public void isDateTime() throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();

        Assert.assertTrue(parser.isDateTime("23/4/2016 2:30pm"));
        Assert.assertTrue(parser.isDateTime("23/4/2016"));
        Assert.assertTrue(parser.isDateTime("2:30pm"));
        Assert.assertTrue(parser.isDateTime("tommorrow 2pm"));
        Assert.assertTrue(parser.isDateTime("next monday 4am"));
        Assert.assertTrue(parser.isDateTime("monday after 2/3"));

        Assert.assertFalse(parser.isDateTime("abcd"));
    }

    @Test
    public void parseSingleDateTime() throws Exception {
        DateTimeParser parser = DateTimeParser.getInstance();

        LocalDateTime expected, actual;

        actual = parser.parseSingleDateTime("today 2:30pm");
        expected = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 30));
        Assert.assertEquals(expected, actual);

        actual = parser.parseSingleDateTime("2/3 21:33");
        expected = LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR), 3, 2, 21, 33);
        Assert.assertEquals(expected, actual);

        //To make sure dates don't skip to next day when it's specified explicitly.
        actual = parser.parseSingleDateTime("2/3 00:00");
        expected = LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR), 3, 2, 0, 0);
        Assert.assertEquals(expected, actual);

        actual = parser.parseSingleDateTime("10pm");
        expected = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));

        //Date is supposed to skip is time specified has already passed today.
        if (expected.isBefore(LocalDateTime.now())) {
            expected = expected.plusDays(1);
        }
        Assert.assertEquals(expected, actual);

        //Always skip
        actual = parser.parseSingleDateTime("00:00");
        expected = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);
        Assert.assertEquals(expected, actual);

        actual = parser.parseSingleDateTime("day after tommorrow");
        expected = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.MIDNIGHT.minusMinutes(1));
        Assert.assertEquals(expected, actual);

        //Boundary: Empty string
        actual = parser.parseSingleDateTime("");
        expected = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT.minusMinutes(1));
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void parseDoubleDateTime() throws Exception {

    }
}