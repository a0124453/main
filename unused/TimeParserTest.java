package lifetracker.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.LocalTime;
import lifetracker.parser.TimeParser;

//@@author A0114240B-unused
/*
* This class was previously used to test TimeParser.
* TimeParser was replaced by external library called Natty.
*/
public class TimeParserTest {

    @Test
    public void timeParserTest() {
        assertEquals(LocalTime.of(11, 0), TimeParser.parse("1100"));
        assertEquals(LocalTime.of(11, 0), TimeParser.parse("1100am"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("1100pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("1100 pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("11:00pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("11:00 pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("11pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("11 pm"));
    }

}
