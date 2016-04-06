package lifetracker.parser;

import java.util.Map;

public interface CommandParametersParser {
    String ERROR_INVALID_PARAMS = "Keywords provided are not valid together!";

    Parameters parseCommandMap(Map<String, String> commandMap);

    static void checkNotAllPresent(Map<String, String> commandMap, String... keywords) {
        boolean allPresent = true;
        for (String word : keywords) {
            allPresent &= commandMap.containsKey(word);
        }

        if (allPresent) {
            throw new IllegalArgumentException(ERROR_INVALID_PARAMS);
        }
    }

}
