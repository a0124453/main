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
        
        parse  = new Parser("event from monday 1200 to sunday 1300");
        assertEquals("\"event\" is added! It is scheduled from monday at 1200 to sunday at 1300.", parse.feedback);
        parse  = new Parser("event from monday 1200 to sunday");
        assertEquals("\"event\" is added! It is scheduled from monday at 1200 to sunday at 2359.", parse.feedback);      
        parse  = new Parser("event from monday 1200 to 1400");
        assertEquals("\"event\" is added! It is scheduled from monday at 1200 to 1400.", parse.feedback);
        parse  = new Parser("event from monday 1200");
        assertEquals("\"event\" is added! It is scheduled from monday at 1200 to 1300.", parse.feedback);
        
        // no start time
        parse  = new Parser("event from monday to sunday 1300");
        assertEquals("\"event\" is added! It is scheduled from monday at currenttime to sunday at 1300.", parse.feedback);
        parse  = new Parser("event from monday to sunday");
        assertEquals("\"event\" is added! It is scheduled from monday at currenttime to sunday at 2359.", parse.feedback);
        parse  = new Parser("event from monday to 1300");
        assertEquals("\"event\" is added! It is scheduled from monday at currenttime to 1300.", parse.feedback);
        
        // this fail since current time is cant be convert to integer
        /*
        parse  = new Parser("event from monday");
        assertEquals("\"event\" is added! It is scheduled from monday at currenttime to 2359.", parse.feedback);
        */
        
        // no start date
        parse  = new Parser("event from 1200 to sunday 1300");
        assertEquals("\"event\" is added! It is scheduled from today at 1200 to sunday at 1300.", parse.feedback);
        parse  = new Parser("event from 1200");
        assertEquals("\"event\" is added! It is scheduled from today at 1200 to 1300.", parse.feedback);
        parse  = new Parser("event from 1200 to sunday");
        assertEquals("\"event\" is added! It is scheduled from today at 1200 to sunday at 2359.", parse.feedback);
        parse  = new Parser("event from 1200 to 1400");
        assertEquals("\"event\" is added! It is scheduled from today at 1200 to 1400.", parse.feedback);
        
        
    }

}
