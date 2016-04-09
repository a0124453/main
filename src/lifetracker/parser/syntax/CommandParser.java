package lifetracker.parser.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

//@@author A0091173J

/**
 * This class deals with separating commands into distinct components.
 * <p>
 * For example, this class can separate a complete command into key words and command bodies. It can the further
 * separate the command body into sections defined by keywords, such as "from".
 *
 */
public class CommandParser {

    public static final String COMMAND_BODY_NAME_FIELD_KEY = "name";

    private static final String COMMAND_BODY_SEPARATOR = " ";

    private final Set<String> commands;
    private final String defaultCommand;

    public CommandParser(Set<String> commands, String defaultCommand) {
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

    /**
     * Parses the command body using the keywords supplied to this object.
     * <p>
     * This method parses the command from the back.
     * <p>
     * Arguments after a keyword is checked with the keyword predicate specified for validity. An invalid argument,
     * repeated keyword, or end list will trigger the method to dump the arguments, together with the rest of the
     * command into a entry accessible with key "name".
     * <p>
     * The method returns a map that can be iterated in parse order (i.e. components iterated from the back of the
     * command). The keys are the keywords and the values are the arguments.
     *
     * @param commandBody               The command body to parse
     * @param keywordsWithVerifications The keyword map to verify options.
     * @return A map with the components parsed
     */

    public <T>Map<T, String> parseCommandBody(String commandBody,
            Map<String, Predicate<String>> keywordsWithVerifications, Map<String, T> keywordToEnumMap, T defaultField) {

        Map<T, String> keyWordArgumentMap = new LinkedHashMap<>();

        Deque<String> processStack = commandBodyToDeque(commandBody);
        Deque<String> intermediateStack = new LinkedList<>();

        while (!processStack.isEmpty()) {
            String element = processStack.removeLast();


            if (keywordToEnumMap.containsKey(element)) {
                T field = keywordToEnumMap.get(element);

                String argument = collapseDeque(intermediateStack);
                intermediateStack.clear();

                if (keywordsWithVerifications.get(element).test(argument) && !keyWordArgumentMap.containsKey(field)) {
                    keyWordArgumentMap.put(field, argument);
                } else {
                    processStack.add(element);
                    processStack.add(argument);

                    keyWordArgumentMap.put(defaultField, collapseDeque(processStack));
                    processStack.clear();
                }
            } else {
                intermediateStack.push(element);
            }
        }

        if (!keyWordArgumentMap.containsKey(defaultField)) {
            keyWordArgumentMap.put(defaultField, collapseDeque(intermediateStack));
        }

        return keyWordArgumentMap;
    }

    private String augmentDefaultToFullCommand(String fullCommand) {
        String command = getFirstWord(fullCommand);

        if (commands.contains(command)) {
            return fullCommand;
        } else {
            return defaultCommand + " " + fullCommand;
        }

    }

    /**
     * Creates a stack from the command body, split into elements and pops from the back.
     *
     * @param commandBody The command body
     * @return The reverse stack
     */
    private static Deque<String> commandBodyToDeque(String commandBody) {
        String[] splitCommand = commandBody.split(COMMAND_BODY_SEPARATOR);

        Deque<String> processStack = new LinkedList<>();

        Collections.addAll(processStack, splitCommand);

        return processStack;
    }

    private static String collapseDeque(Deque<String> list) {
        StringBuilder collateString = new StringBuilder();

        for (String elem : list) {
            collateString.append(elem);
            collateString.append(" ");
        }

        return collateString.toString().trim();
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
