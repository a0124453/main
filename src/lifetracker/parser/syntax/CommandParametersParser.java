package lifetracker.parser.syntax;

import java.util.Map;

public interface CommandParametersParser {
    String ERROR_INVALID_PARAMS = "Keywords provided are not valid together!";

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
