package lifetracker.parser.syntax;

import java.util.Map;

public interface CommandParametersParser {
    String ERROR_INVALID_PARAMS = "Keywords provided are not valid together!";

    Parameters parseCommandMap(Map<String, String> commandMap);

    static void checkMutuallyExclusiveKeywords(Map<String, String> commandMap, String... keywords) {
        int count = 0;
        for (String word : keywords) {
            if (commandMap.containsKey(word)) {
                count++;
            }
        }

        if (count > 1) {
            throw new IllegalArgumentException(ERROR_INVALID_PARAMS);
        }
    }

}
