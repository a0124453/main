package lifetracker.logic;

import java.util.List;

import lifetracker.logic.CommandLineResult.CommandType;

public interface ExecuteResult {
    String getComment();

    void setComment(String comment);

    List<String> getResultLines();

    void addResultLine(String resultLine);
    
    CommandType getType();
    
    void setType(String commandString);
}
