package lifetracker.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public String command;
    public String task;
    public String endTime;
    public String endDate;
    public String feedback;

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
                    feedback = "\"" + task.trim() + "\" is added! It is due on " + endTime + ".";
                } else if (endTime == null) {
                    feedback = "\"" + task.trim() + "\" is added! It is due on " + endDate + ".";
                } else {
                    feedback = "\"" + task.trim() + "\" is added! It is due on " + endDate + " " + endTime + ".";
                }
                
            } else {
                task = userInput;
                feedback = "\"" + task.trim() + "\" is added!";

            }

        }

    }
}
