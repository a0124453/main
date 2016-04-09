package lifetracker.parser.syntax;

import java.util.Map;

import static lifetracker.parser.syntax.CommandParametersParser.checkMutuallyExclusiveKeywords;

//@@author A0091173J
public class AddParameterParser extends EditOneParametersParser {
    private static AddParameterParser ourInstance = new AddParameterParser();

    public static AddParameterParser getInstance() {
        return ourInstance;
    }

    //TODO Set a enum
    protected static final String RECURRING_PERIOD_FIELD = "every";
    protected static final String RECURRING_LIMIT_OCCURRENCES = "for";
    protected static final String RECURRING_LIMIT_DATE = "until";

    @Override
    public Parameters parseCommandMap(Map<String, String> commandMap) {
        assert commandMap.containsKey(NAME_FIELD);

        Parameters result = new Parameters();
        result.name = commandMap.get(NAME_FIELD);

        determineTypeAndPopulateFields(commandMap, result);

        return result;
    }

    void determineTypeAndPopulateFields(Map<String, String> commandMap, Parameters result) {

        super.determineTypeAndPopulateFields(commandMap, result);

        if(isRecurringMap(commandMap)){

            if(result.commandClass == CommandClass.GENERIC){
                fillUpTaskNull(commandMap);
                populateTaskParameters(commandMap, result);
            }

            populateRecurringParameters(commandMap, result);
        }
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

        result.recurringPeriod = durationParser.parse(commandMap.get(RECURRING_PERIOD_FIELD));

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

    private void assignRecurringClass(Map<String, String> commandMap, Parameters result, CommandClass eventEnum,
            CommandClass taskEnum) {
        if (isEventMap(commandMap)) {
            result.commandClass = eventEnum;
        } else {
            result.commandClass = taskEnum;
        }
    }
}
