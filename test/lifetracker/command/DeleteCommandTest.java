package lifetracker.command;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class DeleteCommandTest {
    
    @Test
    public void testUnexecutedComment() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(1);
        Assert.assertEquals(deleteCommand.getComment(), CommandObject.MESSAGE_ERROR);
    }
}
