package lifetracker.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * This class deals with separating commands into distinct components.
 * <p>
 * For example, this class can separate a complete command into key words and command bodies. It can the further
 * separate the command body into sections defined by keywords, such as "from".
 */
public class CommandParser {

    public static final String COMMAND_BODY_NAME_FIELD_KEY = "name";

    private static final String COMMAND_BODY_SEPARATOR = " ";

    private final Set<String> commands;
    /**
     * A map of keywords, together with their respective format verification predicates.
     */
    private final Map<String, Predicate<String>> keywordsWithVerifications;
    private final String defaultCommand;
    private final String fullCommandSeparator;

    public CommandParser(Set<String> commands,
            Map<String, Predicate<String>> keywordsWithVerifications,
            String defaultCommand,
            String fullCommandSeparator) {

        this.commands = commands;
        this.keywordsWithVerifications = keywordsWithVerifications;
        this.defaultCommand = defaultCommand;
        this.fullCommandSeparator = fullCommandSeparator;
    }

    public List<String> parseFullCommand(String fullCommand) {

        fullCommand = augmentDefaultToFullCommand(fullCommand);

        String command = getFirstWord(fullCommand);
        String body = fullCommand.replaceFirst(command, "").trim();

        List<String> parsedComponents = new ArrayList<>();

        parsedComponents.add(command);

        parsedComponents.addAll(Arrays.asList(body.split(fullCommandSeparator)));

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
     * @param commandBody The command body to parse
     * @return A map with the components parsed
     */
    public Map<String, String> parseCommandBody(String commandBody) {

        Map<String, String> keyWordArgumentMap = new LinkedHashMap<>();

        Stack<String> processStack = commandBodyToReverseStack(commandBody);
        Stack<String> intermediateStack = new Stack<>();

        while (!processStack.isEmpty()) {
            String element = processStack.pop();

            if (keywordsWithVerifications.containsKey(element)) {
                String argument = collapseList(intermediateStack);
                intermediateStack.clear();

                if (keywordsWithVerifications.get(element).test(argument) && !keyWordArgumentMap.containsKey(element)) {
                    keyWordArgumentMap.put(element, argument);
                } else {
                    processStack.push(element);
                    processStack.push(argument);

                    keyWordArgumentMap.put(COMMAND_BODY_NAME_FIELD_KEY, collapseList(processStack));
                    processStack.clear();
                }
            } else {
                intermediateStack.push(element);
            }
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
    private static Stack<String> commandBodyToReverseStack(String commandBody) {
        String[] splitCommand = commandBody.split(COMMAND_BODY_SEPARATOR);

        Stack<String> processStack = new Stack<>();

        for (String elem : splitCommand) {
            processStack.push(elem);
        }

        return processStack;
    }

    private static String collapseList(List<String> list) {
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
