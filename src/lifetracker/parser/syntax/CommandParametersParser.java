package lifetracker.parser.syntax;

import java.util.Map;
//@@author A0091173J

/**
 * CommandParametersParser parses a map of parameter option/values into a {@code Parameters} object.
 *
 * @see Parameters
 */
public interface CommandParametersParser {
    String ERROR_INVALID_PARAMS = "Keywords provided are not valid together!";

    /**
     * Parses the command map into a corresponding {@code Parameters} object based on the mpa entries.
     * @param commandMap The map with options mapped to values.
     * @return A corresponding {@code Parameters} Object
     */
    Parameters parseCommandMap(Map<CommandOptions, String> commandMap);

    static void checkMutuallyExclusiveKeywords(Map<CommandOptions, String> commandMap, CommandOptions... options) {
        int count = 0;
        for (CommandOptions option : options) {
            if (commandMap.containsKey(option)) {
                count++;
            }
        }

        if (count > 1) {
            throw new IllegalArgumentException(ERROR_INVALID_PARAMS);
        }
    }

}
