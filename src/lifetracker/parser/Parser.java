package lifetracker.parser;

import lifetracker.command.CommandObject;

public interface Parser {
    CommandObject parse(String userInput);
}
