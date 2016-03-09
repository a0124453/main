package lifetracker.parser;

import lifetracker.command.CommandObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParserImpl implements Parser {

    private static final Map<String, Predicate<String>> keyWordWithVerifications = new HashMap<>();

    static {
        keyWordWithVerifications.put("by", DateTimeParser::isDateTime);
        keyWordWithVerifications.put("from", DateTimeParser::isDateTime);
        keyWordWithVerifications.put("to", DateTimeParser::isDateTime);
    }

    private static final String fullCommandSeparator = " > ";

    private final Map<String, Function<List<String>, CommandObject>> commands = new HashMap<>();

    {
        commands.put("add", null);
        commands.put("list", null);
        commands.put("delete", null);
        commands.put("edit", null);
    }

    private final String defaultCommand = "add";

    private final CommandParser cmdParser;

    public ParserImpl() {
        cmdParser = new CommandParser(commands.keySet(), keyWordWithVerifications, defaultCommand,
                fullCommandSeparator);
    }

    @Override
    public CommandObject parse(String userInput) {
        List<String> commandSegments = cmdParser.parseFullCommand(userInput);

        String command = commandSegments.get(0);
        commandSegments.remove(0);

        return processCommand(command, commandSegments);
    }

    private CommandObject processCommand(String command, List<String> commandBody) {
        return commands.get(command).apply(commandBody);
    }

}
