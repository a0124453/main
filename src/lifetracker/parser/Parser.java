package lifetracker.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public String command;
    public String task;
    public String endTime;
    public String endDate;
    public String feedback;
    public String startTime;
    public String startDate;

    public Parser(String userInput) {

        if (userInput.isEmpty()) {
            feedback = "invalid command!";
        } else {

            if (userInput.startsWith("add")) {
                userInput = userInput.replaceFirst("add ", "");
            }

            if (userInput.contains(" by ")) {
                Pattern pattern = Pattern.compile("^(.+) by(\\s?([a-zA-Z]+))?(\\s?(\\d+))?$");
                Matcher matcher = pattern.matcher(userInput);
                matcher.find();
                task = matcher.group(1);
                endDate = matcher.group(3);
                endTime = matcher.group(5);

                if (endDate == null) {
                    endDate = "today";
                }

                if (endTime == null) {
                    endTime = "2359";
                }

                feedback = "\"" + task.trim() + "\" is added! It is due on " + endDate + " at " + endTime + ".";

            } else if (userInput.contains(" from ")) {
                Pattern pattern = Pattern.compile("^(.+) from(\\s?([a-zA-Z]+))?(\\s?(\\d+))? to(\\s?([a-zA-Z]+))?(\\s?(\\d+))?$");
                Matcher matcher = pattern.matcher(userInput);
                matcher.find();
                task = matcher.group(1);
                startDate = matcher.group(3);
                startTime = matcher.group(5);
                endDate = matcher.group(7);
                endTime = matcher.group(9);
                
                feedback = String.format("\"%s\" is added! It is scheduled from %s at %s to %s at %s.",
                        task, startDate, startTime, endDate, endTime);
                
                
            } else {
                task = userInput;
                feedback = "\"" + task.trim() + "\" is added!";

            }

        }

    }
}
