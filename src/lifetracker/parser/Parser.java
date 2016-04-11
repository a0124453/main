package lifetracker.parser;

import lifetracker.command.CommandObject;

//@@author A0091173J

/**
 * Parser takes in a user input string which represents a command affecting the calendar, and returns a CommandObject
 * that can be executed to change the calendar.
 */
public interface Parser {
    /**
     * Parses the user input into a corresponding CommandObject.
     *
     * @param userInput The user entered command.
     * @return The CommandObject
     */
    CommandObject parse(String userInput);
}
