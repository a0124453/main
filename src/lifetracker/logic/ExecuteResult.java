package lifetracker.logic;

import java.util.List;

public interface ExecuteResult {
    String getComment();

    void setComment();

    List<String> getResultLines();

    void addResultLine(String resultLine);
}
