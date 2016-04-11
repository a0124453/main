package lifetracker.parser.syntax;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Predicate;

//@@author A0091173J

/**
 * @author Shen Yichen <2007.yichen@gmail.com>
 */
public class CommandSectionParser<T extends Enum<T>> {

    private static final String COMMAND_BODY_SEPARATOR = " ";

    private final Map<T, Predicate<String>> verificationMap;
    private final Map<String, T> keywordToEnumMap;
    private final T defaultField;

    public CommandSectionParser(Map<String, T> keywordToEnumMap, Map<T, Predicate<String>> verificationMap,
            T defaultField) {
        this.verificationMap = verificationMap;
        this.keywordToEnumMap = keywordToEnumMap;
        this.defaultField = defaultField;
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
    public Map<T, String> parseCommandSection(String commandBody) {

        Map<T, String> keyWordArgumentMap = new LinkedHashMap<>();

        Deque<String> processStack = commandBodyToDeque(commandBody);
        Deque<String> intermediateStack = new LinkedList<>();

        while (!processStack.isEmpty()) {
            String element = processStack.removeLast();
            T field = keywordToEnumMap.get(element);

            if (field != null && verificationMap.containsKey(field)) {

                String argument = collapseDeque(intermediateStack);
                intermediateStack.clear();

                if (verificationMap.get(field).test(argument) && !keyWordArgumentMap.containsKey(field)) {
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

}
