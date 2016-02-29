package lifetracker.parser;

public class Parser {
    public static String parse(String userInput) {
        String feedback = "";

        if (userInput.isEmpty()) {
            feedback = "invalid command!";
        } else {
            String task = userInput;
            
            if (userInput.startsWith("add")) {
                task = userInput.replaceFirst("add", "");
            }
            
            feedback = "\"" + task.trim() + "\" is added!";

        }

        return feedback;

    }
}
