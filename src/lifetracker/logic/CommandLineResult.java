package lifetracker.logic;

import java.util.ArrayList;
import java.util.List;

public class CommandLineResult implements ExecuteResult {

    private String comment;
    private List<String> resultLines;
    private CommandType commandType;

    public CommandLineResult() {
        this.resultLines = new ArrayList<>();
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String newComment) {
        assert newComment != null;

        comment = newComment;
    }

    @Override
    public List<String> getResultLines() {
        return resultLines;
    }

    @Override
    public void addResultLine(String resultLine) {
        assert resultLine != null;

        resultLines.add(resultLine);
    }

    @Override
    public void setType(String commandString) {
        if (commandString.equals("exit"))
            this.commandType = CommandType.EXIT;

        else
            this.commandType = CommandType.DISPLAY;
    }

    @Override
    public CommandType getType() {
        return this.commandType;
    }
}
