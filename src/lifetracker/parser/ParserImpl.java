package lifetracker.parser;

import lifetracker.command.CommandFactory;
import lifetracker.command.CommandObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParserImpl implements Parser {

    private static final String ERROR_INVALID_ID = "\"%1$s\" is not a valid ID!";

    private static final String ERROR_INVALID_EDIT = "Invalid syntax for edit command!";

    private static final DateTimeParser DATE_TIME_PARSER = DateTimeParser.getInstance();

    private static final DurationParser DURATION_PARSER = DurationParser.getInstance();

    private static final Map<String, Predicate<String>> ADD_KEYWORDS_WITH_VERIFICATIONS = new HashMap<>();

    static {
        ADD_KEYWORDS_WITH_VERIFICATIONS.put("by", DATE_TIME_PARSER::isDateTime);
        ADD_KEYWORDS_WITH_VERIFICATIONS.put("from", DATE_TIME_PARSER::isDateTime);
        ADD_KEYWORDS_WITH_VERIFICATIONS.put("to", DATE_TIME_PARSER::isDateTime);
        ADD_KEYWORDS_WITH_VERIFICATIONS.put("every", DURATION_PARSER::isDurationString);
        ADD_KEYWORDS_WITH_VERIFICATIONS.put("until", DATE_TIME_PARSER::isDateTime);
        ADD_KEYWORDS_WITH_VERIFICATIONS.put("for", StringUtils::isNumeric);

    }

    private static final Map<String, Predicate<String>> EDIT_KEYWORDS_WITH_VERIFICATIONS = new HashMap<>(
            ADD_KEYWORDS_WITH_VERIFICATIONS);

    static {
        EDIT_KEYWORDS_WITH_VERIFICATIONS.put("nodue", StringUtils::isBlank);
        EDIT_KEYWORDS_WITH_VERIFICATIONS.put("stop", StringUtils::isBlank);
        EDIT_KEYWORDS_WITH_VERIFICATIONS.put("forever", StringUtils::isBlank);
    }

    private static final String defaultCommand = "add";

    private static final String FULL_COMMAND_SEPARATOR = " > ";

    private final Map<String, Function<List<String>, CommandObject>> commands = new HashMap<>();

    {
        commands.put("add", this::processAdd);
        commands.put("delete", this::processDelete);
        commands.put("edit", this::processEdit);
        commands.put("list", this::processFind);
        commands.put("find", this::processFind);
        commands.put("search", this::processFind);
        commands.put("listall", this::processFindAll);
        commands.put("findall", this::processFindAll);
        commands.put("searchall", this::processFindAll);
        commands.put("mark", this::processMark);
    }

    private final CommandParser cmdParser;

    private final CommandFactory commandObjectFactory;

    public ParserImpl(CommandFactory commandFactory) {
        cmdParser = new CommandParser(commands.keySet(), defaultCommand);
        commandObjectFactory = commandFactory;
    }

    @Override
    public CommandObject parse(String userInput) {
        List<String> commandSegments = cmdParser.parseFullCommand(userInput, FULL_COMMAND_SEPARATOR);

        String command = commandSegments.get(0).toLowerCase();
        commandSegments.remove(0);

        return commands.get(command).apply(commandSegments);
    }

    private CommandObject processAdd(List<String> commandBody) {
        String addCommandBody = restoreCommandSections(commandBody);

        Map<String, String> commandBodySectionsMap = cmdParser
                .parseCommandBody(addCommandBody, ADD_KEYWORDS_WITH_VERIFICATIONS);

        Parameters params = AddParameterParser.getInstance().parseCommandMap(commandBodySectionsMap);

        return processParametersForAdd(params);
    }

    private CommandObject processDelete(List<String> commandBody) {
        String idString = restoreCommandSections(commandBody);
        try {
            int id = Integer.parseInt(idString);

            return commandObjectFactory.delete(id);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.format(ERROR_INVALID_ID, idString));
        }
    }

    private CommandObject processEdit(List<String> commandBody) {
        if (commandBody.size() < 2) {
            throw new IllegalArgumentException(ERROR_INVALID_EDIT);
        }

        String idString = commandBody.get(0);
        int id;

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.format(ERROR_INVALID_ID, idString));
        }

        String editCommandSection = restoreCommandSections(commandBody.subList(1, commandBody.size()));

        Map<String, String> editSectionMap = cmdParser
                .parseCommandBody(editCommandSection, EDIT_KEYWORDS_WITH_VERIFICATIONS);

        Parameters params = EditParameterParser.getInstance().parseCommandMap(editSectionMap);

        return processParametersForEdit(id, params);
    }

    private CommandObject processFind(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.find();
        } else {
            return commandObjectFactory.find(searchTerm);
        }
    }

    private CommandObject processFindAll(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.findAll();
        } else {
            return commandObjectFactory.findAll(searchTerm);
        }
    }

    private CommandObject processMark(List<String> commandBody) {
        String idString = restoreCommandSections(commandBody);

        try {
            int id = Integer.parseInt(idString);
            return commandObjectFactory.mark(id);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.format(ERROR_INVALID_ID, idString));
        }
    }

    private CommandObject processParametersForAdd(Parameters params) {
        switch (params.commandClass) {
            case GENERIC:
                return commandObjectFactory.addFloatingTask(params.name);
            case DEADLINE:
                return commandObjectFactory.addDeadlineTask(params.name, params.endDateTime);
            case RECURRING_TASK:
                return commandObjectFactory
                        .addRecurringDeadlineTask(params.name, params.endDateTime, params.recurringPeriod);
            case RECURRING_TASK_DATE:
                return commandObjectFactory
                        .addRecurringDeadlineTask(params.name, params.endDateTime, params.recurringPeriod,
                                params.dateLimit);
            case RECURRING_TASK_OCCURRENCES:
                return commandObjectFactory
                        .addRecurringDeadlineTask(params.name, params.endDateTime, params.recurringPeriod,
                                params.occurLimit);
            case EVENT:
                return commandObjectFactory.addEvent(params.name, params.startDateTime, params.endDateTime);
            case RECURRING_EVENT:
                return commandObjectFactory.addRecurringEvent(params.name, params.startDateTime, params.endDateTime,
                        params.recurringPeriod);
            case RECURRING_EVENT_OCCURRENCES:
                return commandObjectFactory.addRecurringEvent(params.name, params.startDateTime, params.endDateTime,
                        params.recurringPeriod, params.occurLimit);
            case RECURRING_EVENT_DATE:
                return commandObjectFactory.addRecurringEvent(params.name, params.startDateTime, params.endDateTime,
                        params.recurringPeriod, params.dateLimit);
            default:
                assert false;
                return null;
        }
    }

    private CommandObject processParametersForEdit(int id, Parameters params) {
        switch (params.commandClass) {
            case GENERIC:
                return commandObjectFactory.editGenericTask(id, params.name, params.isForcedOverwrite);
            case DEADLINE:
                return commandObjectFactory
                        .editDeadline(id, params.name, params.endDateTime, !params.isForcedOverwrite);
            case RECURRING_TASK:
                return commandObjectFactory
                        .editRecurringDeadline(id, params.name, params.endDateTime, params.recurringPeriod,
                                params.isForcedOverwrite);
            case RECURRING_TASK_DATE:
                return commandObjectFactory
                        .editRecurringDeadline(id, params.name, params.endDateTime, params.recurringPeriod,
                                params.dateLimit);
            case RECURRING_TASK_OCCURRENCES:
                return commandObjectFactory
                        .editRecurringDeadline(id, params.name, params.endDateTime, params.recurringPeriod,
                                params.occurLimit);
            case EVENT:
                return commandObjectFactory.editEvent(id, params.name, params.startDateTime, params.endDateTime,
                        !params.isForcedOverwrite);
            case RECURRING_EVENT:
                return commandObjectFactory
                        .editRecurringEvent(id, params.name, params.startDateTime, params.endDateTime,
                                params.recurringPeriod, params.isForcedOverwrite);
            case RECURRING_EVENT_DATE:
                return commandObjectFactory
                        .editRecurringEvent(id, params.name, params.startDateTime, params.endDateTime,
                                params.recurringPeriod, params.dateLimit);
            case RECURRING_EVENT_OCCURRENCES:
                return commandObjectFactory
                        .editRecurringEvent(id, params.name, params.startDateTime, params.endDateTime,
                                params.recurringPeriod, params.occurLimit);
            case RECURRING:
                return commandObjectFactory
                        .editRecurring(id, params.name, params.recurringPeriod, params.isForcedOverwrite);
            case RECURRING_DATE:
                return commandObjectFactory.editRecurring(id, params.name, params.recurringPeriod, params.dateLimit);
            case RECURRING_OCCURRENCES:
                return commandObjectFactory.editRecurring(id, params.name, params.recurringPeriod, params.occurLimit);
            case STOP:
                return commandObjectFactory.editStop(id, params.name, params.startDateTime, params.endDateTime);

            default:
                assert false;
                return null;
        }

    }

    private String restoreCommandSections(List<String> stringList) {

        if (stringList.isEmpty()) {
            return "";
        }

        StringBuilder collapsedString = new StringBuilder();

        for (String fragment : stringList) {
            collapsedString.append(FULL_COMMAND_SEPARATOR);

            collapsedString.append(fragment);
        }

        return collapsedString.substring(FULL_COMMAND_SEPARATOR.length());
    }
}
