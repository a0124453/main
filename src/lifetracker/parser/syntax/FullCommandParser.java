package lifetracker.parser.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

//@@author A0091173J

/**
 * This class deals with separating commands into distinct components.
 * <p>
 * For example, this class can separate a complete command into key words and command bodies. It can the further
 * separate the command body into sections defined by keywords, such as "from".
 *
 */
public class FullCommandParser {

    private final Set<String> commands;
    private final String defaultCommand;

    public FullCommandParser(Set<String> commands, String defaultCommand) {
        this.commands = commands;
        this.defaultCommand = defaultCommand;
    }

    public List<String> parseFullCommand(String fullCommand, String fullCommandSeparator) {
        fullCommandSeparator = Pattern.quote(fullCommandSeparator);

        String[] components = fullCommand.split(fullCommandSeparator);

        components[0] = augmentDefaultToFullCommand(components[0]);

        String command = getFirstWord(components[0]);

        //If command was split without space
        if (components[0].equals(command)) {
            components[0] = "";
        } else {
            components[0] = components[0].replaceFirst(command + " ", "");
        }

        List<String> parsedComponents = new ArrayList<>();

        parsedComponents.add(command);

        for (String component : components) {
            if (!component.trim().isEmpty()) {
                parsedComponents.add(component);
            }
        }

        return parsedComponents;

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
