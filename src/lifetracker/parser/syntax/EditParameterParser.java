package lifetracker.parser.syntax;

import java.util.Map;

import static lifetracker.parser.syntax.CommandClass.GENERIC;
import static lifetracker.parser.syntax.CommandClass.RECURRING;
import static lifetracker.parser.syntax.CommandClass.RECURRING_DATE;
import static lifetracker.parser.syntax.CommandClass.RECURRING_EVENT;
import static lifetracker.parser.syntax.CommandClass.RECURRING_EVENT_DATE;
import static lifetracker.parser.syntax.CommandClass.RECURRING_EVENT_OCCURRENCES;
import static lifetracker.parser.syntax.CommandClass.RECURRING_OCCURRENCES;
import static lifetracker.parser.syntax.CommandClass.RECURRING_TASK;
import static lifetracker.parser.syntax.CommandClass.RECURRING_TASK_DATE;
import static lifetracker.parser.syntax.CommandClass.RECURRING_TASK_OCCURRENCES;
import static lifetracker.parser.syntax.CommandClass.STOP_RECURRING;
import static lifetracker.parser.syntax.CommandOptions.BY;
import static lifetracker.parser.syntax.CommandOptions.EVERY;
import static lifetracker.parser.syntax.CommandOptions.FOR;
import static lifetracker.parser.syntax.CommandOptions.FOREVER;
import static lifetracker.parser.syntax.CommandOptions.FROM;
import static lifetracker.parser.syntax.CommandOptions.NAME;
import static lifetracker.parser.syntax.CommandOptions.NODUE;
import static lifetracker.parser.syntax.CommandOptions.STOP;
import static lifetracker.parser.syntax.CommandOptions.TO;
import static lifetracker.parser.syntax.CommandOptions.UNTIL;
import static lifetracker.parser.syntax.CommandParametersParser.checkMutuallyExclusiveKeywords;

//@@author A0091173J

/**
 * The parameters parser for the "edit" command.
 */
public class EditParameterParser extends AddParameterParser {
    private static EditParameterParser ourInstance = new EditParameterParser();

    /**
     * Gets the instance of this Singleton class.
     * @return The instance of this class.
     */
    public static EditParameterParser getInstance() {
        return ourInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameters parseCommandMap(Map<CommandOptions, String> commandMap) {
        Parameters result = new Parameters();

        result.name = commandMap.get(NAME);

        determineTypeAndPopulateFields(commandMap, result);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    protected void determineTypeAndPopulateFields(Map<CommandOptions, String> commandMap, Parameters result) {
        if (isEventMap(commandMap)) {
            fillUpEventNull(commandMap);
            populateEventParameters(commandMap, result);
            resolveAndProcessRecurringCommandType(commandMap, result);
        } else if (isTaskMap(commandMap)) {
            populateTaskParameters(commandMap, result);
            resolveAndProcessRecurringCommandType(commandMap, result);
        } else {
            result.commandClass = GENERIC;
            if (isRemoveDateMap(commandMap)) {
                result.isForcedOverwrite = true;
            } else {
                resolveAndProcessRecurringCommandType(commandMap, result);
                if (isStopMap(commandMap)) {
                    result.commandClass = STOP_RECURRING;
                }
            }
        }
    }

    @Override
    protected boolean isRecurringMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, NODUE, EVERY);
        checkMutuallyExclusiveKeywords(commandMap, FOREVER, FOR, UNTIL);
        checkMutuallyExclusiveKeywords(commandMap, STOP, EVERY);

        return super.isRecurringMap(commandMap)
                || commandMap.containsKey(FOR)
                || commandMap.containsKey(UNTIL);
    }

    @Override
    protected boolean isEventMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, BY, NODUE);
        return super.isEventMap(commandMap);
    }

    @Override
    protected boolean isTaskMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, FROM, NODUE);
        checkMutuallyExclusiveKeywords(commandMap, TO, NODUE);
        return super.isTaskMap(commandMap);
    }

    @Override
    protected void populateRecurringParameters(Map<CommandOptions, String> commandMap, Parameters result) {

        if (commandMap.containsKey(EVERY)) {
            result.recurringPeriod = durationParser.parse(commandMap.get(EVERY));
        }

        if (commandMap.containsKey(UNTIL)) {
            result.dateLimit = dateTimeParser.parseDateTimeAsIs(commandMap.get(UNTIL)).toLocalDate();

            assignRecurringClass(commandMap, result, RECURRING_EVENT_DATE,
                    RECURRING_TASK_DATE, RECURRING_DATE);

        } else if (commandMap.containsKey(FOR)) {
            result.occurLimit = Integer.parseInt(commandMap.get(FOR));

            assignRecurringClass(commandMap, result, RECURRING_EVENT_OCCURRENCES,
                    RECURRING_TASK_OCCURRENCES, RECURRING_OCCURRENCES);
        } else {
            assignRecurringClass(commandMap, result, RECURRING_EVENT, RECURRING_TASK, RECURRING);
        }
    }

    protected boolean isStopMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, STOP, EVERY);
        checkMutuallyExclusiveKeywords(commandMap, FOR, UNTIL,FOREVER);

        return commandMap.containsKey(STOP);
    }

    protected boolean isUnlimitMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, FOR, UNTIL, FOREVER);

        return commandMap.containsKey(FOREVER);
    }

    protected boolean isRemoveDateMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, BY, NODUE);
        checkMutuallyExclusiveKeywords(commandMap, TO, NODUE);
        checkMutuallyExclusiveKeywords(commandMap, FROM, NODUE);
        checkMutuallyExclusiveKeywords(commandMap, EVERY, NODUE);
        checkMutuallyExclusiveKeywords(commandMap, FOR, NODUE);
        checkMutuallyExclusiveKeywords(commandMap, UNTIL, NODUE);

        return commandMap.containsKey(NODUE);
    }

    private void resolveAndProcessRecurringCommandType(Map<CommandOptions, String> commandMap, Parameters result) {
        if (isRecurringMap(commandMap)) {
            populateRecurringParameters(commandMap, result);
        } else if (isUnlimitMap(commandMap)) {
            populateRecurringParameters(commandMap, result);
            result.isForcedOverwrite = true;
        } else if (isStopMap(commandMap)) {
            result.isForcedOverwrite = true;
        }
    }

    private void assignRecurringClass(Map<CommandOptions, String> commandMap, Parameters result, CommandClass eventEnum,
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
