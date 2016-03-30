package lifetracker.parser;

import lifetracker.command.CommandFactory;
import lifetracker.command.CommandObject;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParserImpl implements Parser {

    private static final Map<String, Predicate<String>> KEYWORDS_WITH_VERIFICATIONS = new HashMap<>();

    private static final DateTimeParser DATE_TIME_PARSER = DateTimeParser.getInstance();

    private static final DurationParser DURATION_PARSER = DurationParser.getInstance();

    static {
        KEYWORDS_WITH_VERIFICATIONS.put("by", DATE_TIME_PARSER::isDateTime);
        KEYWORDS_WITH_VERIFICATIONS.put("from", DATE_TIME_PARSER::isDateTime);
        KEYWORDS_WITH_VERIFICATIONS.put("to", DATE_TIME_PARSER::isDateTime);
        KEYWORDS_WITH_VERIFICATIONS.put("every", DURATION_PARSER::isDurationString);
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
        cmdParser = new CommandParser(commands.keySet(), KEYWORDS_WITH_VERIFICATIONS, defaultCommand,
                FULL_COMMAND_SEPARATOR);
        commandObjectFactory = commandFactory;
    }

    @Override
    public CommandObject parse(String userInput) {
        List<String> commandSegments = cmdParser.parseFullCommand(userInput);

        String command = commandSegments.get(0).toLowerCase();
        commandSegments.remove(0);

        return commands.get(command).apply(commandSegments);
    }

    private CommandObject processAdd(List<String> commandBody) {
        String addCommandBody = restoreCommandSections(commandBody);

        Map<String, String> commandBodySectionsMap = cmdParser.parseCommandBody(addCommandBody);

        return detectAddMethod(commandBodySectionsMap);
    }

    private CommandObject detectAddMethod(Map<String, String> commandBodySectionsMap) {
        if (validAddEventMap(commandBodySectionsMap)) {

            if (!commandBodySectionsMap.containsKey("from")) {
                commandBodySectionsMap.put("from", "");
            }

            if (!commandBodySectionsMap.containsKey("to")) {
                commandBodySectionsMap.put("to", "");
            }

            List<LocalDateTime> startEndDateTime = DATE_TIME_PARSER
                    .parseDoubleDateTime(commandBodySectionsMap.get("from"), commandBodySectionsMap.get("to"));

            if (commandBodySectionsMap.containsKey("every")) {
                TemporalAmount recurringAmount = DURATION_PARSER.parse(commandBodySectionsMap.get("every"));

                return commandObjectFactory
                        .addRecurringEvent(commandBodySectionsMap.get("name"), startEndDateTime.get(0),
                                startEndDateTime.get(1),
                                recurringAmount);
            } else {
                return commandObjectFactory
                        .addEvent(commandBodySectionsMap.get("name"), startEndDateTime.get(0), startEndDateTime.get(1));
            }

        } else if (validAddDeadlineTaskMap(commandBodySectionsMap)) {

            if(!commandBodySectionsMap.containsKey("by")){
                commandBodySectionsMap.put("by", "");
            }

            LocalDateTime dueDate = DATE_TIME_PARSER.parseSingleDateTime(commandBodySectionsMap.get("by"));

            if (commandBodySectionsMap.containsKey("every")) {
                TemporalAmount recurringAmount = DURATION_PARSER.parse(commandBodySectionsMap.get("every"));
                return commandObjectFactory
                        .addRecurringDeadlineTask(commandBodySectionsMap.get("name"), dueDate, recurringAmount);
            } else {
                return commandObjectFactory.addDeadlineTask(commandBodySectionsMap.get("name"), dueDate);
            }

        } else if (validAddFloatingTaskMap(commandBodySectionsMap)) {

            return commandObjectFactory.addFloatingTask(commandBodySectionsMap.get("name"));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean validAddEventMap(Map<String, String> commandBodySectionMap) {
        return commandBodySectionMap.containsKey("from") && !commandBodySectionMap.containsKey("by");
    }

    private boolean validAddDeadlineTaskMap(Map<String, String> commandBodySectionMap) {
        return (commandBodySectionMap.containsKey("by") || commandBodySectionMap.containsKey("every"))
                && !(commandBodySectionMap.containsKey("from") || commandBodySectionMap.containsKey("to"));
    }

    private boolean validAddFloatingTaskMap(Map<String, String> commandBodySectionMap) {
        return !(commandBodySectionMap.containsKey("by")
                || commandBodySectionMap.containsKey("from")
                || commandBodySectionMap.containsKey("to")
                || commandBodySectionMap.containsKey("every"));
    }

    private CommandObject processDelete(List<String> commandBody) throws NumberFormatException {

        String idString = restoreCommandSections(commandBody);

        int id = Integer.parseInt(idString);

        return commandObjectFactory.delete(id);
    }

    private CommandObject processEdit(List<String> commandBody) throws NumberFormatException {
        if (commandBody.size() < 2) {
            throw new IllegalArgumentException();
        }

        String idString = commandBody.get(0);
        int id = Integer.parseInt(idString);

        String editCommandSection = restoreCommandSections(commandBody.subList(1, commandBody.size()));

        Map<String, String> editSectionMap = cmdParser.parseCommandBody(editCommandSection);

        return detectEdit(editSectionMap, id);
    }

    private CommandObject detectEdit(Map<String, String> editSectionMap, int id) {

        String name = editSectionMap.get("name");

        TemporalAmount recurringAmount = null;

        if (editSectionMap.containsKey("every")) {
            recurringAmount = DURATION_PARSER.parse(editSectionMap.get("every"));
        }

        if (validAddEventMap(editSectionMap)) {
            List<LocalDateTime> dateTimes = DATE_TIME_PARSER
                    .parseDoubleDateTime(editSectionMap.get("from"), editSectionMap.get("to"));

            return commandObjectFactory.edit(id, name, dateTimes.get(0), dateTimes.get(1), recurringAmount);
        } else if (validAddDeadlineTaskMap(editSectionMap)) {
            LocalDateTime due = DATE_TIME_PARSER.parseSingleDateTime(editSectionMap.get("by"));

            return commandObjectFactory.edit(id, name, null, due, recurringAmount);
        } else if (validAddFloatingTaskMap(editSectionMap)) {
            return commandObjectFactory.edit(id, name, null, null, null);
        } else {
            throw new IllegalArgumentException();
        }
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
            return commandObjectFactory.find(searchTerm);
        }
    }

    private CommandObject processMark(List<String> commandBody) {
        String idString = restoreCommandSections(commandBody);

        int id = Integer.parseInt(idString);

        return commandObjectFactory.mark(id);
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
