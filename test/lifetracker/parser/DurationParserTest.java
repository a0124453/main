package lifetracker.parser;

import lifetracker.parser.datetime.DurationParser;
import org.junit.Assert;
import org.junit.Test;

import java.time.Period;

//@@author A0091173J
public class DurationParserTest {

    DurationParser parser = DurationParser.getInstance();

    @Test
    public void testFullParse() throws Exception {

        //Partition: Number + duration expression
        String parseString = "2 week";
        Period expectedPeriod = Period.ofWeeks(2);
        Assert.assertEquals(expectedPeriod, parser.parse(parseString));

        parseString = "10 year";
        expectedPeriod = Period.ofYears(10);
        Assert.assertEquals(expectedPeriod, parser.parse(parseString));

        parseString = "5 month";
        expectedPeriod = Period.ofMonths(5);
        Assert.assertEquals(expectedPeriod, parser.parse(parseString));

        parseString = "21 day";
        expectedPeriod = Period.ofDays(21);
        Assert.assertEquals(expectedPeriod, parser.parse(parseString));

        //Boundary: Plural expressions
        parseString = "4 weeks";
        expectedPeriod = Period.ofWeeks(4);
        Assert.assertEquals(expectedPeriod, parser.parse(parseString));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumbers() throws Exception {
        //Partition: when number is not valid
        parser.parse("-2 weeks");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZero() throws Exception {
        //Boundary: when number is zero
        parser.parse("0 days");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonIntegers() throws Exception {
        //Boundary: when number is not an integer
        parser.parse("1.5 weeks");
    }

    @Test
    public void testNoNumbers() throws Exception {
        //Partition: When no numbers are provided
        String parseString = "year";
        Period expectedPeriod = Period.ofYears(1);
        Assert.assertEquals(expectedPeriod, parser.parse(parseString));

        //Boundary: Plural expressions
        parseString = "months";
        expectedPeriod = Period.ofMonths(1);
        Assert.assertEquals(expectedPeriod, parser.parse(parseString));
    }

    //Partition: Invalid inputs
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() throws Exception {
        parser.parse("abc");
    }

    //Boundary: Missing space
    @Test(expected = IllegalArgumentException.class)
    public void testNoSpace() throws Exception {
        parser.parse("2weeks");
    }

    //Boundary: Only invalid expression
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression() throws Exception {
        parser.parse("2 asd");
    }

    //Boundary: Extra words after valid command
    @Test(expected = IllegalArgumentException.class)
    public void testExtraWords() throws Exception {
        parser.parse("2 weeks abc");
    }

    //Boundary: Blank input
    @Test(expected = IllegalArgumentException.class)
    public void testBlankInput() throws Exception {
        parser.parse("");
    }

    @Test
    public void testIsValidMethod() throws Exception {

        Assert.assertTrue(parser.isDurationString("2 weeks"));
        Assert.assertTrue(parser.isDurationString("2 year"));
        Assert.assertTrue(parser.isDurationString("month"));

        Assert.assertFalse(parser.isDurationString("abc"));
        Assert.assertFalse(parser.isDurationString("2 days abc"));
        Assert.assertFalse(parser.isDurationString("-1 days"));
        Assert.assertFalse(parser.isDurationString("1.5 months"));
        Assert.assertFalse(parser.isDurationString("2years"));
        Assert.assertFalse(parser.isDurationString("0 day"));
        Assert.assertFalse(parser.isDurationString(""));
    }
}