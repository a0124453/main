package lifetracker.parser;

import lifetracker.command.CommandObject;

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

    public ParserImpl() {
    }

    @Override
    public CommandObject parse(String userInput) {
        if (userInput.isEmpty()) {
            feedback = "invalid command!";
        } else {

            if (userInput.startsWith("add")) {
                userInput = userInput.replaceFirst("add ", "");
            }

            if (userInput.contains(" by ")) {
                String dayRegex = "[a-zA-Z]+|[a-zA-Z]+ [a-zA-Z]+";
                String dateMonthRegex = "\\d+ (?:[a-zA-Z]+&&^(?:am|AM|pm|PM))";
                String dateMonthYearRegex = "\\d+ [a-zA-Z]+ \\d+";
                String dateIntRegex = "\\d{2,}+";
                String dateMonthIntRegex = "\\d+(?:/|-)\\d+";
                String dateMonthYearIntRegex = "\\d+(?:/|-)\\d+(?:/|-)\\d{2,}+";
                String timeRegex = "\\d+(?::\\d+)?\\s?(?:am|AM|pm|PM)?";
                Pattern pattern = Pattern.compile(String.format("^(.+) by\\s?(%s|%s|%s|%s|%s|%s)?\\s?(%s)?$", 
                        dayRegex, dateMonthRegex, dateMonthYearRegex, 
                        dateIntRegex, dateMonthIntRegex, dateMonthYearIntRegex, timeRegex));
                
                Matcher matcher = pattern.matcher("task by 12/02 1200pm");
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
                Pattern pattern = Pattern
                        .compile("^(.+) from(\\s?([a-zA-Z]+))?(\\s?(\\d+))?(\\s(to))?(\\s?([a-zA-Z]+))?(\\s?(\\d+))?$");
                Matcher matcher = pattern.matcher(userInput);
                matcher.find();
                task = matcher.group(1);
                startDate = matcher.group(3);
                startTime = matcher.group(5);
                String toParam = matcher.group(7);
                endDate = matcher.group(9);
                endTime = matcher.group(11);

                if (endTime == null) {
                    endTime = "2359";
                }

                if (startTime == null) {
                    startTime = "currenttime";
                }

                if (startDate == null) {
                    startDate = "today";
                }

                if (toParam == null) {
                    int time = Integer.parseInt(startTime);
                    endTime = Integer.toString(time + 100);
                }

                if (endDate == null) {
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
        return null;
    }
}
