package lifetracker.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import lifetracker.parser.Parser;

public class AddParserTest {

    @Test
    public void addParserTest() {
        Parser parse;
        parse = new Parser("");
        assertEquals("invalid command!", parse.feedback);
        
        parse = new Parser("add task");
        assertEquals("\"task\" is added!", parse.feedback);
        
        parse = new Parser("task");
        assertEquals("\"task\" is added!", parse.feedback);

        parse = new Parser("task by Sunday 1200");
        assertEquals("\"task\" is added! It is due on Sunday 1200.", parse.feedback);
        
        
        parse = new Parser("task by 1200");
        assertEquals("\"task\" is added! It is due on 1200.", parse.feedback);
        
    }

}
