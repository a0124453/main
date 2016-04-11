package lifetracker.parser.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

//@@author A0091173J

/**
 * This class deals with separating commands into distinct components, specified by a separator.
 */
public class FullCommandParser {

    private final Set<String> commands;
    private final String defaultCommand;

    /**
     * Creates a new {@code FullCommandParser} with a set of commands to identify. The defaultCommand provided is
     * assumed if no match is found in the set.
     *
     * @param commands       The Set of commands
     * @param defaultCommand The default command
     */
    public FullCommandParser(Set<String> commands, String defaultCommand) {
        this.commands = commands;
        this.defaultCommand = defaultCommand;
    }

    /**
     * Parses a full command.
     * <p>
     * This method determines and removes the command term from the command, then splits the remain into sections based
     * on the separator provided.
     * <p>
     * If the command cannot be determined, the default command is assumed instead.
     *
     * @param fullCommand          The full command String
     * @param fullCommandSeparator The separator for the command sections.
     * @return The list of sections, with the identified command term as the first element.
     */
    public List<String> parseFullCommand(String fullCommand, String fullCommandSeparator) {
        String newFullCommandSeparator = fullCommandSeparator;
        newFullCommandSeparator = Pattern.quote(newFullCommandSeparator);

        String[] components = fullCommand.split(newFullCommandSeparator);

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
