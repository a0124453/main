package lifetracker.parser;

import lifetracker.command.CommandFactory;
import lifetracker.command.CommandObject;
import lifetracker.parser.datetime.DateTimeParser;
import lifetracker.parser.datetime.DurationParser;
import lifetracker.parser.syntax.AddParameterParser;
import lifetracker.parser.syntax.CommandSectionParser;
import lifetracker.parser.syntax.CommandOptions;
import lifetracker.parser.syntax.FullCommandParser;
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

/**
 * An implementation of the Parser.
 * <p>
 * This ParserImpl parses commands from the user. It supports commands involving IDs, date/times, as well as durations.
 * <p>
 * The ParserImpl automatically determines the type of command to be executed by detecting the options the user has
 * provided, and creates the correct CommandObject accordingly.
 */
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
        ADD_OPTIONS_VERIFICATIONS.put(CommandOptions.EVERY, DURATION_PARSER::isDuration);
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
        KEYWORD_TO_ENUM_MAP.put("at", CommandOptions.BY);
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

    private final FullCommandParser cmdParser;

    private final CommandFactory commandObjectFactory;

    /**
     * Creates a new {@code ParserImpl} with the specified {@code CommandFactory} as an injected dependency.
     *
     * @param commandFactory The {@code CommandFactory} dependency.
     */
    public ParserImpl(CommandFactory commandFactory) {
        populateCommand();
        cmdParser = new FullCommandParser(commands.keySet(), defaultCommand);
        commandObjectFactory = commandFactory;
    }

    /**
     * {@inheritDoc}
     */
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
        commands.put("today", this::processToday);
        commands.put("todayall", this::processTodayAll);
        commands.put("todayold", this::processTodayOld);
        commands.put("mark", this::processMark);
    }

    /**
     * Parses the split sections of a command identified as "add", and produces an AddCommand accordingly.
     * <p>
     * This method will further parse and analyze additional options to the command to determine the type of Add
     * command
     * the user intends.
     *
     * @param commandBody The sections of the command
     * @return The corresponding {@code AddCommand}
     * @see lifetracker.command.AddCommand
     */
    private CommandObject processAdd(List<String> commandBody) {
        String addCommandBody = restoreCommandSections(commandBody);

        CommandSectionParser<CommandOptions> bodyParser = new CommandSectionParser<>(KEYWORD_TO_ENUM_MAP,
                ADD_OPTIONS_VERIFICATIONS, CommandOptions.NAME);

        Map<CommandOptions, String> commandBodySectionsMap = bodyParser.parseCommandSection(addCommandBody);

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

    /**
     * Parses the split sections of a command identified as "edit", and produces the respectively CommandObject that
     * edits the calendar accordingly.
     * <p>
     * This method acts similar to the {@link #processAdd(List)} method. However, it expects a ID in the first
     * section of the command.
     *
     * @param commandBody The split command sections
     * @return The corresponding edit {@code CommandObject}
     */
    private CommandObject processEdit(List<String> commandBody) {
        if (commandBody.size() < 2) {
            throw new IllegalArgumentException(ERROR_INVALID_EDIT);
        }

        int id = getIDFromList(commandBody);

        String editCommandSection = restoreCommandSections(commandBody.subList(1, commandBody.size()));

        CommandSectionParser<CommandOptions> bodyParser = new CommandSectionParser<>(KEYWORD_TO_ENUM_MAP,
                EDIT_OPTIONS_VERIFICATIONS, CommandOptions.NAME);

        Map<CommandOptions, String> editSectionMap = bodyParser.parseCommandSection(editCommandSection);

        Parameters params = EditParameterParser.getInstance().parseCommandMap(editSectionMap);

        return processParametersForEdit(id, params);
    }

    /**
     * Parses the split sections of a command identified as "editone", and produces an {@code EditOneCommand}
     * accordingly.
     * <p>
     * This method acts similarly to {@link #processEdit(List)}, though less options are allowed.
     *
     * @param commandBody The split command sections
     * @return The corresponding {@code EditOneCommand}
     * @see lifetracker.command.EditOneCommand
     */
    private CommandObject processEditOne(List<String> commandBody) {
        if (commandBody.size() < 2) {
            throw new IllegalArgumentException(ERROR_INVALID_EDIT);
        }

        int id = getIDFromList(commandBody);
        String editCommandSection = restoreCommandSections(commandBody.subList(1, commandBody.size()));

        CommandSectionParser<CommandOptions> bodyParser = new CommandSectionParser<>(KEYWORD_TO_ENUM_MAP,
                EDITONE_OPTIONS_WITH_VERIFICATIONS, CommandOptions.NAME);

        Map<CommandOptions, String> editSectionMap = bodyParser.parseCommandSection(editCommandSection);

        Parameters params = EditOneParametersParser.getInstance().parseCommandMap(editSectionMap);

        return processParametersForEditOne(id, params);
    }

    private CommandObject processFind(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.find(false);
        } else {
            return commandObjectFactory.find(searchTerm, false);
        }
    }

    private CommandObject processFindAll(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.findAll(false);
        } else {
            return commandObjectFactory.findAll(searchTerm, false);
        }
    }

    private CommandObject processFindOld(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.findOld(false);
        } else {
            return commandObjectFactory.findOld(searchTerm, false);
        }
    }

    private CommandObject processToday(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.find(true);
        } else {
            return commandObjectFactory.find(searchTerm, false);
        }
    }

    private CommandObject processTodayOld(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.findOld(true);
        } else {
            return commandObjectFactory.findOld(searchTerm, false);
        }
    }

    private CommandObject processTodayAll(List<String> commandBody) {
        String searchTerm = restoreCommandSections(commandBody).trim();

        if (searchTerm.isEmpty()) {
            return commandObjectFactory.findAll(true);
        } else {
            return commandObjectFactory.findAll(searchTerm, false);
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

    /**
     * Determines the correct "add" {@code CommandObject} to create based on the {@code CommandClass} specified in the
     * given {@code Paramaters} object.
     *
     * @param params The Parameters object.
     * @return The correct "add" {@code CommandObject}
     * @see Parameters
     * @see lifetracker.parser.syntax.CommandClass
     */
    private CommandObject processParametersForAdd(Parameters params) {
        switch (params.commandClass) {
        case GENERIC :
            return commandObjectFactory.addGenericTask(params.name);
        case DEADLINE :
            return commandObjectFactory.addDeadlineTask(params.name, params.endDateTime);
        case RECURRING_TASK :
            return commandObjectFactory
                    .addRecurringDeadlineTask(params.name, params.endDateTime, params.recurringPeriod);
        case RECURRING_TASK_DATE :
            return commandObjectFactory
                    .addRecurringDeadlineTask(params.name, params.endDateTime, params.recurringPeriod,
                            params.dateLimit);
        case RECURRING_TASK_OCCURRENCES :
            return commandObjectFactory
                    .addRecurringDeadlineTask(params.name, params.endDateTime, params.recurringPeriod,
                            params.occurLimit);
        case EVENT :
            return commandObjectFactory.addEvent(params.name, params.startDateTime, params.endDateTime);
        case RECURRING_EVENT :
            return commandObjectFactory.addRecurringEvent(params.name, params.startDateTime, params.endDateTime,
                    params.recurringPeriod);
        case RECURRING_EVENT_OCCURRENCES :
            return commandObjectFactory.addRecurringEvent(params.name, params.startDateTime, params.endDateTime,
                    params.recurringPeriod, params.occurLimit);
        case RECURRING_EVENT_DATE :
            return commandObjectFactory.addRecurringEvent(params.name, params.startDateTime, params.endDateTime,
                    params.recurringPeriod, params.dateLimit);
        default :
            assert false;
            return null;
        }
    }

    /**
     * Determines the correct "edit" {@code CommandObject} to create based on the {@code CommandClass} specified in the
     * given {@code Paramaters} object.
     *
     * @param params The Parameters object.
     * @return The correct "edit" {@code CommandObject}
     * @see Parameters
     * @see lifetracker.parser.syntax.CommandClass
     */
    private CommandObject processParametersForEdit(int id, Parameters params) {
        switch (params.commandClass) {
        case GENERIC :
            return commandObjectFactory.editGenericTask(id, params.name, params.isForcedOverwrite);
        case DEADLINE :
            return commandObjectFactory
                    .editDeadline(id, params.name, params.endDateTime, params.isForcedOverwrite);
        case RECURRING_TASK :
            return commandObjectFactory
                    .editRecurringDeadline(id, params.name, params.endDateTime, params.recurringPeriod,
                            params.isForcedOverwrite);
        case RECURRING_TASK_DATE :
            return commandObjectFactory
                    .editRecurringDeadline(id, params.name, params.endDateTime, params.recurringPeriod,
                            params.dateLimit);
        case RECURRING_TASK_OCCURRENCES :
            return commandObjectFactory
                    .editRecurringDeadline(id, params.name, params.endDateTime, params.recurringPeriod,
                            params.occurLimit);
        case EVENT :
            return commandObjectFactory.editEvent(id, params.name, params.startDateTime, params.endDateTime,
                    params.isForcedOverwrite);
        case RECURRING_EVENT :
            return commandObjectFactory
                    .editRecurringEvent(id, params.name, params.startDateTime, params.endDateTime,
                            params.recurringPeriod, params.isForcedOverwrite);
        case RECURRING_EVENT_DATE :
            return commandObjectFactory
                    .editRecurringEvent(id, params.name, params.startDateTime, params.endDateTime,
                            params.recurringPeriod, params.dateLimit);
        case RECURRING_EVENT_OCCURRENCES :
            return commandObjectFactory
                    .editRecurringEvent(id, params.name, params.startDateTime, params.endDateTime,
                            params.recurringPeriod, params.occurLimit);
        case RECURRING :
            return commandObjectFactory
                    .editRecurring(id, params.name, params.recurringPeriod, params.isForcedOverwrite);
        case RECURRING_DATE :
            return commandObjectFactory.editRecurring(id, params.name, params.recurringPeriod, params.dateLimit);
        case RECURRING_OCCURRENCES :
            return commandObjectFactory.editRecurring(id, params.name, params.recurringPeriod, params.occurLimit);
        case STOP_RECURRING :
            return commandObjectFactory.editStop(id, params.name);
        default :
            assert false;
            return null;
        }
    }

    /**
     * Determines the correct "editone" {@code CommandObject} to create based on the {@code CommandClass} specified in
     * the
     * given {@code Paramaters} object.
     *
     * @param params The Parameters object.
     * @return The correct "editone" {@code CommandObject}
     * @see Parameters
     * @see lifetracker.parser.syntax.CommandClass
     */
    private CommandObject processParametersForEditOne(int id, Parameters params) {
        switch (params.commandClass) {
        case GENERIC :
            return commandObjectFactory.editOne(id, params.name);
        case DEADLINE :
            return commandObjectFactory.editOneToDeadline(id, params.name, params.endDateTime);
        case EVENT :
            return commandObjectFactory.editOneToEvent(id, params.name, params.startDateTime, params.endDateTime);
        default :
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
