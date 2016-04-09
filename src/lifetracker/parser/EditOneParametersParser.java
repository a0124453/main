package lifetracker.parser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static lifetracker.parser.CommandParametersParser.checkMutuallyExclusiveKeywords;

//@@author A0091173J
public class EditOneParametersParser implements CommandParametersParser {

    private static EditOneParametersParser ourInstance = new EditOneParametersParser();

    public static EditOneParametersParser getInstance() {
        return ourInstance;
    }

    protected static final String NAME_FIELD = "name";
    protected static final String EVENT_START_FIELD = "from";
    protected static final String EVENT_END_FIELD = "to";
    protected static final String TASK_DEADLINE_FIELD = "by";

    protected final DateTimeParser dateTimeParser = DateTimeParser.getInstance();
    protected final DurationParser durationParser = DurationParser.getInstance();

    @Override
    public Parameters parseCommandMap(Map<String, String> commandMap) {
        Parameters results = new Parameters();
        results.name = commandMap.get(NAME_FIELD);

        determineTypeAndPopulateFields(commandMap, results);

        return results;
    }

    void determineTypeAndPopulateFields(Map<String, String> commandMap, Parameters results){
        if(isEventMap(commandMap)){
            fillUpEventNull(commandMap);
            populateEventParameters(commandMap, results);
        } else if(isTaskMap(commandMap)){
            populateTaskParameters(commandMap, results);
        } else{
            results.commandClass = CommandClass.GENERIC;
        }
    }

    boolean isTaskMap(Map<String, String> commandMap) {
        checkMutuallyExclusiveKeywords(commandMap, TASK_DEADLINE_FIELD, EVENT_START_FIELD);
        checkMutuallyExclusiveKeywords(commandMap, TASK_DEADLINE_FIELD, EVENT_END_FIELD);

        return commandMap.containsKey(TASK_DEADLINE_FIELD);
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

    void populateTaskParameters(Map<String, String> commandMap, Parameters result) {
        result.endDateTime = dateTimeParser.parseSingleDateTime(commandMap.get(TASK_DEADLINE_FIELD));
        result.commandClass = CommandClass.DEADLINE;
    }
}
