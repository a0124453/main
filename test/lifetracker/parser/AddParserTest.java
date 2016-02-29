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
        assertEquals("\"task\" is added! It is due on Sunday at 1200.", parse.feedback);   
        parse = new Parser("task by 1200");
        assertEquals("\"task\" is added! It is due on today at 1200.", parse.feedback);
        
        parse = new Parser("add task by Sunday");
        assertEquals("\"task\" is added! It is due on Sunday at 2359.", parse.feedback);
        parse = new Parser("add task by 1200");
        assertEquals("\"task\" is added! It is due on today at 1200.", parse.feedback);
        
    }

}
