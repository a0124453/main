package lifetracker.parser;

import lifetracker.command.CommandFactory;
import lifetracker.command.CommandObject;
import lifetracker.parser.datetime.DateTimeParser;
import lifetracker.parser.datetime.DurationParser;
import lifetracker.parser.syntax.AddParameterParser;
import lifetracker.parser.syntax.CommandBodyParser;
import lifetracker.parser.syntax.CommandOptions;
import lifetracker.parser.syntax.CommandSectionsParser;
import lifetracker.parser.syntax.EditOneParametersParser;
import lifetracker.parser.syntax.EditParameterParser;
import lifetracker.parser.syntax.Parameters;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

//@@author A0091173J
public class ParserImpl implements Parser {

    private static final String ERROR_INVALID_ID = "\"%1$s\" is not a valid ID!";

    private static final String ERROR_INVALID_EDIT = "Invalid syntax for edit command!";

    private static final DateTimeParser DATE_TIME_PARSER = DateTimeParser.getInstance();

    private static final DurationParser DURATION_PARSER = DurationParser.getInstance();

    private static final Map<CommandOptions, Predicate<String>> EDITONE_OPTIONS_WITH_VERIFICATIONS = new HashMap<>();

    static {
        EDITONE_OPTIONS_WITH_VERIFICATIONS.put(CommandOptions.BY, DATE_TIME_PARSER::isDateTime);
        EDITONE_OPTIONS_WITH_VERIFICATIONS.put(CommandOptions.FROM, DATE_TIME_PARSER::isDateTime);
        EDITONE_OPTIONS_WITH_VERIFICATIONS.put(CommandOptions.TO, DATE_TIME_PARSER::isDateTime);
    }

    private static final Map<CommandOptions, Predicate<String>> ADD_OPTIONS_VERIFICATIONS
            = new HashMap<>(EDITONE_OPTIONS_WITH_VERIFICATIONS);

    static {
        ADD_OPTIONS_VERIFICATIONS.put(CommandOptions.EVERY, DURATION_PARSER::isDurationString);
        ADD_OPTIONS_VERIFICATIONS.put(CommandOptions.UNTIL, DATE_TIME_PARSER::isDateTime);
        ADD_OPTIONS_VERIFICATIONS.put(CommandOptions.FOR, StringUtils::isNumeric);
    }

    private static final Map<CommandOptions, Predicate<String>> EDIT_OPTIONS_VERIFICATIONS = new HashMap<>(
            ADD_OPTIONS_VERIFICATIONS);

    static {
        EDIT_OPTIONS_VERIFICATIONS.put(CommandOptions.NODUE, StringUtils::isBlank);
        EDIT_OPTIONS_VERIFICATIONS.put(CommandOptions.STOP, StringUtils::isBlank);
        EDIT_OPTIONS_VERIFICATIONS.put(CommandOptions.FOREVER, StringUtils::isBlank);
    }

    private static final Map<String, CommandOptions> KEYWORD_TO_ENUM_MAP = new HashMap<>();

    static {
        KEYWORD_TO_ENUM_MAP.put("by", CommandOptions.BY);
        KEYWORD_TO_ENUM_MAP.put("from", CommandOptions.FROM);
        KEYWORD_TO_ENUM_MAP.put("to", CommandOptions.TO);
        KEYWORD_TO_ENUM_MAP.put("every", CommandOptions.EVERY);
        KEYWORD_TO_ENUM_MAP.put("until", CommandOptions.UNTIL);
        KEYWORD_TO_ENUM_MAP.put("for", CommandOptions.FOR);
        KEYWORD_TO_ENUM_MAP.put("stop", CommandOptions.STOP);
        KEYWORD_TO_ENUM_MAP.put("nodue", CommandOptions.NODUE);
        KEYWORD_TO_ENUM_MAP.put("forever", CommandOptions.FOREVER);
    }

    private static final String defaultCommand = "add";

    private static final String FULL_COMMAND_SEPARATOR = " > ";

    private final Map<String, Function<List<String>, CommandObject>> commands = new HashMap<>();

    private final CommandSectionsParser cmdParser;

    private final CommandFactory commandObjectFactory;

    public ParserImpl(CommandFactory commandFactory) {
        populateCommand();
        cmdParser = new CommandSectionsParser(commands.keySet(), defaultCommand);
        commandObjectFactory = commandFactory;
    }

    @Override
    public CommandObject parse(String userInput) {
        List<String> commandSegments = cmdParser.parseFullCommand(userInput, FULL_COMMAND_SEPARATOR);

        String command = commandSegments.get(0).toLowerCase();
        commandSegments.remove(0);

        return commands.get(command).apply(commandSegments);
    }
    
    private void populateCommand() {
        commands.put("add", this::processAdd);
        commands.put("delete", this::processDelete);
        commands.put("edit", this::processEdit);
        commands.put("editone", this::processEditOne);
        commands.put("list", this::processFind);
        commands.put("find", this::processFind);
        commands.put("search", this::processFind);
        commands.put("listall", this::processFindAll);
        commands.put("findall", this::processFindAll);
        commands.put("searchall", this::processFindAll);
        commands.put("findold", this::processFindOld);
        commands.put("listold", this::processFindOld);
        commands.put("searchold", this::processFindOld);
        commands.put("mark", this::processMark);
    }

    private CommandObject processAdd(List<String> commandBody) {
        String addCommandBody = restoreCommandSections(commandBody);

        CommandBodyParser<CommandOptions> bodyParser = new CommandBodyParser<>(KEYWORD_TO_ENUM_MAP,
                ADD_OPTIONS_VERIFICATIONS, CommandOptions.NAME);

        Map<CommandOptions, String> commandBodySectionsMap = bodyParser.parseCommandBody(addCommandBody);

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

        int id = getIDFromList(commandBody);

        String editCommandSection = restoreCommandSections(commandBody.subList(1, commandBody.size()));

        CommandBodyParser<CommandOptions> bodyParser = new CommandBodyParser<>(KEYWORD_TO_ENUM_MAP,
                EDIT_OPTIONS_VERIFICATIONS, CommandOptions.NAME);

        Map<CommandOptions, String> editSectionMap = bodyParser.parseCommandBody(editCommandSection);

        Parameters params = EditParameterParser.getInstance().parseCommandMap(editSectionMap);

        return processParametersForEdit(id, params);
    }

    private CommandObject processEditOne(List<String> commandBody) {
        if (commandBody.size() < 2) {
            throw new IllegalArgumentException(ERROR_INVALID_EDIT);
        }

        int id = getIDFromList(commandBody);
        String editCommandSection = restoreCommandSections(commandBody.subList(1, commandBody.size()));

        CommandBodyParser<CommandOptions> bodyParser = new CommandBodyParser<>(KEYWORD_TO_ENUM_MAP,
                EDITONE_OPTIONS_WITH_VERIFICATIONS, CommandOptions.NAME);

        Map<CommandOptions, String> editSectionMap = bodyParser.parseCommandBody(editCommandSection);

        Parameters params = EditOneParametersParser.getInstance().parseCommandMap(editSectionMap);

        return processParametersForEditOne(id, params);
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

    private CommandObject processFindOld(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.findOld();
        } else {
            return commandObjectFactory.findOld(searchTerm);
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
                        .editDeadline(id, params.name, params.endDateTime, params.isForcedOverwrite);
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
                        params.isForcedOverwrite);
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
            case STOP_RECURRING:
                return commandObjectFactory.editStop(id, params.name);
            default:
                assert false;
                return null;
        }
    }

    private CommandObject processParametersForEditOne(int id, Parameters params) {
        switch (params.commandClass) {
            case GENERIC:
                return commandObjectFactory.editOne(id, params.name);
            case DEADLINE:
                return commandObjectFactory.editOneToDeadline(id, params.name, params.endDateTime);
            case EVENT:
                return commandObjectFactory.editOneToEvent(id, params.name, params.startDateTime, params.endDateTime);
            default:
                assert false;
                return null;
        }
    }

    private int getIDFromList(List<String> stringList) {
        String idString = stringList.get(0).trim();
        int id;

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.format(ERROR_INVALID_ID, idString));
        }
        return id;
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
