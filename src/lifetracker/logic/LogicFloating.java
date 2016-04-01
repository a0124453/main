package lifetracker.logic;

public interface LogicFloating {

    void setName(String recordName);
    
    String getName();
    
    void setId(int recordId);
    
    int getId();
    
    void setDone(boolean recordDone);
    
    boolean isDone();
}
