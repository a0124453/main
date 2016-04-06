package lifetracker.parser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static lifetracker.parser.CommandParametersParser.checkMutuallyExclusiveKeywords;

//@@author A0091173J
public class AddParameterParser implements CommandParametersParser {
    private static AddParameterParser ourInstance = new AddParameterParser();

    public static AddParameterParser getInstance() {
        return ourInstance;
    }

    //TODO Set a enum
    protected static final String NAME_FIELD = "name";
    protected static final String EVENT_START_FIELD = "from";
    protected static final String EVENT_END_FIELD = "to";
    protected static final String TASK_DEADLINE_FIELD = "by";
    protected static final String RECURRING_PERIOD_FIELD = "every";
    protected static final String RECURRING_LIMIT_OCCURRENCES = "for";
    protected static final String RECURRING_LIMIT_DATE = "until";

    protected final DateTimeParser dateTimeParser = DateTimeParser.getInstance();

    @Override
    public Parameters parseCommandMap(Map<String, String> commandMap) {
        assert commandMap.containsKey(NAME_FIELD);

        Parameters result = new Parameters();
        result.name = commandMap.get(NAME_FIELD);

        determineTypeAndPopulateFields(commandMap, result);

        return result;
    }

    void determineTypeAndPopulateFields(Map<String, String> commandMap, Parameters result) {
        if (isEventMap(commandMap)) {
            fillUpEventNull(commandMap);
            populateEventParameters(commandMap, result);

            if (isRecurringMap(commandMap)) {
                populateRecurringParameters(commandMap, result);
            }
        } else if (isTaskMap(commandMap)) {
            populateTaskParameters(commandMap, result);

            if (isRecurringMap(commandMap)) {
                populateRecurringParameters(commandMap, result);
            }
        } else {
            result.commandClass = CommandClass.GENERIC;

            if (isRecurringMap(commandMap)) {
                fillUpTaskNull(commandMap);
                populateRecurringParameters(commandMap, result);
            }
        }
    }

    boolean isEventMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, EVENT_START_FIELD, TASK_DEADLINE_FIELD);

        return commandMap.containsKey(EVENT_START_FIELD);
    }

    void populateEventParameters(Map<String, String> commandMap, Parameters result) {
        List<LocalDateTime> startEndDateTime = dateTimeParser
                .parseDoubleDateTime(commandMap.get(EVENT_START_FIELD), commandMap.get(EVENT_END_FIELD));

        result.startDateTime = startEndDateTime.get(0);
        result.endDateTime = startEndDateTime.get(1);
        result.commandClass = CommandClass.EVENT;
    }

    void fillUpEventNull(Map<String, String> commandMap) {
        if (!commandMap.containsKey(EVENT_START_FIELD)) {
            commandMap.put(EVENT_START_FIELD, "");
        }

        if (!commandMap.containsKey(EVENT_END_FIELD)) {
            commandMap.put(EVENT_END_FIELD, "");
        }
    }

    boolean isTaskMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, TASK_DEADLINE_FIELD, EVENT_START_FIELD);
        checkMutuallyExclusiveKeywords(commandMap, TASK_DEADLINE_FIELD, EVENT_END_FIELD);

        return commandMap.containsKey(TASK_DEADLINE_FIELD);
    }

    void populateTaskParameters(Map<String, String> commandMap, Parameters result) {
        result.endDateTime = dateTimeParser.parseSingleDateTime(commandMap.get(TASK_DEADLINE_FIELD));
        result.commandClass = CommandClass.DEADLINE;
    }

    void fillUpTaskNull(Map<String, String> commandMap) {
        if (!commandMap.containsKey(TASK_DEADLINE_FIELD)) {
            commandMap.put(TASK_DEADLINE_FIELD, "");
        }
    }

    boolean isRecurringMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, RECURRING_LIMIT_DATE, RECURRING_LIMIT_OCCURRENCES);

        return commandMap.containsKey(RECURRING_PERIOD_FIELD);
    }

    void populateRecurringParameters(Map<String, String> commandMap, Parameters result) {
        if (commandMap.containsKey(RECURRING_LIMIT_DATE)) {
            result.dateLimit = dateTimeParser.parseDateTimeAsIs(commandMap.get(RECURRING_LIMIT_DATE)).toLocalDate();

            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT_DATE,
                    CommandClass.RECURRING_TASK_DATE);
        } else if (commandMap.containsKey(RECURRING_LIMIT_OCCURRENCES)) {
            result.occurLimit = Integer.parseInt(commandMap.get(RECURRING_LIMIT_OCCURRENCES));

            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT_OCCURRENCES,
                    CommandClass.RECURRING_TASK_OCCURRENCES);
        } else {
            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT, CommandClass.RECURRING_TASK);
        }
    }

    void assignRecurringClass(Map<String, String> commandMap, Parameters result, CommandClass eventEnum,
            CommandClass taskEnum) {
        if (isEventMap(commandMap)) {
            result.commandClass = eventEnum;
        } else {
            result.commandClass = taskEnum;
        }
    }
}
