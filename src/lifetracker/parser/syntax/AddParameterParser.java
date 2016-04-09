package lifetracker.parser.syntax;

import java.util.Map;

import static lifetracker.parser.syntax.CommandParametersParser.checkMutuallyExclusiveKeywords;

//@@author A0091173J
public class AddParameterParser extends EditOneParametersParser {
    private static AddParameterParser ourInstance = new AddParameterParser();

    public static AddParameterParser getInstance() {
        return ourInstance;
    }

    @Override
    public Parameters parseCommandMap(Map<CommandOptions, String> commandMap) {
        assert commandMap.containsKey(CommandOptions.NAME);

        Parameters result = new Parameters();
        result.name = commandMap.get(CommandOptions.NAME);

        determineTypeAndPopulateFields(commandMap, result);

        return result;
    }

    void determineTypeAndPopulateFields(Map<CommandOptions, String> commandMap, Parameters result) {

        super.determineTypeAndPopulateFields(commandMap, result);

        if(isRecurringMap(commandMap)){

            if(result.commandClass == CommandClass.GENERIC){
                fillUpTaskNull(commandMap);
                populateTaskParameters(commandMap, result);
            }

            populateRecurringParameters(commandMap, result);
        }
    }

    void fillUpTaskNull(Map<CommandOptions, String> commandMap) {
        if (!commandMap.containsKey(CommandOptions.BY)) {
            commandMap.put(CommandOptions.BY, "");
        }
    }

    boolean isRecurringMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, CommandOptions.UNTIL, CommandOptions.FOR);

        return commandMap.containsKey(CommandOptions.EVERY);
    }

    void populateRecurringParameters(Map<CommandOptions, String> commandMap, Parameters result) {

        result.recurringPeriod = durationParser.parse(commandMap.get(CommandOptions.EVERY));

        if (commandMap.containsKey(CommandOptions.UNTIL)) {
            result.dateLimit = dateTimeParser.parseDateTimeAsIs(commandMap.get(CommandOptions.UNTIL)).toLocalDate();

            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT_DATE,
                    CommandClass.RECURRING_TASK_DATE);
        } else if (commandMap.containsKey(CommandOptions.FOR)) {
            result.occurLimit = Integer.parseInt(commandMap.get(CommandOptions.FOR));

            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT_OCCURRENCES,
                    CommandClass.RECURRING_TASK_OCCURRENCES);
        } else {
            assignRecurringClass(commandMap, result, CommandClass.RECURRING_EVENT, CommandClass.RECURRING_TASK);
        }
    }

    private void assignRecurringClass(Map<CommandOptions, String> commandMap, Parameters result, CommandClass eventEnum,
            CommandClass taskEnum) {
        if (isEventMap(commandMap)) {
            result.commandClass = eventEnum;
        } else {
            result.commandClass = taskEnum;
        }
    }
}
