package lifetracker.parser;

import lifetracker.command.AddCommand;
import lifetracker.command.CommandObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParserImpl implements Parser {

    private static final Map<String, Predicate<String>> KEYWORDS_WITH_VERIFICATIONS = new HashMap<>();

    static {
        KEYWORDS_WITH_VERIFICATIONS.put("by", DateTimeParser::isDateTime);
        KEYWORDS_WITH_VERIFICATIONS.put("from", DateTimeParser::isDateTime);
        KEYWORDS_WITH_VERIFICATIONS.put("to", DateTimeParser::isDateTime);
    }

    private static final String defaultCommand = "add";

    private static final String FULL_COMMAND_SEPARATOR = " > ";

    private final Map<String, Function<List<String>, CommandObject>> commands = new HashMap<>();

    {
        commands.put("add", this::processAdd);
        commands.put("list", this::processList);
        commands.put("delete", this::processDelete);
        commands.put("edit", this::processEdit);
    }

    private final CommandParser cmdParser;

    public ParserImpl() {
        cmdParser = new CommandParser(commands.keySet(), KEYWORDS_WITH_VERIFICATIONS, defaultCommand,
                FULL_COMMAND_SEPARATOR);
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

    private CommandObject processAdd(List<String> commandBody) {
        String addCommandBody = restoreCommandSections(commandBody);

        Map<String, String> commandBodySectionsMap = cmdParser.parseCommandBody(addCommandBody);

        return detectAddMethod(commandBodySectionsMap);
    }

    private CommandObject detectAddMethod(Map<String, String> commandBodySectionsMap) {
        if (validAddEventMap(commandBodySectionsMap)) {

            LocalDateTime[] startEndDateTime =
                    DateTimeParser.parse(commandBodySectionsMap.get("from"), commandBodySectionsMap.get("to"));

            return new AddCommand(commandBodySectionsMap.get("name"), startEndDateTime[0], startEndDateTime[1]);

        } else if (validAddDeadlineTaskMap(commandBodySectionsMap)) {

            LocalDateTime dueDate = DateTimeParser.parse(commandBodySectionsMap.get("by"));

            return new AddCommand(commandBodySectionsMap.get("name"), dueDate);

        } else if (validAddFloatingTaskMap(commandBodySectionsMap)) {

            return new AddCommand(commandBodySectionsMap.get("name"));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean validAddEventMap(Map<String, String> commandBodySectionMap) {
        return commandBodySectionMap.containsKey("from") && !commandBodySectionMap.containsKey("by");
    }

    private boolean validAddDeadlineTaskMap(Map<String, String> commandBodySectionMap) {
        return commandBodySectionMap.containsKey("by")
                && !(commandBodySectionMap.containsKey("from") || commandBodySectionMap.containsKey("to"));
    }

    private boolean validAddFloatingTaskMap(Map<String, String> commandBodySectionMap) {
        return !(commandBodySectionMap.containsKey("by")
                || commandBodySectionMap.containsKey("from")
                || commandBodySectionMap.containsKey("to"));
    }

    private CommandObject processList(List<String> commandBody) {
        return null;
    }

    private CommandObject processDelete(List<String> commandBody) {
        return null;
    }

    private CommandObject processEdit(List<String> commmandBody) {
        return null;
    }

    private String restoreCommandSections(List<String> stringList) {

        if(stringList.isEmpty()){
            return "";
        }

        StringBuilder collapsedString = new StringBuilder();

        for (String fragment : stringList) {
            collapsedString.append(FULL_COMMAND_SEPARATOR);

            collapsedString.append(fragment);
        }

        return collapsedString.substring(FULL_COMMAND_SEPARATOR.length());
    }
}
