package lifetracker.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import lifetracker.parser.Parser;

public class AddParserTest {

    @Test
    public void addParserTest() {
        String feedback = Parser.parse("");
        assertEquals("invalid command!", feedback);
        
        feedback = Parser.parse("add task");
        assertEquals("\"task\" is added!", feedback);
        
        feedback = Parser.parse("task");
        assertEquals("\"task\" is added!", feedback);
    }

}
