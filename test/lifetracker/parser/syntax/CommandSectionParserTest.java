package lifetracker.parser.syntax;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

//@@author A0091173J
public class CommandSectionParserTest {

    private enum Options{
        NAME, THREE, EMPTY, EMPTY2, ANYTHING
    }

    private Map<String, Options> keywordToEnumMap =  new HashMap<>();

    private Map<Options, Predicate<String>> keyWordVerification = new HashMap<>();

    private CommandSectionParser<Options> cmdParser = new CommandSectionParser<>(keywordToEnumMap, keyWordVerification, Options.NAME);

    @Test
    public void testParseCommandBody() throws Exception {
        keywordToEnumMap.put("three", Options.THREE);
        keywordToEnumMap.put("empty", Options.EMPTY);
        keywordToEnumMap.put("empty2", Options.EMPTY2);
        keywordToEnumMap.put("anything", Options.ANYTHING);
        
        keyWordVerification.put(Options.THREE, s -> s.length() == 3);
        keyWordVerification.put(Options.EMPTY, String::isEmpty);
        keyWordVerification.put(Options.EMPTY2, String::isEmpty);
        keyWordVerification.put(Options.ANYTHING, s -> true);

        //Partition: All keyword arguments valid
        String commandBody = "name empty three abc";

        Map<Options, String> expected = new LinkedHashMap<>();

        expected.put(Options.THREE, "abc");
        expected.put(Options.EMPTY, "");
        expected.put(Options.NAME, "name");

        Assert.assertEquals(expected, cmdParser.parseCommandSection(commandBody));

        //Boundary: Blank name
        commandBody = "empty empty2";

        expected.clear();
        expected.put(Options.EMPTY2, "");
        expected.put(Options.EMPTY, "");
        expected.put(Options.NAME, "");

        Assert.assertEquals(expected, cmdParser.parseCommandSection(commandBody));

        //Partition: repeated keywords
        commandBody = "name anything abs anything abc def three abc";

        expected.clear();
        expected.put(Options.THREE, "abc");
        expected.put(Options.ANYTHING, "abc def");
        expected.put(Options.NAME, "name anything abs");

        Assert.assertEquals(expected, cmdParser.parseCommandSection(commandBody));

        //Partition: Invalid keyword arguements
        commandBody = "anything something something empty aaa";

        expected.clear();
        expected.put(Options.NAME, "anything something something empty aaa");

        Assert.assertEquals(expected, cmdParser.parseCommandSection(commandBody));
    }
}