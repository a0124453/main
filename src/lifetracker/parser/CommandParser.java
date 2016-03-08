package lifetracker.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * This class deals with separating commands into distinct components.
 * <p>
 * For example, this class can separate a complete command into key words and command bodies. It can the further
 * separate the command body into sections defined by keywords, such as "from".
 */
public class CommandParser {

    private final Set<String> commands;

    /**
     * A map of keywords, together with their respective format verification predicates.
     */
    private final Map<String, Predicate<String>> keywordsWithVerifications;
    private final String defaultCommand;
    private final String bodySeperator;

    public CommandParser(Set<String> commands,
            Map<String, Predicate<String>> keywordsWithVerifications,
            String defaultCommand,
            String bodySeperator) {

        this.commands = commands;
        this.keywordsWithVerifications = keywordsWithVerifications;
        this.defaultCommand = defaultCommand;
        this.bodySeperator = bodySeperator;
    }

    public List<String> parseFullCommand(String fullCommand) {

        fullCommand = augmentDefaultToFullCommand(fullCommand);

        String command = getFirstWord(fullCommand);
        String body = fullCommand.replaceFirst(command, "").trim();

        List<String> parsedComponents = new ArrayList<>();

        parsedComponents.add(command);

        parsedComponents.addAll(Arrays.asList(body.split(bodySeperator)));

        return parsedComponents;

    }

    public Map<String, String> parseCommandBody(String commandBody, List<String> keywords) {
        return null;
    }

    private String augmentDefaultToFullCommand(String fullCommand) {
        String command = getFirstWord(fullCommand);

        if (commands.contains(command)) {
            return fullCommand;
        } else {
            return defaultCommand + " " + fullCommand;
        }

    }

    private static String getFirstWord(String command) {

        int pos = command.indexOf(' ');

        if (pos == -1) {
            return command;
        } else {
            return command.substring(0, pos);
        }

    }

}
