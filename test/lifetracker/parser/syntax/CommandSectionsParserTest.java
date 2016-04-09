package lifetracker.parser.syntax;

import lifetracker.parser.syntax.CommandSectionsParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandSectionsParserTest {

    private Set<String> commands = new HashSet<>();

    {
        commands.add("add");
        commands.add("testcommand");
    }

    private String defaultCommand = "add";

    private String fullCommandSeparator = "| ";

    private CommandSectionsParser cmdParser = new CommandSectionsParser(commands, defaultCommand);

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

}