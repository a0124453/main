package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lifetracker.command.CommandObject;
import lifetracker.parser.Parser;;

public class LogicParserStub implements Parser {
    
    @Override
    public CommandObject parse(String userInput) {
        
        int position = userInput.indexOf(" ");
        String item = userInput.substring(position + 1);
        
        if(userInput.contains("by")) {
            String[] content = item.split(" by ");
            String name = content[0];
            LocalDateTime deadline = LocalDateTime.parse(content[1],
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return new LogicCommandStub(name, deadline);
        }
        
        else if(userInput.contains("from")) {
            String[] content = item.split(" from ");
            String name = content[0];
            
            String[] startEnd = content[1].split(" to ");
            LocalDateTime start = LocalDateTime.parse(startEnd[0],
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime end = LocalDateTime.parse(startEnd[1],
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            return new LogicCommandStub(name, start, end);
        }
        
        else {
            String name = item;
            return new LogicCommandStub(name);
        }
    }
}
