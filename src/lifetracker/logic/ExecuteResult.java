package lifetracker.logic;

import java.util.List;

public interface ExecuteResult {
    
    public enum CommandType {
        DISPLAY, EXIT
    }
    
    String getComment();

    void setComment(String comment);

    List<String> getResultLines();

    void addResultLine(String resultLine);

    CommandType getType();

    void setType(String commandString);
}
