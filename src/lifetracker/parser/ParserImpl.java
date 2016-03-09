package lifetracker.parser;

import lifetracker.command.CommandObject;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserImpl implements Parser {
    public String command;
    public String task;
    public String endTime;
    public String endDate;
    public String feedback;
    public String startTime;
    public String startDate;
    
    private static final String DATE_PATTERN0 = "[a-zA-Z]+|[a-zA-Z]+ [a-zA-Z]+";
    private static final String DATE_PATTERN1 = "\\d+ (?:[a-zA-Z]+&&^(?:am|AM|pm|PM))";
    private static final String DATE_PATTERN2 = "\\d+ [a-zA-Z]+ \\d+";
    private static final String DATE_PATTERN3 = "\\d{2,}+";
    private static final String DATE_PATTERN4 = "\\d+(?:/|-)\\d+";
    private static final String DATE_PATTERN5 = "\\d+(?:/|-)\\d+(?:/|-)\\d{2,}+";
    private static final String TIME_PATTERN = "\\d+(?::\\d+)?\\s?(?:am|AM|pm|PM)?";
    private static final String DATE_TIME_PATTERN = String.format("^(%s|%s|%s|%s|%s|%s)?\\s?(%s)?$", 
            DATE_PATTERN0, DATE_PATTERN1, DATE_PATTERN2, 
            DATE_PATTERN3, DATE_PATTERN4, DATE_PATTERN5, TIME_PATTERN);

    public ParserImpl() {
    }

    @Override
    public CommandObject parse(String userInput) {
        if (userInput.isEmpty()) {
            feedback = "invalid command!";
        } else {
            
            String command = getCommand(userInput);
            
            switch (command) {
                case "add":
                    userInput = userInput.replaceFirst("add ", "");
                    processAddCommand(userInput);
                    break;
                default:
                    processAddCommand(userInput);
                    break;
            }

        }
        return null;
    }
    
    private String getCommand(String userInput) {
        if (userInput.indexOf(' ') > -1) { // Check if there is more than one word.
          return userInput.substring(0, userInput.indexOf(' ')); // Extract first word.
        } else {
          return userInput; // Text is the first word itself.
        }
      }

    private void processAddCommand(String userInput) {
        if (userInput.contains(" by ")) {
            
            Pattern pattern = Pattern.compile(String.format("^(.+) by\\s?(%s|%s|%s|%s|%s|%s)?\\s?(%s)?$", 
                    DATE_PATTERN0, DATE_PATTERN1, DATE_PATTERN2, 
                    DATE_PATTERN3, DATE_PATTERN4, DATE_PATTERN5, TIME_PATTERN));
            
            Matcher matcher = pattern.matcher(userInput);
            matcher.find();
            task = matcher.group(1);
            endDate = matcher.group(2);
            endTime = matcher.group(3);

            if (endDate == null) {
                endDate = "today";
            }

            if (endTime == null) {
                endTime = "2359";
            }

            feedback = "\"" + task.trim() + "\" is added! It is due on " + endDate + " at " + endTime + ".";

        } else if (userInput.contains(" from ")) {
            int beginIndexOfForm = userInput.lastIndexOf("from");
            
            task = userInput.substring(0, beginIndexOfForm - 1);
            String inputParam = userInput.substring(beginIndexOfForm);
            inputParam = inputParam.replace("from ", "");
            
            
            if (inputParam.contains(" to ")) {
                String[] inputParams = inputParam.split(" to ");
                String startParam = inputParams[0];
                String endParam = inputParams[1];
                
                Pattern pattern = Pattern.compile(DATE_TIME_PATTERN);
                
                Matcher matcher = pattern.matcher(startParam);
                matcher.find();
                startDate = matcher.group(1);
                startTime = matcher.group(2);
                
                
                matcher = pattern.matcher(endParam);
                matcher.find();
                endDate = matcher.group(1);
                endTime = matcher.group(2);
                
            } else {
                Pattern pattern = Pattern.compile(DATE_TIME_PATTERN);
                
                Matcher matcher = pattern.matcher(inputParam);
                matcher.find();
                startDate = matcher.group(1);
                startTime = matcher.group(2);
                
                int time = Integer.parseInt(startTime);
                endTime = Integer.toString(time + 100);
                
            }

            if (endTime == null) {
                endTime = "2359";
            }

            if (startTime == null) {
                String hour = Integer.toString(LocalTime.now().getHour());
                String minute = Integer.toBinaryString(LocalTime.now().getMinute());
                startTime = new String(hour + minute);
            }

            if (startDate == null) {
                startDate = "today";
            }

            if (endDate == null) {
                endDate = startDate;
                feedback = String.format("\"%s\" is added! It is scheduled from %s at %s to %s.", task,
                        startDate, startTime, endTime);
            } else {
                feedback = String.format("\"%s\" is added! It is scheduled from %s at %s to %s at %s.", task,
                        startDate, startTime, endDate, endTime);
            }

        } else {
            task = userInput;
            feedback = "\"" + task.trim() + "\" is added!";
        }
        
    }
}
