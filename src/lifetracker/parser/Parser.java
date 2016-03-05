package lifetracker.parser;

import lifetracker.command.CommandObject;

public interface Parser {
    public CommandObject parse(String userInput);
}
