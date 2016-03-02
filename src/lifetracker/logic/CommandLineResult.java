package lifetracker.logic;

import java.util.ArrayList;
import java.util.List;

public class CommandLineResult implements ExecuteResult {

    private String comment;
    private List<String> resultLines;

    public CommandLineResult() {
        this.resultLines = new ArrayList<>();
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String newComment) {
        comment = newComment;
    }

    @Override
    public List<String> getResultLines() {
        return resultLines;
    }

    @Override
    public void addResultLine(String resultLine) {
        resultLines.add(resultLine);
    }
}
