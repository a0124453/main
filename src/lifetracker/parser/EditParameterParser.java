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
            resolveAndProcessRecurringCommandType(commandMap, result);
        } else if (isTaskMap(commandMap)) {
            populateTaskParameters(commandMap, result);
            resolveAndProcessRecurringCommandType(commandMap, result);
        } else {
            result.commandClass = CommandClass.GENERIC;
            if (commandMap.containsKey(REMOVE_DATE_MARKER)) {
                result.isForcedOverwrite = true;
            } else {
                resolveAndProcessRecurringCommandType(commandMap, result);
            }
        }
    }

    @Override
    boolean isRecurringMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_STOP_MARKER, RECURRING_PERIOD_FIELD);
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_LIMIT_OCCURRENCES, RECURRING_LIMIT_DATE,
                RECURRING_UNLIMIT_MARKER);
        checkMutuallyExclusiveKeywords(commandMap, REMOVE_DATE_MARKER, RECURRING_PERIOD_FIELD);

        return super.isRecurringMap(commandMap)
                || commandMap.containsKey(RECURRING_UNLIMIT_MARKER)
                || commandMap.containsKey(RECURRING_LIMIT_OCCURRENCES)
                || commandMap.containsKey(RECURRING_LIMIT_DATE)
                || commandMap.containsKey(RECURRING_STOP_MARKER);
    }

    @Override
    boolean isEventMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, TASK_DEADLINE_FIELD, REMOVE_DATE_MARKER);
        return super.isEventMap(commandMap);
    }

    @Override
    boolean isTaskMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, EVENT_START_FIELD, REMOVE_DATE_MARKER);
        checkMutuallyExclusiveKeywords(commandMap, EVENT_END_FIELD, REMOVE_DATE_MARKER);
        return super.isTaskMap(commandMap);
    }

    @Override
    void populateRecurringParameters(Map<String, String> commandMap, Parameters result) {
        if (commandMap.containsKey(RECURRING_LIMIT_DATE)) {
            result.dateLimit = dateTimeParser.parseDateTimeAsIs(commandMap.get(RECURRING_LIMIT_DATE)).toLocalDate();

            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT_DATE,
                    CommandClass.RECURRING_TASK_DATE, CommandClass.RECURRING_DATE);
        } else if (commandMap.containsKey(RECURRING_LIMIT_OCCURRENCES)) {
            result.occurLimit = Integer.parseInt(commandMap.get(RECURRING_LIMIT_OCCURRENCES));

            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT_OCCURRENCES,
                    CommandClass.RECURRING_TASK_OCCURRENCES, CommandClass.RECURRING_OCCURRENCES);
        } else {
            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT, CommandClass.RECURRING_TASK,
                    CommandClass.RECURRING);
        }
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

    boolean isRemoveDateMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, TASK_DEADLINE_FIELD, REMOVE_DATE_MARKER);
        checkMutuallyExclusiveKeywords(commandMap, EVENT_END_FIELD, REMOVE_DATE_MARKER);
        checkMutuallyExclusiveKeywords(commandMap, EVENT_START_FIELD, REMOVE_DATE_MARKER);
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_PERIOD_FIELD, REMOVE_DATE_MARKER);
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_LIMIT_OCCURRENCES, REMOVE_DATE_MARKER);
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_LIMIT_DATE, REMOVE_DATE_MARKER);

        return commandMap.containsKey(REMOVE_DATE_MARKER);
    }

    private void resolveAndProcessRecurringCommandType(Map<String, String> commandMap, Parameters result) {
        if (isRecurringMap(commandMap)) {
            populateRecurringParameters(commandMap, result);
        } else if (isUnlimitMap(commandMap)) {
            populateRecurringParameters(commandMap, result);
            result.isForcedOverwrite = true;
        } else if (isStopMap(commandMap)) {
            result.isForcedOverwrite = true;
        }
    }

    private void assignRecurringClass(Map<String, String> commandMap, Parameters result, CommandClass eventEnum,
            CommandClass taskEnum, CommandClass genericEnum) {
        if (isEventMap(commandMap)) {
            result.commandClass = eventEnum;
        } else if (isTaskMap(commandMap)) {
            result.commandClass = taskEnum;
        } else {
            result.commandClass = genericEnum;
        }
    }
}
