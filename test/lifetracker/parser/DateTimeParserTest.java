package lifetracker.parser;

import org.junit.Assert;
import org.junit.Test;

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

    }

    @Test
    public void parseDoubleDateTime() throws Exception {

    }
}