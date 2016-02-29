package lifetracker.parser;

public class Parser {
    public static String parse(String userInput) {
        String feedback = "";
        
        if(userInput.isEmpty()) {
            feedback = "invalid command!";
        }
        
        return feedback;

    }
}
