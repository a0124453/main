package lifetracker.parser;

import java.time.LocalDateTime;

import org.junit.Test;

//@@author A0114240B-unused
/*
* This class was previously used to test DateTimeParser.
* DateTimeParser was replaced by external library called Natty.
*/
public class DateTimeParserTest {

    private LocalDateTime[] dates;

    @Test
    public void dateTimeParserTest() {
        dates = new LocalDateTime[2];
        dates[0] = LocalDateTime.of(2016, 3, 11, 16, 0);
        dates[1] = LocalDateTime.of(2016, 3, 11, 17, 0);
        assertArrayEquals(dates, DateTimeParser.parse("today 4pm", "today"));
        
        
        dates[0] = LocalDateTime.of(2016, 3, 11, 16, 0);
        dates[1] = LocalDateTime.of(2016, 3, 11, 15, 0);
        assertArrayEquals(dates, DateTimeParser.parse("today 4pm", "3pm"));
        
    }

}
