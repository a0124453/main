package lifetracker.parser;

import lifetracker.parser.syntax.CommandParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class CommandParserTest {

    private Set<String> commands = new HashSet<>();

    {
        commands.add("add");
        commands.add("testcommand");
    }

    private enum Options{
        NAME, THREE, EMPTY, EMPTY2, ANYTHING
    }

    private Map<String, Predicate<String>> keyWordVerification = new HashMap<>();

    {
        keyWordVerification.put("three", s -> s.length() == 3);
        keyWordVerification.put("empty", String::isEmpty);
        keyWordVerification.put("empty2", String::isEmpty);
        keyWordVerification.put("anything", s -> true);
    }

    private Map<String, Options> keywordToEnumMap =  new HashMap<>();

    {
        keywordToEnumMap.put("three", Options.THREE);
        keywordToEnumMap.put("empty", Options.EMPTY);
        keywordToEnumMap.put("empty2", Options.EMPTY2);
        keywordToEnumMap.put("anything", Options.ANYTHING);
    }

    private String defaultCommand = "add";

    private String fullCommandSeparator = "| ";

    private CommandParser cmdParser = new CommandParser(commands, defaultCommand);

    @Test
    public void testParseFullCommand() throws Exception {

        //Partition: Default (no) command with spaces
        String command = "abc testcommand";

        List<String> expected = new ArrayList<>();

        expected.add(defaultCommand);
        expected.add(command);

        Assert.assertEquals(expected, cmdParser.parseFullCommand(command, fullCommandSeparator));

        //Partition: Explicit command specified
        //Boundary: more than one space between words
        command = "testcommand  add  testcommand ";

        expected.clear();
        expected.add("testcommand");
        expected.add(" add  testcommand ");

        Assert.assertEquals(expected, cmdParser.parseFullCommand(command, fullCommandSeparator));

        //Partition: Command with mulitple sections
        command = "testcommand add testcommand |  add";
        expected.set(1, "add testcommand ");
        expected.add(" add");

        Assert.assertEquals(expected, cmdParser.parseFullCommand(command, fullCommandSeparator));

        expected.clear();

        //Boundary: Spaces after command itself
        //Boundary: Blank last section
        command = "testcommand   | add| two| ";
        expected.add("testcommand");
        expected.add("add");
        expected.add("two");

        Assert.assertEquals(expected, cmdParser.parseFullCommand(command, fullCommandSeparator));

        //Boundary: Spaces right after separator
        command = "testcommand|  abc |def";
        expected.clear();
        expected.add("testcommand");
        expected.add(" abc |def");

        Assert.assertEquals(expected, cmdParser.parseFullCommand(command, fullCommandSeparator));

    }

    @Test
    public void testParseCommandBody() throws Exception {

        //Partition: All keyword arguments valid
        String commandBody = "name empty three abc";

        Map<Options, String> expected = new LinkedHashMap<>();

        expected.put(Options.THREE, "abc");
        expected.put(Options.EMPTY, "");
        expected.put(Options.NAME, "name");

        Assert.assertEquals(expected, cmdParser.parseCommandBody(commandBody, keyWordVerification, keywordToEnumMap, Options.NAME));

        //Boundary: Blank name
        commandBody = "empty empty2";

        expected.clear();
        expected.put(Options.EMPTY2, "");
        expected.put(Options.EMPTY, "");
        expected.put(Options.NAME, "");

        Assert.assertEquals(expected, cmdParser.parseCommandBody(commandBody, keyWordVerification, keywordToEnumMap, Options.NAME));

        //Partition: repeated keywords
        commandBody = "name anything abs anything abc def three abc";

        expected.clear();
        expected.put(Options.THREE, "abc");
        expected.put(Options.ANYTHING, "abc def");
        expected.put(Options.NAME, "name anything abs");

        Assert.assertEquals(expected, cmdParser.parseCommandBody(commandBody, keyWordVerification, keywordToEnumMap, Options.NAME));

        //Partition: Invalid keyword arguements
        commandBody = "anything something something empty aaa";

        expected.clear();
        expected.put(Options.NAME, "anything something something empty aaa");

        Assert.assertEquals(expected, cmdParser.parseCommandBody(commandBody, keyWordVerification, keywordToEnumMap, Options.NAME));
    }
}