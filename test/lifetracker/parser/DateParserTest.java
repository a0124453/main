package lifetracker.parser;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class DateParserTest {

    @Test
    public void test() {
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("07/03/16"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("7/03/16"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("07/3/16"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("07/03/2016"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("07-03-16"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("7 mar 16"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("7 march 16"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("7 march 2016"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("7 mar"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("today"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("Today"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("Monday"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("mon"));
        assertEquals(LocalDate.of(2016, 3, 7), DateParser.parseDate("m"));
        
    }

}
