package lifetracker.parser.syntax;

import lifetracker.parser.datetime.DateTimeParser;
import lifetracker.parser.datetime.DurationParser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static lifetracker.parser.syntax.CommandOptions.BY;
import static lifetracker.parser.syntax.CommandOptions.FROM;
import static lifetracker.parser.syntax.CommandOptions.NAME;
import static lifetracker.parser.syntax.CommandOptions.TO;
import static lifetracker.parser.syntax.CommandParametersParser.checkMutuallyExclusiveKeywords;

//@@author A0091173J

/**
 * The parameters parser for the "editone" command.
 */
public class EditOneParametersParser implements CommandParametersParser {

    private static EditOneParametersParser ourInstance = new EditOneParametersParser();
    protected final DateTimeParser dateTimeParser = DateTimeParser.getInstance();
    protected final DurationParser durationParser = DurationParser.getInstance();

    /**
     * Gets the instance of this Singleton class.
     *
     * @return The instance of this class
     */
    public static EditOneParametersParser getInstance() {
        return ourInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameters parseCommandMap(Map<CommandOptions, String> commandMap) {
        Parameters results = new Parameters();
        results.name = commandMap.get(NAME);

        determineTypeAndPopulateFields(commandMap, results);

        return results;
    }

    /**
     * Determines the correct "add" {@code CommandObject} to create based on the {@code CommandClass} specified in the
     * given {@code Paramaters} object.
     *
     * @param params The Parameters object.
     * @return The correct "add" {@code CommandObject}
     * @see Parameters
     * @see lifetracker.parser.syntax.CommandClass
     */
    protected void determineTypeAndPopulateFields(Map<CommandOptions, String> commandMap, Parameters results) {
        if (isEventMap(commandMap)) {
            fillUpEventNull(commandMap);
            populateEventParameters(commandMap, results);
        } else if (isTaskMap(commandMap)) {
            populateTaskParameters(commandMap, results);
        } else {
            results.commandClass = CommandClass.GENERIC;
        }
    }

    protected boolean isTaskMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, BY, FROM);
        checkMutuallyExclusiveKeywords(commandMap, BY, TO);

        return commandMap.containsKey(BY);
    }

    protected boolean isEventMap(Map<CommandOptions, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, FROM, BY);

        return commandMap.containsKey(FROM);
    }

    protected void populateEventParameters(Map<CommandOptions, String> commandMap, Parameters result) {
        List<LocalDateTime> startEndDateTime = dateTimeParser
                .parseDoubleDateTime(commandMap.get(FROM), commandMap.get(TO));

        result.startDateTime = startEndDateTime.get(0);
        result.endDateTime = startEndDateTime.get(1);
        result.commandClass = CommandClass.EVENT;
    }

    protected void fillUpEventNull(Map<CommandOptions, String> commandMap) {
        if (!commandMap.containsKey(FROM)) {
            commandMap.put(FROM, "");
        }

        if (!commandMap.containsKey(TO)) {
            commandMap.put(TO, "");
        }
    }

    protected void populateTaskParameters(Map<CommandOptions, String> commandMap, Parameters result) {
        result.endDateTime = dateTimeParser.parseSingleDateTime(commandMap.get(BY));
        result.commandClass = CommandClass.DEADLINE;
    }
}
