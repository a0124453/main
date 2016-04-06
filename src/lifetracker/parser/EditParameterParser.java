package lifetracker.parser;

import java.util.Map;

import static lifetracker.parser.CommandParametersParser.checkMutuallyExclusiveKeywords;

//@@author A0091173J
public class EditParameterParser extends AddParameterParser {
    private static EditParameterParser ourInstance = new EditParameterParser();

    public static EditParameterParser getInstance() {
        return ourInstance;
    }

    protected static final String RECURRING_STOP_MARKER = "stop";
    protected static final String RECURRING_UNLIMIT_MARKER = "forever";
    protected static final String REMOVE_DATE_MARKER = "nodue";

    @Override
    public Parameters parseCommandMap(Map<String, String> commandMap) {
        Parameters result = new Parameters();

        result.name = commandMap.get(NAME_FIELD);

        determineTypeAndPopulateFields(commandMap, result);

        return result;
    }

    void determineTypeAndPopulateFields(Map<String, String> commandMap, Parameters result) {
        if (isEventMap(commandMap)) {
            populateEventParameters(commandMap, result);

            if (isRecurringMap(commandMap)) {
                populateRecurringParameters(commandMap, result);
            } else if (isUnlimitMap(commandMap)) {
                populateEventParameters(commandMap, result);
                result.isForcedOverwrite = true;
            } else if (isStopMap(commandMap)) {
                result.isForcedOverwrite = true;
            }
        }
    }

    @Override
    boolean isRecurringMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_STOP_MARKER, RECURRING_PERIOD_FIELD);
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_LIMIT_OCCURRENCES, RECURRING_LIMIT_DATE,
                RECURRING_UNLIMIT_MARKER);

        return super.isRecurringMap(commandMap);
    }

    boolean isStopMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_STOP_MARKER, RECURRING_PERIOD_FIELD);
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_LIMIT_OCCURRENCES, RECURRING_LIMIT_DATE,
                RECURRING_UNLIMIT_MARKER);

        return commandMap.containsKey(RECURRING_STOP_MARKER);
    }

    boolean isUnlimitMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_LIMIT_OCCURRENCES, RECURRING_LIMIT_DATE,
                RECURRING_UNLIMIT_MARKER);

        return commandMap.containsKey(RECURRING_UNLIMIT_MARKER);
    }

}
